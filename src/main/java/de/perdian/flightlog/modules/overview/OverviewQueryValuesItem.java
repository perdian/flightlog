package de.perdian.flightlog.modules.overview;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

public class OverviewQueryValuesItem implements Comparable<OverviewQueryValuesItem> {

    private String value = null;
    private OverviewString title = null;

    public OverviewQueryValuesItem(String value) {
        this(value, OverviewString.forValue(value));
    }

    public OverviewQueryValuesItem(String value, OverviewString title) {
        this.setValue(value);
        this.setTitle(Objects.requireNonNull(title));
    }

    public OverviewQueryValuesItem(Enum<?> enumValue) {
        this.setValue(enumValue.name());
        this.setTitle(OverviewString.forEnum(enumValue));
    }

    @Override
    public int compareTo(OverviewQueryValuesItem otherItem) {
        return this.getTitle().compareTo(otherItem.getTitle());
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof OverviewQueryValuesItem thatItem) {
            return Objects.equals(this.getValue(), thatItem.getValue());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public String getValue() {
        return this.value;
    }
    void setValue(String value) {
        this.value = value;
    }

    public OverviewString getTitle() {
        return this.title;
    }
    void setTitle(OverviewString title) {
        this.title = title;
    }

}
