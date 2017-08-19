package de.perdian.apps.flighttracker.business.modules.overview.model;

import java.io.Serializable;

public class OverviewBean implements Serializable {

    static final long serialVersionUID = 1L;

    private StatisticsBean statistics = null;

    public StatisticsBean getStatistics() {
        return this.statistics;
    }
    public void setStatistics(StatisticsBean statistics) {
        this.statistics = statistics;
    }

}
