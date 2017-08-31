package de.perdian.apps.flighttracker.business.modules.overview.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class StatisticsTopItem implements Serializable {

    static final long serialVersionUID = 1L;

    private String title = null;
    private String description = null;
    private Map<String, Object> context = null;
    private Number value = null;
    private Number percentage = null;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getTitle()).append(" = ").append(this.getValue());
        if (this.getPercentage() != null) {
            result.append(" (").append(this.getPercentage().intValue()).append("%)");
        }
        if (!StringUtils.isEmpty(this.getDescription())) {
            result.append(" [").append(this.getDescription()).append("]");
        }
        return result.toString();
    }

    public static class ValueComparator implements Comparator<StatisticsTopItem> {

        @Override
        public int compare(StatisticsTopItem o1, StatisticsTopItem o2) {
            return Objects.compare(o1.getValue(), o2.getValue(), (v1, v2) -> v1.intValue() == v2.intValue() ? 0 : v1.intValue() < v2.intValue() ? -1 : 1);
        }

    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
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
    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public Number getValue() {
        return this.value;
    }
    public void setValue(Number value) {
        this.value = value;
    }

    public Number getPercentage() {
        return this.percentage;
    }
    public void setPercentage(Number percentage) {
        this.percentage = percentage;
    }

}
