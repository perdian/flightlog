package de.perdian.apps.flighttracker.business.modules.overview.contributors;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import de.perdian.apps.flighttracker.business.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.business.modules.overview.model.StatisticsTopItem;

class StatisticsDistributionComputer<E extends Enum<E>> {

    private Function<FlightBean, E> propertyFunction = null;
    private Class<E> enumClass = null;

    StatisticsDistributionComputer(Class<E> enumClass) {
        this.setEnumClass(enumClass);
    }

    public List<StatisticsTopItem> computeDistribution(List<FlightBean> flights) {

        EnumMap<E, AtomicInteger> valueMap = new EnumMap<>(this.getEnumClass());
        for (E enumValue : this.getEnumClass().getEnumConstants()) {
            valueMap.put(enumValue, new AtomicInteger(0));
        }

        flights.stream()
            .map(this.getPropertyFunction())
            .filter(Objects::nonNull)
            .forEach(value -> valueMap.get(value).incrementAndGet());

        List<StatisticsTopItem> resultList = new ArrayList<>();
        for (Map.Entry<E, AtomicInteger> valueEntry : valueMap.entrySet()) {

            StatisticsTopItem resultItem = new StatisticsTopItem();
            resultItem.setTitle(valueEntry.getKey().name());
            resultItem.setValue(valueEntry.getValue());
            resultItem.setPercentage((100d / flights.size()) * valueEntry.getValue().intValue());
            resultList.add(resultItem);

        }
        return resultList;

    }

    StatisticsDistributionComputer<E> propertyFunction(Function<FlightBean, E> function) {
        this.setPropertyFunction(function);
        return this;
    }

    private Function<FlightBean, E> getPropertyFunction() {
        return this.propertyFunction;
    }
    private void setPropertyFunction(Function<FlightBean, E> propertyFunction) {
        this.propertyFunction = propertyFunction;
    }

    private Class<E> getEnumClass() {
        return this.enumClass;
    }
    private void setEnumClass(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

}
