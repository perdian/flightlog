package de.perdian.apps.flighttracker.modules.overview.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class OverviewItem implements Serializable {

    static final long serialVersionUID = 1L;

    private OverviewItemString title = null;
    private OverviewItemString description = null;
    private Map<String, Object> context = null;
    private Number value = null;
    private Number percentage = null;

    public OverviewItem(OverviewItemString title, OverviewItemString description, Number value, Number percentage) {
        this.setTitle(title);
        this.setDescription(description);
        this.setValue(value);
        this.setPercentage(percentage);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getTitle()).append(" = ").append(this.getValue());
        if (this.getPercentage() != null) {
            result.append(" (").append(this.getPercentage().intValue()).append("%)");
        }
        result.append(" [").append(this.getDescription()).append("]");
        return result.toString();
    }

    public static class ValueComparator implements Comparator<OverviewItem> {

        @Override
        public int compare(OverviewItem o1, OverviewItem o2) {
            return Objects.compare(o1.getValue(), o2.getValue(), (v1, v2) -> v1.intValue() == v2.intValue() ? 0 : v1.intValue() < v2.intValue() ? -1 : 1);
        }

    }

    public OverviewItemString getTitle() {
        return this.title;
    }
    private void setTitle(OverviewItemString title) {
        this.title = title;
    }

    public OverviewItemString getDescription() {
        return this.description;
    }
    private void setDescription(OverviewItemString description) {
        this.description = description;
    }

    public void addToContext(String key, Object value) {
        if (this.getContext() == null) {
            this.setContext(new LinkedHashMap<>());
        }
        this.getContext().put(key, value);
    }
    public Map<String, Object> getContext() {
        return this.context;
    }
    private void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public Number getValue() {
        return this.value;
    }
    private void setValue(Number value) {
        this.value = value;
    }

    public Number getPercentage() {
        return this.percentage;
    }
    private void setPercentage(Number percentage) {
        this.percentage = percentage;
    }

}
