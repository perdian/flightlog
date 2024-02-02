package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.support.types.FlightDistance;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

@ControllerAdvice(assignableTypes = OverviewController.class)
class OverviewStatisticsAdvice {

    @ModelAttribute(name = "overviewStatistics", binding = false)
    OverviewStatistics overviewStatistics(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        OverviewStatistics overviewStatistics = new OverviewStatistics();
        overviewStatistics.setFlightTotals(this.createFlightTotals(flights));
        overviewStatistics.setFlightsByDistance(this.createFlightsByDistance(flights));
        overviewStatistics.setOtherStatistics(this.createOtherStatistics(flights));
        return overviewStatistics;
    }

    private List<OverviewStatisticsItem> createFlightTotals(List<Flight> flights) {
        return List.of(
            this.createCumulatedNumberStatisticsItem(flights, flight -> 1d).withTitle(OverviewString.forKey("totalNumber")),
            this.createCumulatedNumberStatisticsItem(flights, flight -> flight.getFlightDistance()).withTitle(OverviewString.forKey("distanceInKm")),
            this.createCumulatedNumberStatisticsItem(flights, flight -> flight.getFlightDistance() / 1609d).withTitle(OverviewString.forKey("distanceInMiles")),
            this.createCumulatedNumberStatisticsItem(flights, flight -> flight.getFlightDuration() == null ? 0 : (flight.getFlightDuration().toMinutes() / 60d)).withTitle(OverviewString.forKey("durationInHours")),
            this.createCumulatedNumberStatisticsItem(flights, flight -> flight.getFlightDuration() == null ? 0 : (flight.getFlightDuration().toMinutes() / 60d / 24d)).withTitle(OverviewString.forKey("durationInDays")).withValueFormat("#,##0.0")
        );
    }

    private List<OverviewStatisticsItem> createFlightsByDistance(List<Flight> flights) {
        return Stream.of(FlightDistance.values())
            .map(flightDistance -> this.createFlightsByDistanceItem(flights, flightDistance))
            .toList();
    }

    private OverviewStatisticsItem createFlightsByDistanceItem(List<Flight> flights, FlightDistance flightDistance) {
        return this.createCumulatedNumberStatisticsItem(flights, flight -> flightDistance.equals(flight.getFlightDistanceType()) ? 1 : 0)
            .withTitle(OverviewString.forEnum(flightDistance))
            .withDescription(OverviewString.forValue(flightDistance.toRangeString()));
    }

    private List<OverviewStatisticsItem> createOtherStatistics(List<Flight> flights) {
        return List.of(
            this.createDistinctNumberStatisticsItem(flights, flight -> List.of(flight.getAircraft().getType())).withTitle(OverviewString.forKey("numberOfDifferentAircraftTypes")),
            this.createDistinctNumberStatisticsItem(flights, flight -> List.of(flight.getDepartureContact().getAirport().getCode(), flight.getArrivalContact().getAirport().getCode())).withTitle(OverviewString.forKey("numberOfDifferentAirports")),
            this.createDistinctNumberStatisticsItem(flights, flight -> List.of(flight.getDepartureContact().getAirport().getCountryCode(), flight.getArrivalContact().getAirport().getCountryCode())).withTitle(OverviewString.forKey("numberOfDifferentCountries")),
            this.createDistinctNumberStatisticsItem(flights, flight -> List.of(flight.getDepartureContact().getAirport().getCode() + "-" + flight.getArrivalContact().getAirport().getCode())).withTitle(OverviewString.forKey("numberOfDifferentRoutes")),
            this.createDistinctNumberStatisticsItem(flights, flight -> List.of(flight.getAirline().getCode())).withTitle(OverviewString.forKey("numberOfDifferentAirlines"))
        );
    }

    private OverviewStatisticsItem createCumulatedNumberStatisticsItem(List<Flight> flights, Function<Flight, Number> valueExtractorFunction) {

        Double totalValue = flights.stream()
            .map(valueExtractorFunction)
            .filter(Objects::nonNull)
            .mapToDouble(Number::doubleValue)
            .sum();

        return new OverviewStatisticsItem().withValue(totalValue);

    }

    private OverviewStatisticsItem createDistinctNumberStatisticsItem(List<Flight> flights, Function<Flight, List<?>> valuesExtractorFunction) {

        Long totalValue = flights.stream()
            .map(valuesExtractorFunction)
            .flatMap(Collection::stream)
            .filter(Objects::nonNull)
            .distinct()
            .count();

        return new OverviewStatisticsItem().withValue(totalValue);

    }

}
