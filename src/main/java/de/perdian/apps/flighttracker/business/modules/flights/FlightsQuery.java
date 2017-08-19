package de.perdian.apps.flighttracker.business.modules.flights;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FlightsQuery implements Serializable {

    static final long serialVersionUID = 1L;

    private Integer page = null;
    private Integer pageSize = null;
    private Collection<Long> restrictIdentifiers = null;
    private Collection<String> restrictFlightNumbers = null;
    private Collection<String> restrictDepartureAirportCodes = null;
    private LocalDate minimumDepartureDateLocal = null;
    private LocalDate maximumDepartureDateLocal = null;
    private Collection<String> restrictArrivalAirportCodes = null;
    private LocalDate minimumArrivalDateLocal = null;
    private LocalDate maximumArrivalDateLocal = null;

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        toStringBuilder.append("restrictIdentifiers", this.getRestrictIdentifiers());
        toStringBuilder.append("restrictFlightNumbers", this.getRestrictDepartureAirportCodes());
        toStringBuilder.append("restrictDepartureAirportCodes", this.getRestrictDepartureAirportCodes());
        toStringBuilder.append("minimumDepartureDateLocal", this.getMinimumDepartureDateLocal());
        toStringBuilder.append("maximumDepartureDateLocal", this.getMaximumDepartureDateLocal());
        toStringBuilder.append("restrictArrivalAirportCodes", this.getRestrictArrivalAirportCodes());
        toStringBuilder.append("minimumArrivalDateLocal", this.getMinimumArrivalDateLocal());
        toStringBuilder.append("maximumArrivalDateLocal", this.getMaximumArrivalDateLocal());
        return toStringBuilder.toString();
    }

    public Integer getPage() {
        return this.page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Collection<Long> getRestrictIdentifiers() {
        return this.restrictIdentifiers;
    }
    public void setRestrictIdentifiers(Collection<Long> restrictIdentifiers) {
        this.restrictIdentifiers = restrictIdentifiers;
    }

    public Collection<String> getRestrictFlightNumbers() {
        return this.restrictFlightNumbers;
    }
    public void setRestrictFlightNumbers(Collection<String> restrictFlightNumbers) {
        this.restrictFlightNumbers = restrictFlightNumbers;
    }

    public Collection<String> getRestrictDepartureAirportCodes() {
        return this.restrictDepartureAirportCodes;
    }
    public void setRestrictDepartureAirportCodes(Collection<String> restrictDepartureAirportCodes) {
        this.restrictDepartureAirportCodes = restrictDepartureAirportCodes;
    }

    public LocalDate getMinimumDepartureDateLocal() {
        return this.minimumDepartureDateLocal;
    }
    public void setMinimumDepartureDateLocal(LocalDate minimumDepartureDateLocal) {
        this.minimumDepartureDateLocal = minimumDepartureDateLocal;
    }

    public LocalDate getMaximumDepartureDateLocal() {
        return this.maximumDepartureDateLocal;
    }
    public void setMaximumDepartureDateLocal(LocalDate maximumDepartureDateLocal) {
        this.maximumDepartureDateLocal = maximumDepartureDateLocal;
    }

    public Collection<String> getRestrictArrivalAirportCodes() {
        return this.restrictArrivalAirportCodes;
    }
    public void setRestrictArrivalAirportCodes(Collection<String> restrictArrivalAirportCodes) {
        this.restrictArrivalAirportCodes = restrictArrivalAirportCodes;
    }

    public LocalDate getMinimumArrivalDateLocal() {
        return this.minimumArrivalDateLocal;
    }
    public void setMinimumArrivalDateLocal(LocalDate minimumArrivalDateLocal) {
        this.minimumArrivalDateLocal = minimumArrivalDateLocal;
    }

    public LocalDate getMaximumArrivalDateLocal() {
        return this.maximumArrivalDateLocal;
    }
    public void setMaximumArrivalDateLocal(LocalDate maximumArrivalDateLocal) {
        this.maximumArrivalDateLocal = maximumArrivalDateLocal;
    }


}
