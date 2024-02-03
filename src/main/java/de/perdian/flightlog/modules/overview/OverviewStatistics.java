package de.perdian.flightlog.modules.overview;

import java.util.List;

public class OverviewStatistics {

    private List<OverviewStatisticsItem> flightTotals = null;
    private List<OverviewStatisticsItem> flightsByDistance = null;
    private List<OverviewStatisticsItem> flightsByCabinClasses = null;
    private List<OverviewStatisticsItem> flightsByFlightReasons = null;
    private List<OverviewStatisticsItem> flightsBySeatTypes = null;
    private List<OverviewStatisticsItem> otherStatistics = null;
    private List<OverviewStatisticsItem> topAirports = null;
    private List<OverviewStatisticsItem> topAirlines = null;
    private List<OverviewStatisticsItem> topRoutes = null;
    private List<OverviewStatisticsItem> topAircraftTypes = null;

    public List<OverviewStatisticsItem> getFlightTotals() {
        return this.flightTotals;
    }
    public void setFlightTotals(List<OverviewStatisticsItem> flightTotals) {
        this.flightTotals = flightTotals;
    }

    public List<OverviewStatisticsItem> getFlightsByDistance() {
        return this.flightsByDistance;
    }
    public void setFlightsByDistance(List<OverviewStatisticsItem> flightsByDistance) {
        this.flightsByDistance = flightsByDistance;
    }

    public List<OverviewStatisticsItem> getFlightsByCabinClasses() {
        return this.flightsByCabinClasses;
    }
    public void setFlightsByCabinClasses(List<OverviewStatisticsItem> flightsByCabinClasses) {
        this.flightsByCabinClasses = flightsByCabinClasses;
    }

    public List<OverviewStatisticsItem> getFlightsByFlightReasons() {
        return this.flightsByFlightReasons;
    }
    public void setFlightsByFlightReasons(List<OverviewStatisticsItem> flightsByFlightReasons) {
        this.flightsByFlightReasons = flightsByFlightReasons;
    }

    public List<OverviewStatisticsItem> getFlightsBySeatTypes() {
        return this.flightsBySeatTypes;
    }
    public void setFlightsBySeatTypes(List<OverviewStatisticsItem> flightsBySeatTypes) {
        this.flightsBySeatTypes = flightsBySeatTypes;
    }

    public List<OverviewStatisticsItem> getOtherStatistics() {
        return this.otherStatistics;
    }
    public void setOtherStatistics(List<OverviewStatisticsItem> otherStatistics) {
        this.otherStatistics = otherStatistics;
    }

}
