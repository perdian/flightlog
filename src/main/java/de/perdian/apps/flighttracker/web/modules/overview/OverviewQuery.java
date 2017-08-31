package de.perdian.apps.flighttracker.web.modules.overview;

import java.io.Serializable;
import java.util.List;

public class OverviewQuery implements Serializable {

    static final long serialVersionUID = 1L;

    private List<String> year = null;
    private List<String> airlineCode = null;
    private List<String> airportCode = null;
    private List<String> aircraftType = null;
    private List<String> cabinClass = null;
    private List<String> flightReason = null;

    public boolean isFilterActive() {
        boolean filterActive = false;
        filterActive |= this.getYear() != null && !this.getYear().isEmpty();
        filterActive |= this.getAirlineCode() != null && !this.getAirlineCode().isEmpty();
        filterActive |= this.getAirportCode() != null && !this.getAirportCode().isEmpty();
        filterActive |= this.getAircraftType() != null && !this.getAircraftType().isEmpty();
        filterActive |= this.getCabinClass() != null && !this.getCabinClass().isEmpty();
        filterActive |= this.getFlightReason() != null && !this.getFlightReason().isEmpty();
        return filterActive;
    }

    public List<String> getYear() {
        return this.year;
    }
    public void setYear(List<String> year) {
        this.year = year;
    }

    public List<String> getAirlineCode() {
        return this.airlineCode;
    }
    public void setAirlineCode(List<String> airlineCode) {
        this.airlineCode = airlineCode;
    }

    public List<String> getAirportCode() {
        return this.airportCode;
    }
    public void setAirportCode(List<String> airportCode) {
        this.airportCode = airportCode;
    }

    public List<String> getAircraftType() {
        return this.aircraftType;
    }
    public void setAircraftType(List<String> aircraftType) {
        this.aircraftType = aircraftType;
    }

    public List<String> getCabinClass() {
        return this.cabinClass;
    }
    public void setCabinClass(List<String> cabinClass) {
        this.cabinClass = cabinClass;
    }

    public List<String> getFlightReason() {
        return this.flightReason;
    }
    public void setFlightReason(List<String> flightReason) {
        this.flightReason = flightReason;
    }

}
