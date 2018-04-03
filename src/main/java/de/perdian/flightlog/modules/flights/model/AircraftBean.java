package de.perdian.flightlog.modules.flights.model;

import java.io.Serializable;

public class AircraftBean implements Serializable {

    static final long serialVersionUID = 1L;

    private String type = null;
    private String registration = null;
    private String name = null;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[type=").append(this.getType());
        result.append(",registration=").append(this.getRegistration());
        result.append(",name=").append(this.getName());
        return result.append("]").toString();
    }

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
