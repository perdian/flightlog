package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.support.types.DescriptionContainer;

import java.util.List;
import java.util.function.BiFunction;

class OverviewStatisticsItemFactory<V> {

    private V itemValue = null;
    private OverviewString itemTitle = null;
    private OverviewString itemDescription = null;
    private String itemIconCode = null;
    private boolean isItemPercentage = false;
    private BiFunction<List<Flight>, V, Number> resultValueFunction = null;

    OverviewStatisticsItem createItem(List<Flight> flights) {
        Number itemValue = this.getResultValueFunction().apply(flights, this.getItemValue());
        OverviewStatisticsItem item = new OverviewStatisticsItem();
        item.setTitle(this.getItemTitle());
        item.setDescription(this.getItemDescription());
        item.setIconCode(this.getItemIconCode());
        item.setValue(itemValue);
        if (this.isItemPercentage()) {
            item.setPercentage(itemValue == null || flights.isEmpty() ? 0d : itemValue.doubleValue() / flights.size());
        }
        return item;
    }

    static <E extends Enum<E>> OverviewStatisticsItemFactory<E> forEnum(E enumValue) {
        return new OverviewStatisticsItemFactory<E>()
            .withItemValue(enumValue)
            .withItemTitle(OverviewString.forEnum(enumValue))
            .withItemDescription(enumValue instanceof DescriptionContainer descriptionValue ? OverviewString.forValue(descriptionValue.description()) : null)
        ;
    }

    OverviewStatisticsItemFactory<V> withItemValue(V itemValue) {
        this.setItemValue(itemValue);
        return this;
    }
    V getItemValue() {
        return this.itemValue;
    }
    void setItemValue(V itemValue) {
        this.itemValue = itemValue;
    }

    OverviewStatisticsItemFactory<V> withItemTitle(OverviewString itemTitle) {
        this.setItemTitle(itemTitle);
        return this;
    }
    OverviewString getItemTitle() {
        return this.itemTitle;
    }
    void setItemTitle(OverviewString itemTitle) {
        this.itemTitle = itemTitle;
    }

    OverviewStatisticsItemFactory<V> withItemDescription(OverviewString itemDescription) {
        this.setItemDescription(itemDescription);
        return this;
    }
    OverviewString getItemDescription() {
        return this.itemDescription;
    }
    void setItemDescription(OverviewString itemDescription) {
        this.itemDescription = itemDescription;
    }

    OverviewStatisticsItemFactory<V> withItemIconCode(String itemIconCode) {
        this.setItemIconCode(itemIconCode);
        return this;
    }
    String getItemIconCode() {
        return this.itemIconCode;
    }
    void setItemIconCode(String itemIconCode) {
        this.itemIconCode = itemIconCode;
    }

    OverviewStatisticsItemFactory<V> withItemPercentage(boolean itemPercentage) {
        this.setItemPercentage(itemPercentage);
        return this;
    }
    boolean isItemPercentage() {
        return this.isItemPercentage;
    }
    void setItemPercentage(boolean itemPercentage) {
        isItemPercentage = itemPercentage;
    }

    OverviewStatisticsItemFactory<V> withResultValueFunction(BiFunction<List<Flight>, V, Number> resultValueFunction) {
        this.setResultValueFunction(resultValueFunction);
        return this;
    }
    BiFunction<List<Flight>, V, Number> getResultValueFunction() {
        return this.resultValueFunction;
    }
    void setResultValueFunction(BiFunction<List<Flight>, V, Number> resultValueFunction) {
        this.resultValueFunction = resultValueFunction;
    }

}
