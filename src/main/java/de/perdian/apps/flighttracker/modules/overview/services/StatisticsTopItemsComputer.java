package de.perdian.apps.flighttracker.modules.overview.services;

class StatisticsTopItemsComputer {
//
//    private Function<FlightBean, Collection<String>> groupingFunction = null;
//    private Function<String, String> descriptionFunction = null;
//    private double factor = 1d;
//    private int maxResults = 0;
//
//    StatisticsTopItemsComputer(Integer maxResults) {
//        this.setMaxResults(maxResults == null ? Integer.MAX_VALUE : maxResults);
//    }
//
//    List<OverviewItem> computeTopItems(List<FlightBean> flights) {
//
//        Comparator<Map.Entry<String, AtomicInteger>> valueComparator = (e1, e2) -> -1 * Integer.compare(e1.getValue().intValue(), e2.getValue().intValue());
//        Comparator<Map.Entry<String, AtomicInteger>> keyComparator = (e1, e2) -> e1.getKey().compareTo(e2.getKey());
//
//        Map<String, AtomicInteger> values = new HashMap<>();
//        for (FlightBean flight : flights) {
//            Optional.ofNullable(this.getGroupingFunction().apply(flight)).orElseGet(Collections::emptyList).stream()
//                .filter(value -> !StringUtils.isEmpty(value))
//                .forEach(value -> {
//                    values.compute(value, (k, v) -> v == null ? new AtomicInteger() : v).incrementAndGet();
//                });
//        }
//
//        return values.entrySet().stream()
//            .sorted(valueComparator.thenComparing(keyComparator))
//            .limit(this.getMaxResults())
//            .map(entry -> {
//
//                int currentValue = entry.getValue().intValue();
//                Double percentageValue = currentValue <= 0 ? null : (100d / flights.size()) * currentValue * this.getFactor();
//
//                OverviewItem topItem = new OverviewItem();
//                topItem.setTitle(OverviewItemString.forText(entry.getKey()));
//                topItem.setDescription(OverviewItemString.forText(this.getDescriptionFunction() == null ? null : this.getDescriptionFunction().apply(entry.getKey())));
//                topItem.setValue(currentValue < 0 ? null : currentValue);
//                topItem.setPercentage(percentageValue);
//                return topItem;
//
//            })
//            .collect(Collectors.toList());
//    }
//
//    StatisticsTopItemsComputer factor(Double factor) {
//        this.setFactor(factor == null ? 1d : factor);
//        return this;
//    }
//
//    StatisticsTopItemsComputer groupingFunction(Function<FlightBean, Collection<String>> function) {
//        this.setGroupingFunction(function);
//        return this;
//    }
//    private Function<FlightBean, Collection<String>> getGroupingFunction() {
//        return this.groupingFunction;
//    }
//    private void setGroupingFunction(Function<FlightBean, Collection<String>> groupingFunction) {
//        this.groupingFunction = groupingFunction;
//    }
//
//    StatisticsTopItemsComputer descriptionFunction(Function<String, String> function) {
//        this.setDescriptionFunction(function);
//        return this;
//    }
//    private Function<String, String> getDescriptionFunction() {
//        return this.descriptionFunction;
//    }
//    private void setDescriptionFunction(Function<String, String> descriptionFunction) {
//        this.descriptionFunction = descriptionFunction;
//    }
//
//    private double getFactor() {
//        return this.factor;
//    }
//    private void setFactor(double factor) {
//        this.factor = factor;
//    }
//
//    private int getMaxResults() {
//        return this.maxResults;
//    }
//    private void setMaxResults(int maxResults) {
//        this.maxResults = maxResults;
//    }

}
