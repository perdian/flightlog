package de.perdian.flightlog.modules.overview;

import java.util.List;

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

}
