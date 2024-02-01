package de.perdian.flightlog.modules.overview;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Objects;

public class OverviewQueryValues {

    private List<OverviewQueryValuesItem> years = null;
    private List<OverviewQueryValuesItem> airlines = null;
    private List<OverviewQueryValuesItem> airports = null;
    private List<OverviewQueryValuesItem> aircraftTypes = null;
    private List<OverviewQueryValuesItem> cabinClasses = null;
    private List<OverviewQueryValuesItem> flightReasons = null;
    private List<OverviewQueryValuesItem> flightDistances = null;
    private List<OverviewQueryValuesItem> flightTypes = null;

    public List<OverviewQueryValuesItem> getYears() {
        return this.years;
    }
    public void setYears(List<OverviewQueryValuesItem> years) {
        this.years = years;
    }

    public List<OverviewQueryValuesItem> getAirlines() {
        return this.airlines;
    }
    public void setAirlines(List<OverviewQueryValuesItem> airlines) {
        this.airlines = airlines;
    }

    public List<OverviewQueryValuesItem> getAirports() {
        return this.airports;
    }
    public void setAirports(List<OverviewQueryValuesItem> airports) {
        this.airports = airports;
    }

    public List<OverviewQueryValuesItem> getAircraftTypes() {
        return this.aircraftTypes;
    }
    public void setAircraftTypes(List<OverviewQueryValuesItem> aircraftTypes) {
        this.aircraftTypes = aircraftTypes;
    }

    public List<OverviewQueryValuesItem> getCabinClasses() {
        return this.cabinClasses;
    }
    public void setCabinClasses(List<OverviewQueryValuesItem> cabinClasses) {
        this.cabinClasses = cabinClasses;
    }

    public List<OverviewQueryValuesItem> getFlightReasons() {
        return this.flightReasons;
    }
    public void setFlightReasons(List<OverviewQueryValuesItem> flightReasons) {
        this.flightReasons = flightReasons;
    }

    public List<OverviewQueryValuesItem> getFlightDistances() {
        return this.flightDistances;
    }
    public void setFlightDistances(List<OverviewQueryValuesItem> flightDistances) {
        this.flightDistances = flightDistances;
    }

    public List<OverviewQueryValuesItem> getFlightTypes() {
        return this.flightTypes;
    }
    public void setFlightTypes(List<OverviewQueryValuesItem> flightTypes) {
        this.flightTypes = flightTypes;
    }

    public static class OverviewQueryValuesItem {

        private String text = null;
        private String textKey = null;
        private String value = null;

        public static OverviewQueryValuesItem forText(String text) {
            return OverviewQueryValuesItem.forText(text, text);
        }

        public static OverviewQueryValuesItem forText(String text, String value) {
            OverviewQueryValuesItem item = new OverviewQueryValuesItem();
            item.setText(text);
            item.setValue(value);
            return item;
        }

        public static OverviewQueryValuesItem forTextKey(String textKey, String value) {
            OverviewQueryValuesItem item = new OverviewQueryValuesItem();
            item.setTextKey(textKey);
            item.setValue(value);
            return item;
        }

        public static int compareByText(OverviewQueryValuesItem i1, OverviewQueryValuesItem i2) {
            return String.CASE_INSENSITIVE_ORDER.compare(i1.getText(), i2.getText());
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

        public String getText() {
            return this.text;
        }
        void setText(String text) {
            this.text = text;
        }

        public String getTextKey() {
            return this.textKey;
        }
        void setTextKey(String textKey) {
            this.textKey = textKey;
        }

        public String getValue() {
            return this.value;
        }
        void setValue(String value) {
            this.value = value;
        }

    }

}
