package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

class OverviewStatisticsItemHelpers {

    static <V> BiFunction<List<Flight>, V, Number> distinctValues(BiFunction<Flight, V, List<Object>> valuesExtractor) {
        return (flights, value) -> flights.stream()
            .map(flight -> valuesExtractor.apply(flight, value))
            .flatMap(flightValue -> flightValue.stream())
            .distinct()
            .count();
    }

    static <V> BiFunction<List<Flight>, V, Number> sumValues(BiFunction<Flight, V, Number> valueExtractor) {
        return (flights, value) -> flights.stream()
            .map(flight -> valueExtractor.apply(flight, value))
            .mapToDouble(flightValue -> flightValue == null ? 0d : flightValue.doubleValue())
            .sum();
    }

    static <V> BiFunction<List<Flight>, V, Number> countMatchingValues(Function<Flight, V> valueExtractor) {
        return (flights, value) -> flights.stream()
            .map(flight -> valueExtractor.apply(flight))
            .filter(flightValue -> Objects.equals(flightValue, value))
            .count();
    }

}
