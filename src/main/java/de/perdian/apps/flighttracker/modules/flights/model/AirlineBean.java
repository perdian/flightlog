package de.perdian.apps.flighttracker.modules.flights.model;

import java.io.Serializable;

public class AirlineBean implements Serializable {

    static final long serialVersionUID = 1L;

    private String code = null;
    private String name = null;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[code=").append(this.getCode());
        result.append(",name=").append(this.getName());
        return result.append("]").toString();
    }

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
