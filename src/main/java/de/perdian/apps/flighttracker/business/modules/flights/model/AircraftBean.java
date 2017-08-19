package de.perdian.apps.flighttracker.business.modules.flights.model;

import java.io.Serializable;

public class AircraftBean implements Serializable {

    static final long serialVersionUID = 1L;

    private String type = null;
    private String registration = null;
    private String name = null;

    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getRegistration() {
        return this.registration;
    }
    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
