package de.perdian.flightlog.modules.overview.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class OverviewItemString implements Serializable {

    static final long serialVersionUID = 1L;

    private String key = null;
    private String text = null;

    private OverviewItemString(String key, String text) {
        this.setKey(key);
        this.setText(text);
    }

    public static OverviewItemString forKey(String key) {
        return new OverviewItemString(key, null);
    }

    public static OverviewItemString forText(String text) {
        return new OverviewItemString(null, text);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder().append("[");
        result.append(this.getKey() == null ? "" : "key=" + this.getKey());
        result.append(this.getText() == null ? "" : "text=" + this.getText());
        return result.append("]").toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof OverviewItemString) {
            OverviewItemString thatString = (OverviewItemString)that;
            EqualsBuilder equalsBuilder = new EqualsBuilder();
            equalsBuilder.append(this.getKey(), thatString.getKey());
            equalsBuilder.append(this.getText(), thatString.getText());
            return equalsBuilder.isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(this.getKey());
        hashCodeBuilder.append(this.getText());
        return hashCodeBuilder.toHashCode();
    }

    public boolean isAvailable() {
        return !StringUtils.isEmpty(this.getKey()) || !StringUtils.isEmpty(this.getText());
    }

    public String getKey() {
        return this.key;
    }
    private void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return this.text;
    }
    private void setText(String text) {
        this.text = text;
    }

}
