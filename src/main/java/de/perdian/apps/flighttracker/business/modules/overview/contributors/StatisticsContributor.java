package de.perdian.apps.flighttracker.business.modules.overview.contributors;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.perdian.apps.flighttracker.business.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.business.modules.overview.OverviewContributor;
import de.perdian.apps.flighttracker.business.modules.overview.model.OverviewBean;
import de.perdian.apps.flighttracker.business.modules.overview.model.StatisticsBean;
import de.perdian.apps.flighttracker.business.modules.overview.model.StatisticsTopItem;
import de.perdian.apps.flighttracker.persistence.entities.AirlineEntity;
import de.perdian.apps.flighttracker.persistence.entities.AirportEntity;
import de.perdian.apps.flighttracker.persistence.repositories.AirlinesRepository;
import de.perdian.apps.flighttracker.persistence.repositories.AirportsRepository;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightDistance;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

@Component
public class StatisticsContributor implements OverviewContributor {

    private AirlinesRepository airlinesRepository = null;
    private AirportsRepository airportsRepository = null;

    @Override
    public void contribute(OverviewBean overview, List<FlightBean> flights) {

        int totalDistanceInKilometers = this.computeDistanceInKilometers(flights);
        int totolDistanceInMiles = (int)(totalDistanceInKilometers * 0.621371d);
        Duration duration = this.computeDuration(flights);

        StatisticsBean statistics = new StatisticsBean();
        statistics.setDistanceInKilometers(totalDistanceInKilometers);
        statistics.setDistanceInMiles(totolDistanceInMiles);
        statistics.setEarthOrbits(totalDistanceInKilometers / 40074d);
        statistics.setEarthToMoon(totalDistanceInKilometers / 384400d);
        statistics.setEarthToSun(totalDistanceInKilometers / 149600000d);
        statistics.setDurationInHours(duration.toMinutes() / 60d);
        statistics.setDurationInDays(duration.toMinutes() / (60d * 24d));
        statistics.setDurationInWeeks(duration.toMinutes() / (60d * 24d * 7d));
        statistics.setDurationInMonths(duration.toMinutes() / (60d * 24d * 30.41d));
        statistics.setDurationInYears(duration.toMinutes() / (60d * 24d * 365d));
        statistics.setLongestFlightByDuration(this.computeMaxFlight(flights, 1, FlightBean::getFlightDuration));
        statistics.setLongestFlightByDistance(this.computeMaxFlight(flights, 1, FlightBean::getFlightDistance));
        statistics.setShortestFlightByDuration(this.computeMaxFlight(flights, -1, FlightBean::getFlightDuration));
        statistics.setShortestFlightByDistance(this.computeMaxFlight(flights, -1, FlightBean::getFlightDistance));
        statistics.setFastestFlight(this.computeMaxFlight(flights, 1, FlightBean::getAverageSpeed));
        statistics.setSlowestFlight(this.computeMaxFlight(flights, -1, FlightBean::getAverageSpeed));
        statistics.setTopAircraftTypes(new StatisticsTopItemsComputer(10).groupingFunction(flight -> Arrays.asList(flight.getAircraft().getType())).computeTopItems(flights));
        statistics.setTopAirlines(new StatisticsTopItemsComputer(10).groupingFunction(flight -> Arrays.asList(flight.getAirline().getCode())).descriptionFunction(this::computeAirlineName).computeTopItems(flights));
        statistics.setTopAirports(this.computeTopAirports(flights));
        statistics.setTopRoutes(new StatisticsTopItemsComputer(10).groupingFunction(flight -> Arrays.asList(flight.getDepartureContact().getAirport().getCode() + " - " + flight.getArrivalContact().getAirport().getCode())).computeTopItems(flights));
        statistics.setDistributionOfCabinClasses(new StatisticsDistributionComputer<>(CabinClass.class).propertyFunction(FlightBean::getCabinClass).computeDistribution(flights));
        statistics.setDistributionOfSeatTypes(new StatisticsDistributionComputer<>(SeatType.class).propertyFunction(FlightBean::getSeatType).computeDistribution(flights));
        statistics.setDistributionOfFlightReasons(new StatisticsDistributionComputer<>(FlightReason.class).propertyFunction(FlightBean::getFlightReason).computeDistribution(flights));
        statistics.setNumberOfFlights(flights.size());
        statistics.setNumberOfFlightsByDistance(this.computeNumberOfFlightsByDistance(flights));
        statistics.setNumberOfDifferentAircraftTypes(this.countDifferentValues(flights, flight -> Arrays.asList(flight.getAircraft().getType())));
        statistics.setNumberOfDifferentAirlines(this.countDifferentValues(flights, flight -> Arrays.asList(flight.getAirline().getCode())));
        statistics.setNumberOfDifferentAirports(this.countDifferentValues(flights, flight -> Arrays.asList(flight.getDepartureContact().getAirport().getCode(), flight.getDepartureContact().getAirport().getCode())));
        statistics.setNumberOfDifferentCountries(this.countDifferentValues(flights, flight -> Arrays.asList(flight.getDepartureContact().getAirport().getCountryCode(), flight.getDepartureContact().getAirport().getCountryCode())));
        statistics.setNumberOfDifferentRoutes(this.countDifferentValues(flights, flight -> Arrays.asList(flight.getDepartureContact().getAirport().getCode() + "-" + flight.getArrivalContact().getAirport().getCode())));

        overview.setStatistics(statistics);

    }

