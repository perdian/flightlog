package de.perdian.apps.flighttracker.modules.airlines.web;

import java.io.Serializable;
import java.util.List;

public class AirlineEditorList implements Serializable {

    static final long serialVersionUID = 1L;

    private List<AirlineEditor> airlines = null;

    public List<AirlineEditor> getAirlines() {
        return this.airlines;
    }
    public void setAirlines(List<AirlineEditor> airlines) {
        this.airlines = airlines;
    }

}
