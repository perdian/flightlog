package de.perdian.flightlog.modules.overview.api.routes.model;

public class RoutesPoint {

    private String airportCode = null;
    private Float longitude = null;
    private Float latitude = null;

    public String getAirportCode() {
        return this.airportCode;
    }
    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public Float getLongitude() {
        return this.longitude;
    }
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return this.latitude;
    }
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

}
