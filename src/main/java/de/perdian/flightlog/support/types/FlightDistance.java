package de.perdian.flightlog.support.types;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public enum FlightDistance implements DescriptionContainer {

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

    @Override
    public String description() {
        NumberFormat numberFormat = new DecimalFormat("#,##0", new DecimalFormatSymbols(Locale.GERMANY));
        StringBuilder rangeString = new StringBuilder();
        if (this.getMinValue() == null) {
            rangeString.append("< ").append(numberFormat.format(this.getMaxValue())).append(" km");
        } else if (this.getMaxValue() == null) {
            rangeString.append("> ").append(numberFormat.format(this.getMinValue())).append(" km");
        } else {
            rangeString.append(numberFormat.format(this.getMinValue())).append(" - ");
            rangeString.append(numberFormat.format(this.getMaxValue())).append(" km");
        }
        return rangeString.toString();
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
