package de.perdian.apps.flighttracker.web.modules.overview;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class OverviewQuery implements Serializable {

    static final long serialVersionUID = 1L;

    private Integer year = null;
    private String airlineCode = null;
    private String airportCode = null;
    private String aircraftType = null;
    private String cabinClass = null;
    private String flightReason = null;

    public boolean isFilterActive() {
        boolean filterActive = false;
        filterActive |= this.getYear() != null && !Integer.valueOf(0).equals(this.getYear());
        filterActive |= !StringUtils.isEmpty(this.getAirlineCode()) && !".".equalsIgnoreCase(this.getAirlineCode());
        filterActive |= !StringUtils.isEmpty(this.getAirportCode()) && !".".equalsIgnoreCase(this.getAirportCode());
        filterActive |= !StringUtils.isEmpty(this.getAircraftType()) && !".".equalsIgnoreCase(this.getAircraftType());
        filterActive |= !StringUtils.isEmpty(this.getCabinClass()) && !".".equalsIgnoreCase(this.getCabinClass());
        filterActive |= !StringUtils.isEmpty(this.getFlightReason()) && !".".equalsIgnoreCase(this.getFlightReason());
        return filterActive;
    }

    public Integer getYear() {
        return this.year;
    }
    public void setYear(Integer year) {
        this.year = Integer.valueOf(0).equals(year) ? null : year;
    }

    public String getAirlineCode() {
        return this.airlineCode;
    }
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getAirportCode() {
        return this.airportCode;
    }
    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getAircraftType() {
        return this.aircraftType;
    }
    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getCabinClass() {
        return this.cabinClass;
    }
    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    public String getFlightReason() {
        return this.flightReason;
    }
    public void setFlightReason(String flightReason) {
        this.flightReason = flightReason;
    }

}
