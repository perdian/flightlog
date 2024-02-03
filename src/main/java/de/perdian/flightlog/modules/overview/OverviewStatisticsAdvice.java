package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.support.types.FlightDistance;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@ControllerAdvice(assignableTypes = OverviewController.class)
class OverviewStatisticsAdvice {

    @ModelAttribute(name = "overviewStatistics", binding = false)
    OverviewStatistics overviewStatistics(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        OverviewStatistics overviewStatistics = new OverviewStatistics();

        overviewStatistics.setFlightTotals(
            OverviewStatisticsCounter.<OverviewStatisticsTotals, Number>forEnum(OverviewStatisticsTotals.class)
                .withFlightToPropertyValueFunction((flight, value) -> value.getResultFromMatchingFlightFunction().apply(flight))
                .withPropertyValueToResultFunction(value -> value)
                .withPropertyValuePredicate((propertyValue, value) -> true)
                .withResultValueFormat("#,##0.#")
                .createStatisticsItems(flights)
        );

        overviewStatistics.setFlightsByDistance(
            OverviewStatisticsCounter.forEnum(FlightDistance.class, flight -> flight.getFlightDistanceType())
                .withValueToDescriptionFunction(value -> OverviewString.forValue(value.toRangeString()))
                .createStatisticsItems(flights)
        );

        overviewStatistics.setOtherStatistics(List.of(
            this.createDistinctNumberStatisticsItem(flights, flight -> List.of(flight.getAircraft().getType())).withTitle(OverviewString.forKey("numberOfDifferentAircraftTypes")),
            this.createDistinctNumberStatisticsItem(flights, flight -> List.of(flight.getDepartureContact().getAirport().getCode(), flight.getArrivalContact().getAirport().getCode())).withTitle(OverviewString.forKey("numberOfDifferentAirports")),
            this.createDistinctNumberStatisticsItem(flights, flight -> List.of(flight.getDepartureContact().getAirport().getCountryCode(), flight.getArrivalContact().getAirport().getCountryCode())).withTitle(OverviewString.forKey("numberOfDifferentCountries")),
            this.createDistinctNumberStatisticsItem(flights, flight -> List.of(flight.getDepartureContact().getAirport().getCode() + "-" + flight.getArrivalContact().getAirport().getCode())).withTitle(OverviewString.forKey("numberOfDifferentRoutes")),
            this.createDistinctNumberStatisticsItem(flights, flight -> List.of(flight.getAirline().getCode())).withTitle(OverviewString.forKey("numberOfDifferentAirlines"))
        ));

        return overviewStatistics;

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
