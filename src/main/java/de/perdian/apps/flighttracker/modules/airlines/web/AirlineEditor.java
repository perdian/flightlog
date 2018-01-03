package de.perdian.apps.flighttracker.modules.airlines.web;

import java.io.Serializable;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;

public class AirlineEditor implements Serializable {

    static final long serialVersionUID = 1L;

    private AirlineBean airlineBean = null;
    private boolean delete = false;

    public AirlineBean getAirlineBean() {
        return this.airlineBean;
    }
    public void setAirlineBean(AirlineBean airlineBean) {
        this.airlineBean = airlineBean;
    }

    public boolean isDelete() {
        return this.delete;
    }
    public void setDelete(boolean delete) {
        this.delete = delete;
    }

}
