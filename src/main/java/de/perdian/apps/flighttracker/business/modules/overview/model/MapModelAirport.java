package de.perdian.apps.flighttracker.business.modules.overview.model;

import java.io.Serializable;

public class MapModelAirport implements Serializable {

    static final long serialVersionUID = 1L;

    private String code = null;
    private MapModelPoint point = null;

    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public MapModelPoint getPoint() {
        return this.point;
    }
    public void setPoint(MapModelPoint point) {
        this.point = point;
    }

}
