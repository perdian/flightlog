package de.perdian.flightlog.modules.overview.services.contributors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.perdian.flightlog.modules.airlines.model.AirlineBean;
import de.perdian.flightlog.modules.airlines.services.AirlinesService;
import de.perdian.flightlog.modules.airports.persistence.AirportEntity;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.modules.flights.model.FlightBean;
import de.perdian.flightlog.modules.overview.model.OverviewBean;
import de.perdian.flightlog.modules.overview.model.OverviewItem;
import de.perdian.flightlog.modules.overview.model.OverviewItemString;
import de.perdian.flightlog.modules.overview.services.OverviewContributor;
import de.perdian.flightlog.modules.users.persistence.UserEntity;

@Component
class TopFlightsContributor implements OverviewContributor {

    private AirlinesService airlinesService = null;
    private AirportsRepository airportsRepository = null;

    @Override
    public void contributeTo(OverviewBean overviewBean, List<FlightBean> flights, UserEntity user) {
        Map<String, List<OverviewItem>> topFlights = new LinkedHashMap<>();
        topFlights.put("topTenAirports", this.computeTopAirports(flights));
        topFlights.put("topTenAirlines", this.computeTopItems(flights, flight -> Arrays.asList(flight.getAirline().getCode()), airlineCode -> this.computeAirlineName(airlineCode, user), 1d));
        topFlights.put("topTenRoutes", this.computeTopItems(flights, flight -> Arrays.asList(flight.getDepartureContact().getAirport().getCode() + " - " + flight.getArrivalContact().getAirport().getCode()), this::computeAirportName, 1d));
        topFlights.put("topTenAircraftTypes", this.computeTopItems(flights, flight -> Arrays.asList(flight.getAircraft().getType()), null, 1d));
        overviewBean.setTopFlights(topFlights);
    }

    List<OverviewItem> computeTopItems(List<FlightBean> flights, Function<FlightBean, Collection<String>> groupingFunction, Function<String, String> descriptionFunction, double factor) {

        Comparator<Map.Entry<String, AtomicInteger>> valueComparator = (e1, e2) -> -1 * Integer.compare(e1.getValue().intValue(), e2.getValue().intValue());
        Comparator<Map.Entry<String, AtomicInteger>> keyComparator = (e1, e2) -> e1.getKey().compareTo(e2.getKey());

        Map<String, AtomicInteger> values = new HashMap<>();
        for (FlightBean flight : flights) {
            Optional.ofNullable(groupingFunction.apply(flight)).orElseGet(Collections::emptyList).stream().filter(value -> !StringUtils.isEmpty(value)).forEach(value -> {
                values.compute(value, (k, v) -> v == null ? new AtomicInteger() : v).incrementAndGet();
            });
        }

        return values.entrySet().stream().sorted(valueComparator.thenComparing(keyComparator)).limit(10).map(entry -> {

            int currentValue = entry.getValue().intValue();
            Double percentageValue = currentValue <= 0 ? null : (100d / flights.size()) * currentValue * factor;

            OverviewItemString title = OverviewItemString.forText(entry.getKey());
            OverviewItemString description = OverviewItemString.forText(descriptionFunction == null ? null : descriptionFunction.apply(entry.getKey()));
            Number value = currentValue < 0 ? null : currentValue;

            return new OverviewItem(title, description, value, null, percentageValue);

        }).collect(Collectors.toList());
    }

    private List<OverviewItem> computeTopAirports(List<FlightBean> flights) {
        List<OverviewItem> topItems = this.computeTopItems(flights, flight -> Arrays.asList(flight.getDepartureContact().getAirport().getCode(), flight.getArrivalContact().getAirport().getCode()), this::computeAirportName, 0.5d);
        topItems.forEach(topItem -> {
            AirportEntity airportEntity = this.getAirportsRepository().loadAirportByIataCode(topItem.getTitle().getText());
            if (airportEntity != null) {
                topItem.addToContext("countryCode", airportEntity.getCountryCode());
            }
        });
        return topItems;
    }

    private String computeAirlineName(String airlineCode, UserEntity user) {
        return Optional.ofNullable(this.getAirlinesService().loadAirlineByCode(airlineCode, user)).map(AirlineBean::getName).orElse(null);
    }

    private String computeAirportName(String airportCode) {
        return Optional.ofNullable(this.getAirportsRepository().loadAirportByIataCode(airportCode)).map(AirportEntity::getName).orElse(null);
    }

    AirlinesService getAirlinesService() {
        return this.airlinesService;
    }
    @Autowired
    void setAirlinesService(AirlinesService airlinesService) {
        this.airlinesService = airlinesService;
    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

}
