package de.perdian.flightlog.support.types;

public enum FlightDistance {

    SHORT(null, 1500),
    MEDIUM(1500, 3500),
    LONG(3500, 10000),
    ULTRALONG(10000, null);

    private Integer minValue = null;
    private Integer maxValue = null;

    private FlightDistance(Integer minValue, Integer maxValue) {
        this.setMinValue(minValue);
        this.setMaxValue(maxValue);
    }

    public boolean matches(int value) {
        boolean minMatches = this.getMinValue() == null || this.getMinValue().intValue() < value;
        boolean maxMatches = this.getMaxValue() == null || this.getMaxValue().intValue() >= value;
        return minMatches && maxMatches;
    }

    public Integer getMinValue() {
        return this.minValue;
    }
    private void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public Integer getMaxValue() {
        return this.maxValue;
    }
    private void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

}
