package de.perdian.flightlog.modules.overview;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class OverviewString implements Comparable<OverviewString> {

    private String value = null;
    private String key = null;

    public static OverviewString forValue(String value) {
        OverviewString overviewString = new OverviewString();
        overviewString.setValue(value);
        return overviewString;
    }

    public static OverviewString forKey(String key) {
        OverviewString overviewString = new OverviewString();
        overviewString.setKey(key);
        return overviewString;
    }

    public static OverviewString forEnum(Enum<?> enumValue) {
        return OverviewString.forKey(Character.toLowerCase(enumValue.getClass().getSimpleName().charAt(0)) + enumValue.getClass().getSimpleName().substring(1) + "." + enumValue.name());
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    @Override
    public int compareTo(OverviewString otherString) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.getValue(), otherString.getValue());
    }

    public String getValue() {
        return this.value;
    }
    void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }
    void setKey(String key) {
        this.key = key;
    }

}
