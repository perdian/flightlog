package de.perdian.flightlog.modules.overview;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class OverviewStatisticsItem {

    private OverviewString title = null;
    private OverviewString description = null;
    private String iconCode = null;
    private Number value = null;
    private String valueFormat = "#,##0";
    private Number percentage = null;

    public OverviewStatisticsItem withTitle(OverviewString title) {
        this.setTitle(title);
        return this;
    }
    public OverviewString getTitle() {
        return this.title;
    }
    public void setTitle(OverviewString title) {
        this.title = title;
    }

    public OverviewStatisticsItem withDescription(OverviewString description) {
        this.setDescription(description);
        return this;
    }
    public OverviewString getDescription() {
        return this.description;
    }
    public void setDescription(OverviewString description) {
        this.description = description;
    }

    public OverviewStatisticsItem withIconCode(String iconCode) {
        this.setIconCode(iconCode);
        return this;
    }
    public String getIconCode() {
        return this.iconCode;
    }
    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public String getValueString() {
        return this.getValue() == null ? null : new DecimalFormat(this.getValueFormat(), new DecimalFormatSymbols(Locale.GERMANY)).format(this.getValue());
    }

    public OverviewStatisticsItem withValue(Number value) {
        this.setValue(value);
        return this;
    }
    public Number getValue() {
        return this.value;
    }
    public void setValue(Number value) {
        this.value = value;
    }

    public OverviewStatisticsItem withValueFormat(String valueFormat) {
        this.setValueFormat(valueFormat);
        return this;
    }
    public String getValueFormat() {
        return this.valueFormat;
    }
    public void setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
    }

    public OverviewStatisticsItem withPercentage(Number percentage) {
        this.setPercentage(percentage);
        return this;
    }
    public Number getPercentage() {
        return this.percentage;
    }
    public void setPercentage(Number percentage) {
        this.percentage = percentage;
    }

}
