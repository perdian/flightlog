package de.perdian.apps.flighttracker.business.modules.flights.model;

import java.io.Serializable;

public class AirlineBean implements Serializable {

    static final long serialVersionUID = 1L;

    private String code = null;
    private String name = null;

    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
