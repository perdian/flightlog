package de.perdian.apps.flighttracker.modules.airlines.web;

import java.io.Serializable;

public class Airline implements Serializable {

    static final long serialVersionUID = 1L;

    private String code = null;
    private String name = null;

    public String getCode() {
        return this.code;
    }
    void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }
    void setName(String name) {
        this.name = name;
    }

}
