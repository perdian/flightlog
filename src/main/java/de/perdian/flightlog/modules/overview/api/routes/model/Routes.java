package de.perdian.flightlog.modules.overview.api.routes.model;

import java.util.List;

public class Routes {

    private List<RoutesItem> items = null;
    private List<RoutesPoint> uniqueAirports = null;

    public List<RoutesItem> getItems() {
        return this.items;
    }
    public void setItems(List<RoutesItem> items) {
        this.items = items;
    }

    public List<RoutesPoint> getUniqueAirports() {
        return this.uniqueAirports;
    }
    public void setUniqueAirports(List<RoutesPoint> uniqueAirports) {
        this.uniqueAirports = uniqueAirports;
    }

}
