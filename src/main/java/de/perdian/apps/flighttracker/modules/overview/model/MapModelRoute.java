package de.perdian.apps.flighttracker.modules.overview.model;

import java.io.Serializable;

public class MapModelRoute implements Serializable {

    static final long serialVersionUID = 1L;

    private String route = null;
    private MapModelPoint startPoint = null;
    private MapModelPoint endPoint = null;
    private int weight = 1;

    public String getRoute() {
        return this.route;
    }
    public void setRoute(String route) {
        this.route = route;
    }

    public MapModelPoint getStartPoint() {
        return this.startPoint;
    }
    public void setStartPoint(MapModelPoint startPoint) {
        this.startPoint = startPoint;
    }

    public MapModelPoint getEndPoint() {
        return this.endPoint;
    }
    public void setEndPoint(MapModelPoint endPoint) {
        this.endPoint = endPoint;
    }

    public int getWeight() {
        return this.weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

}
