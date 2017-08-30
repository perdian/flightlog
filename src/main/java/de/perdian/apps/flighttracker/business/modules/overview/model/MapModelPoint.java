package de.perdian.apps.flighttracker.business.modules.overview.model;

import java.io.Serializable;

public class MapModelPoint implements Serializable {

    static final long serialVersionUID = 1L;

    private Float longitude = null;
    private Float latitude = null;

    public MapModelPoint(Float latitude, Float longitude) {
        this.setLatitude(latitude);
        this.setLongitude(longitude);
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
