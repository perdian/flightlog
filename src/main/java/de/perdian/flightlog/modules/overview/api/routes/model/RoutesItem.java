package de.perdian.flightlog.modules.overview.api.routes.model;

public class RoutesItem {

    private RoutesPoint departurePoint = null;
    private RoutesPoint arrivalPoint = null;
    private int counter = 0;

    public RoutesPoint getDeparturePoint() {
        return this.departurePoint;
    }
    public void setDeparturePoint(RoutesPoint departurePoint) {
        this.departurePoint = departurePoint;
    }

    public RoutesPoint getArrivalPoint() {
        return this.arrivalPoint;
    }
    public void setArrivalPoint(RoutesPoint arrivalPoint) {
        this.arrivalPoint = arrivalPoint;
    }

    public int getWeight() {
        if (this.getCounter() <= 1) {
            return 1;
        } else if (this.getCounter() <= 5) {
            return 3;
        } else if (this.getCounter() <= 10) {
            return 5;
        } else if (this.getCounter() <= 20) {
            return 7;
        } else {
            return 9;
        }
    }

    public void incrementCounter() {
        this.setCounter(this.getCounter() + 1);
    }
    public int getCounter() {
        return this.counter;
    }
    public void setCounter(int weight) {
        this.counter = weight;
    }

}
