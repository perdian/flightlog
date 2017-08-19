package de.perdian.apps.flighttracker.web.modules.overview;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class OverviewQueryHelper implements Serializable {

    static final long serialVersionUID = 1L;

    private Set<Integer> availableYears = Collections.emptySet();

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        toStringBuilder.append("availableYears", this.getAvailableYears());
        return toStringBuilder.toString();
    }

    public Set<Integer> getAvailableYears() {
        return this.availableYears;
    }
    public void setAvailableYears(SortedSet<Integer> availableYears) {
        this.availableYears = availableYears;
    }

}
