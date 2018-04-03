package de.perdian.flightlog.modules.overview.model;

import java.io.Serializable;
import java.util.List;

public class MapModel implements Serializable {

    static final long serialVersionUID = 1L;

    private List<MapModelRoute> routes = null;
    private List<MapModelAirport> airports = null;

    public List<MapModelRoute> getRoutes() {
        return this.routes;
    }
    public void setRoutes(List<MapModelRoute> routes) {
        this.routes = routes;
    }

    public List<MapModelAirport> getAirports() {
        return this.airports;
    }
    public void setAirports(List<MapModelAirport> airports) {
        this.airports = airports;
    }

}
