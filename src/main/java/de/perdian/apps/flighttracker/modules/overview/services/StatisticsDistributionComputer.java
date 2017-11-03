package de.perdian.apps.flighttracker.modules.overview.services;

class StatisticsDistributionComputer<E extends Enum<E>> {
//
//    private Function<FlightBean, E> propertyFunction = null;
//    private Class<E> enumClass = null;
//
//    StatisticsDistributionComputer(Class<E> enumClass) {
//        this.setEnumClass(enumClass);
//    }
//
//    public List<OverviewItem> computeDistribution(List<FlightBean> flights) {
//
//        EnumMap<E, AtomicInteger> valueMap = new EnumMap<>(this.getEnumClass());
//        for (E enumValue : this.getEnumClass().getEnumConstants()) {
//            valueMap.put(enumValue, new AtomicInteger(0));
//        }
//
//        flights.stream()
//            .map(this.getPropertyFunction())
//            .filter(Objects::nonNull)
//            .forEach(value -> valueMap.get(value).incrementAndGet());
//
//        List<OverviewItem> resultList = new ArrayList<>();
//        for (Map.Entry<E, AtomicInteger> valueEntry : valueMap.entrySet()) {
//
//            OverviewItem resultItem = new OverviewItem();
//            resultItem.setTitle(OverviewItemString.forText(valueEntry.getKey().name()));
//            resultItem.setValue(valueEntry.getValue());
//            resultItem.setPercentage(valueEntry.getValue().intValue() <= 0 ? null : (100d / flights.size()) * valueEntry.getValue().intValue());
//            resultList.add(resultItem);
//
//        }
//        return resultList;
//
//    }
//
//    StatisticsDistributionComputer<E> propertyFunction(Function<FlightBean, E> function) {
//        this.setPropertyFunction(function);
//        return this;
//    }
//
//    private Function<FlightBean, E> getPropertyFunction() {
//        return this.propertyFunction;
//    }
//    private void setPropertyFunction(Function<FlightBean, E> propertyFunction) {
//        this.propertyFunction = propertyFunction;
//    }
//
//    private Class<E> getEnumClass() {
//        return this.enumClass;
//    }
//    private void setEnumClass(Class<E> enumClass) {
//        this.enumClass = enumClass;
//    }

}
