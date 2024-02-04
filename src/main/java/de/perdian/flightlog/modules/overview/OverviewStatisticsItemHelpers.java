package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

class OverviewStatisticsItemHelpers {

    static <V> BiFunction<List<Flight>, V, Number> sumValues(BiFunction<Flight, V, Number> valueExtractor) {
        return (flights, value) -> flights.stream()
            .map(flight -> valueExtractor.apply(flight, value))
            .mapToDouble(flightValue -> flightValue == null ? 0d : flightValue.doubleValue())
            .sum();
    }

    static <V> BiFunction<List<Flight>, V, Number> countMatchingValues(Function<Flight, List<V>> valueExtractor) {
        return (flights, value) -> flights.stream()
            .map(flight -> valueExtractor.apply(flight))
            .flatMap(flightValues -> flightValues == null ? Stream.empty() : flightValues.stream())
            .filter(flightValue -> Objects.equals(flightValue, value))
            .count();
    }

    static <V> BiFunction<List<Flight>, V, Number> countDistinctValues(BiFunction<Flight, V, List<Object>> valuesExtractor) {
        return (flights, value) -> flights.stream()
            .map(flight -> valuesExtractor.apply(flight, value))
            .flatMap(flightValues -> flightValues == null ? Stream.empty() : flightValues.stream())
            .distinct()
            .count();
    }

}
