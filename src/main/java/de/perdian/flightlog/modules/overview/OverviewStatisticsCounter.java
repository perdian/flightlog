package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

class OverviewStatisticsCounter<V, P> {

    private Function<List<Flight>, List<V>> flightsToValuesFunction = null;
    private BiFunction<Flight, V, P> flightToPropertyValueFunction = null;
    private Function<P, Number> propertyValueToResultFunction = propertyValue -> 1;
    private BiPredicate<P, V> propertyValuePredicate = Objects::equals;
    private Function<V, OverviewString> valueToTitleFunction = null;
    private Function<V, OverviewString> valueToDescriptionFunction = null;
    private Function<V, String> valueToIconCodeFunction = null;
    private String resultValueFormat = "#,##0";
    private boolean includePercentages = false;

    static <E extends Enum<E>, P> OverviewStatisticsCounter<E, P> forEnum(Class<E> enumClass) {
        return new OverviewStatisticsCounter<E, P>()
            .withFlightsToValuesFunction(flights -> List.of(enumClass.getEnumConstants()))
            .withValueToTitleFunction(value -> OverviewString.forEnum(value));
    }

    static <E extends Enum<E>> OverviewStatisticsCounter<E, E> forEnum(Class<E> enumClass, Function<Flight, E> flightToValueFromFunction) {
        return new OverviewStatisticsCounter<E, E>()
            .withFlightsToValuesFunction(flights -> List.of(enumClass.getEnumConstants()))
            .withFlightToPropertyValueFunction((flight, value) -> flightToValueFromFunction.apply(flight))
            .withValueToTitleFunction(value -> OverviewString.forEnum(value));
    }

    List<OverviewStatisticsItem> createStatisticsItems(List<Flight> flights) {
        return this.getFlightsToValuesFunction().apply(flights).stream()
            .map(value -> this.createStatisticsItem(flights, value))
            .toList();
    }

    private OverviewStatisticsItem createStatisticsItem(List<Flight> flights, V value) {

        Number resultValue = flights.stream()
            .map(flight -> this.getFlightToPropertyValueFunction().apply(flight, value))
            .filter(flightPropertyValue -> this.getPropertyValuePredicate().test(flightPropertyValue, value))
            .map(flightPropertyValue -> this.getPropertyValueToResultFunction().apply(flightPropertyValue))
            .mapToDouble(flightResultValue -> flightResultValue == null ? 0d : flightResultValue.doubleValue())
            .sum();

        OverviewStatisticsItem statisticsItem = new OverviewStatisticsItem();
        statisticsItem.setTitle(this.getValueToTitleFunction().apply(value));
        statisticsItem.setDescription(this.getValueToDescriptionFunction() == null ? null : this.getValueToDescriptionFunction().apply(value));
        statisticsItem.setIconCode(this.getValueToIconCodeFunction() == null ? null : this.getValueToIconCodeFunction().apply(value));
        statisticsItem.setValue(resultValue);
        statisticsItem.setValueFormat(this.getResultValueFormat());
        if (this.isIncludePercentages()) {
            statisticsItem.setPercentage(flights.isEmpty() ? 0d : resultValue.doubleValue() / flights.size());
        }
        return statisticsItem;

    }

    OverviewStatisticsCounter<V, P> withFlightsToValuesFunction(Function<List<Flight>, List<V>> flightsToValuesFunction) {
        this.setFlightsToValuesFunction(flightsToValuesFunction);
        return this;
    }
    Function<List<Flight>, List<V>> getFlightsToValuesFunction() {
        return this.flightsToValuesFunction;
    }
    void setFlightsToValuesFunction(Function<List<Flight>, List<V>> flightsToValuesFunction) {
        this.flightsToValuesFunction = flightsToValuesFunction;
    }

    OverviewStatisticsCounter<V, P> withFlightToPropertyValueFunction(BiFunction<Flight, V, P> flightToPropertyValueFunction) {
        this.setFlightToPropertyValueFunction(flightToPropertyValueFunction);
        return this;
    }
    BiFunction<Flight, V, P> getFlightToPropertyValueFunction() {
        return this.flightToPropertyValueFunction;
    }
    void setFlightToPropertyValueFunction(BiFunction<Flight, V, P> flightToPropertyValueFunction) {
        this.flightToPropertyValueFunction = flightToPropertyValueFunction;
    }

    OverviewStatisticsCounter<V, P> withPropertyValueToResultFunction(Function<P, Number> propertyValueToResultFunction) {
        this.setPropertyValueToResultFunction(propertyValueToResultFunction);
        return this;
    }
    Function<P, Number> getPropertyValueToResultFunction() {
        return this.propertyValueToResultFunction;
    }
    void setPropertyValueToResultFunction(Function<P, Number> propertyValueToResultFunction) {
        this.propertyValueToResultFunction = propertyValueToResultFunction;
    }

    OverviewStatisticsCounter<V, P> withPropertyValuePredicate(BiPredicate<P, V> propertyValuePredicate) {
        this.setPropertyValuePredicate(propertyValuePredicate);
        return this;
    }
    BiPredicate<P, V> getPropertyValuePredicate() {
        return this.propertyValuePredicate;
    }
    void setPropertyValuePredicate(BiPredicate<P, V> propertyValuePredicate) {
        this.propertyValuePredicate = propertyValuePredicate;
    }

    OverviewStatisticsCounter<V, P> withValueToTitleFunction(Function<V, OverviewString> valueToTitleFunction) {
        this.setValueToTitleFunction(valueToTitleFunction);
        return this;
    }
    Function<V, OverviewString> getValueToTitleFunction() {
        return this.valueToTitleFunction;
    }
    void setValueToTitleFunction(Function<V, OverviewString> valueToTitleFunction) {
        this.valueToTitleFunction = valueToTitleFunction;
    }

    OverviewStatisticsCounter<V, P> withValueToDescriptionFunction(Function<V, OverviewString> valueToDescriptionFunction) {
        this.setValueToDescriptionFunction(valueToDescriptionFunction);
        return this;
    }
    Function<V, OverviewString> getValueToDescriptionFunction() {
        return this.valueToDescriptionFunction;
    }
    void setValueToDescriptionFunction(Function<V, OverviewString> valueToDescriptionFunction) {
        this.valueToDescriptionFunction = valueToDescriptionFunction;
    }

    OverviewStatisticsCounter<V, P> withValueToIconCode(Function<V, String> valueToIconCodeFunction) {
        this.setValueToIconCodeFunction(valueToIconCodeFunction);
        return this;
    }
    Function<V, String> getValueToIconCodeFunction() {
        return this.valueToIconCodeFunction;
    }
    void setValueToIconCodeFunction(Function<V, String> valueToIconCodeFunction) {
        this.valueToIconCodeFunction = valueToIconCodeFunction;
    }

    OverviewStatisticsCounter<V, P> withResultValueFormat(String resultValueFormat) {
        this.setResultValueFormat(resultValueFormat);
        return this;
    }
    String getResultValueFormat() {
        return this.resultValueFormat;
    }
    void setResultValueFormat(String resultValueFormat) {
        this.resultValueFormat = resultValueFormat;
    }

    OverviewStatisticsCounter<V, P> withIncludePercentages(boolean includePercentages) {
        this.setIncludePercentages(includePercentages);
        return this;
    }
    public boolean isIncludePercentages() {
        return this.includePercentages;
    }
    public void setIncludePercentages(boolean includePercentages) {
        this.includePercentages = includePercentages;
    }

}
