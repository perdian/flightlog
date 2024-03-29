package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

class OverviewStatisticsGroupFactory<V> {

    private OverviewString title = null;
    private List<OverviewStatisticsItemFactory<V>> itemFactories = null;
    private Comparator<OverviewStatisticsItem> itemComparator = null;
    private Integer maxItems = null;

    static <V> OverviewStatisticsGroupFactory<V> forItemFactories(List<OverviewStatisticsItemFactory<V>> itemFactories) {
        return new OverviewStatisticsGroupFactory<V>().withItemFactories(itemFactories);
    }

    static <E extends Enum<E>> OverviewStatisticsGroupFactory<E> forEnum(Class<E> enumClass, BiFunction<List<Flight>, E, Number> resultValueFunction, boolean withPercentages) {

        List<OverviewStatisticsItemFactory<E>> itemFactoriesForEnumMatchingCount = Stream.of(enumClass.getEnumConstants())
            .map(enumValue -> OverviewStatisticsItemFactory.forEnum(enumValue, withPercentages).withResultValueFunction(resultValueFunction))
            .toList();

        return new OverviewStatisticsGroupFactory<E>().withItemFactories(itemFactoriesForEnumMatchingCount);

    }

    OverviewStatisticsGroup createGroup(List<Flight> flights) {
        OverviewStatisticsGroup group = new OverviewStatisticsGroup();
        group.setTitle(this.getTitle());
        group.setItems(this.createGroupItems(flights));
        return group;
    }

    private List<OverviewStatisticsItem> createGroupItems(List<Flight> flights) {

        List<OverviewStatisticsItem> allUnsortedItems = this.getItemFactories().stream()
            .map(itemFactory -> itemFactory.createItem(flights))
            .toList();

        List<OverviewStatisticsItem> allSortedItems = this.getItemComparator() == null ? allUnsortedItems : allUnsortedItems.stream().sorted(this.getItemComparator()).toList();
        List<OverviewStatisticsItem> filteredSortedItems = this.getMaxItems() == null ? allSortedItems : allSortedItems.stream().limit(this.getMaxItems()).toList();

        return filteredSortedItems;

    }

    OverviewStatisticsGroupFactory<V> withTitle(OverviewString title) {
        this.setTitle(title);
        return this;
    }
    OverviewString getTitle() {
        return this.title;
    }
    void setTitle(OverviewString title) {
        this.title = title;
    }

    void addItemFactory(OverviewStatisticsItemFactory<V> itemFactory) {
        if (this.getItemFactories() == null) {
            this.setItemFactories(new ArrayList<>());
        }
        this.getItemFactories().add(itemFactory);
    }

    OverviewStatisticsGroupFactory<V> withItemFactories(List<OverviewStatisticsItemFactory<V>> itemFactories) {
        this.setItemFactories(itemFactories);
        return this;
    }
    List<OverviewStatisticsItemFactory<V>> getItemFactories() {
        return this.itemFactories;
    }
    void setItemFactories(List<OverviewStatisticsItemFactory<V>> itemFactories) {
        this.itemFactories = itemFactories;
    }

    OverviewStatisticsGroupFactory<V> withItemComparatorByValueDesc() {
        Comparator<OverviewStatisticsItem> valueComparator = OverviewStatisticsItem::compareByValue;
        Comparator<OverviewStatisticsItem> titleComparator = OverviewStatisticsItem::compareByTitle;
        return this.withItemComparator(valueComparator.reversed().thenComparing(titleComparator));
    }
    OverviewStatisticsGroupFactory<V> withItemComparator(Comparator<OverviewStatisticsItem> itemComparator) {
        this.setItemComparator(itemComparator);
        return this;
    }
    Comparator<OverviewStatisticsItem> getItemComparator() {
        return this.itemComparator;
    }
    void setItemComparator(Comparator<OverviewStatisticsItem> itemComparator) {
        this.itemComparator = itemComparator;
    }

    public OverviewStatisticsGroupFactory<V> withMaxItems(Integer maxItems) {
        this.setMaxItems(maxItems);
        return this;
    }
    Integer getMaxItems() {
        return this.maxItems;
    }
    void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

}
