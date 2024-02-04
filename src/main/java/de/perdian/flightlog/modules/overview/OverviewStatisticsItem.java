package de.perdian.flightlog.modules.overview;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class OverviewStatisticsItem {

    private OverviewString title = null;
    private OverviewString description = null;
    private String iconCode = null;
    private Number value = null;
    private String valueFormat = "#,##0.#";
    private Number percentage = null;

    public static int compareByValue(OverviewStatisticsItem i1, OverviewStatisticsItem i2) {
        return Double.compare(i1.getValue().doubleValue(), i2.getValue().doubleValue());
    }

    public OverviewString getTitle() {
        return this.title;
    }
    void setTitle(OverviewString title) {
        this.title = title;
    }

    public OverviewString getDescription() {
        return this.description;
    }
    void setDescription(OverviewString description) {
        this.description = description;
    }

    public String getIconCode() {
        return this.iconCode;
    }
    void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public String getValueString() {
        return this.getValue() == null ? null : new DecimalFormat(this.getValueFormat(), new DecimalFormatSymbols(Locale.GERMANY)).format(this.getValue());
    }

    public Number getValue() {
        return this.value;
    }
    void setValue(Number value) {
        this.value = value;
    }

    public String getValueFormat() {
        return this.valueFormat;
    }
    void setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
    }

    public String getPercentageString() {
        if (this.getPercentage() == null) {
            return null;
        } else {
            int percentageValue = (int)(Math.round(this.getPercentage().doubleValue() * 100d));
            if (percentageValue <= 0 && this.getPercentage().doubleValue() > 0) {
                return "< 1";
            } else {
                return String.valueOf(percentageValue);
            }
        }
    }

    public Number getPercentage() {
        return this.percentage;
    }
    void setPercentage(Number percentage) {
        this.percentage = percentage;
    }

}
