package de.perdian.apps.flighttracker.persistence.entities;

import java.io.Serializable;

public class AircraftTypeEntity implements Serializable {

    static final long serialVersionUID = 1L;

    private String iataCode = null;
    private String icaoCode = null;
    private String title = null;

    public String getIataCode() {
        return this.iataCode;
    }
    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getIcaoCode() {
        return this.icaoCode;
    }
    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

}
