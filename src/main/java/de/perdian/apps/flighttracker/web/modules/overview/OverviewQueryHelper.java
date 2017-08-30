package de.perdian.apps.flighttracker.web.modules.overview;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class OverviewQueryHelper implements Serializable {

    static final long serialVersionUID = 1L;

    private Set<Integer> availableYears = Collections.emptySet();
    private List<OverviewQueryHelperItem> availableAirlineCodes = null;
    private List<OverviewQueryHelperItem> availableAirportCodes = null;
    private List<OverviewQueryHelperItem> availableAircraftTypes = null;
    private List<OverviewQueryHelperItem> availableCabinClasses = null;
    private List<OverviewQueryHelperItem> availableFlightReasons = null;

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        toStringBuilder.append("availableYears", this.getAvailableYears());
        return toStringBuilder.toString();
    }

    public Set<Integer> getAvailableYears() {
        return this.availableYears;
    }
    public void setAvailableYears(Set<Integer> availableYears) {
        this.availableYears = availableYears;
    }

    public List<OverviewQueryHelperItem> getAvailableAirlineCodes() {
        return this.availableAirlineCodes;
    }
    public void setAvailableAirlineCodes(List<OverviewQueryHelperItem> availableAirlineCodes) {
        this.availableAirlineCodes = availableAirlineCodes;
    }

    public List<OverviewQueryHelperItem> getAvailableAirportCodes() {
        return this.availableAirportCodes;
    }
    public void setAvailableAirportCodes(List<OverviewQueryHelperItem> availableAirportCodes) {
        this.availableAirportCodes = availableAirportCodes;
    }

    public List<OverviewQueryHelperItem> getAvailableAircraftTypes() {
        return this.availableAircraftTypes;
    }
    public void setAvailableAircraftTypes(List<OverviewQueryHelperItem> availableAircraftTypes) {
        this.availableAircraftTypes = availableAircraftTypes;
    }

    public List<OverviewQueryHelperItem> getAvailableCabinClasses() {
        return this.availableCabinClasses;
    }
    public void setAvailableCabinClasses(List<OverviewQueryHelperItem> availableCabinClasses) {
        this.availableCabinClasses = availableCabinClasses;
    }

    public List<OverviewQueryHelperItem> getAvailableFlightReasons() {
        return this.availableFlightReasons;
    }
    public void setAvailableFlightReasons(List<OverviewQueryHelperItem> availableFlightReasons) {
        this.availableFlightReasons = availableFlightReasons;
    }

}
