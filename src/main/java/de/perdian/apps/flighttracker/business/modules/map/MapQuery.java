package de.perdian.apps.flighttracker.business.modules.map;

import java.io.Serializable;

public class MapQuery implements Serializable {

    static final long serialVersionUID = 1L;

    private Integer year = null;

    public Integer getYear() {
        return this.year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }

}