    private List<StatisticsTopItem> computeTopAirports(List<FlightBean> flights) {
        List<StatisticsTopItem> topItems = new StatisticsTopItemsComputer(10).groupingFunction(flight -> Arrays.asList(flight.getDepartureContact().getAirport().getCode(), flight.getArrivalContact().getAirport().getCode())).descriptionFunction(this::computeAirportName).factor(0.5d).computeTopItems(flights);
        topItems.forEach(topItem -> {
            AirportEntity airportEntity = this.getAirportsRepository().loadAirportByIataCode(topItem.getTitle());
            if (airportEntity != null) {
                topItem.addToContext("countryCode", airportEntity.getCountryCode());
            }
        });
        return topItems;
    }

    private Map<FlightDistance, Integer> computeNumberOfFlightsByDistance(List<FlightBean> flights) {
        Map<FlightDistance, Integer> resultMap = new LinkedHashMap<>();
        for (FlightDistance flightDistance : FlightDistance.values()) {
            resultMap.put(flightDistance, (int)flights.stream()
                .filter(flight -> flight.getFlightDistance() != null)
                .mapToInt(flight -> flight.getFlightDistance())
                .filter(value -> flightDistance.matches(value)).count()
            );
        }
        return resultMap;
    }

    private int computeDistanceInKilometers(List<FlightBean> flights) {
        return flights.stream()
            .filter(flight -> flight.getFlightDistance() != null)
            .mapToInt(flight -> flight.getFlightDistance())
            .sum();
    }

    private Duration computeDuration(List<FlightBean> flights) {
        return Duration.ofMinutes(flights.stream()
            .filter(flight -> flight.getFlightDuration() != null)
            .mapToInt(flight -> (int)flight.getFlightDuration().toMinutes())
            .sum());
    }

    private <T extends Comparable<T>> FlightBean computeMaxFlight(List<FlightBean> flights, int direction, Function<FlightBean, T> valueFunction) {
        FlightBean maxFlight = null;
        T maxValue = null;
        for (FlightBean currentFlight : flights) {
            T currentValue = valueFunction.apply(currentFlight);
            if (currentValue != null) {
                if (maxValue == null) {
                    maxFlight = currentFlight;
                    maxValue = currentValue;
                } else {
                    int compareValue = currentValue.compareTo(maxValue) * direction;
                    if (compareValue > 0) {
                        maxFlight = currentFlight;
                        maxValue = currentValue;
                    }
                }
            }
        }
        return maxFlight;
    }

    private Integer countDifferentValues(List<FlightBean> flights, Function<FlightBean, Collection<String>> valueFunction) {
        return (int)flights.stream()
            .map(valueFunction)
            .flatMap(Collection::stream)
            .distinct()
            .filter(value -> !StringUtils.isEmpty(value))
            .count();
    }

    private String computeAirlineName(String airlineCode) {
        return Optional.ofNullable(this.getAirlinesRepository().loadAirlineByIataCode(airlineCode)).map(AirlineEntity::getName).orElse(null);
    }

    private String computeAirportName(String airportCode) {
        return Optional.ofNullable(this.getAirportsRepository().loadAirportByIataCode(airportCode)).map(AirportEntity::getName).orElse(null);
    }

    AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

}
