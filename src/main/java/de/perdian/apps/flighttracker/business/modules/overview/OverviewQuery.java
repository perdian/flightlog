package de.perdian.apps.flighttracker.business.modules.overview;

import java.io.Serializable;

public class OverviewQuery implements Serializable {

    static final long serialVersionUID = 1L;

    private Integer year = null;

    public Integer getYear() {
        return this.year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }

}
