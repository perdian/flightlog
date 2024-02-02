package de.perdian.flightlog.modules.overview;

import java.util.List;

public class OverviewStatistics {

    private List<OverviewStatisticsItem> flightTotals = null;
    private List<OverviewStatisticsItem> flightsByDistance = null;
    private List<OverviewStatisticsItem> otherStatistics = null;

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

    public List<OverviewStatisticsItem> getOtherStatistics() {
        return this.otherStatistics;
    }
    public void setOtherStatistics(List<OverviewStatisticsItem> otherStatistics) {
        this.otherStatistics = otherStatistics;
    }

}
