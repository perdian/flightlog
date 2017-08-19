package de.perdian.apps.flighttracker.web.modules.airports;

import java.io.Serializable;
import java.time.ZoneId;

public class Airport implements Serializable {

    static final long serialVersionUID = 1L;

    private String code = null;
    private String name = null;
    private Float longitude = null;
    private Float latitude = null;
    private ZoneId timezoneId = null;

    public String getCode() {
        return this.code;
    }
    void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }
    void setName(String name) {
        this.name = name;
    }

    public Float getLongitude() {
        return this.longitude;
    }
    void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return this.latitude;
    }
    void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public ZoneId getTimezoneId() {
        return this.timezoneId;
    }
    void setTimezoneId(ZoneId timezoneId) {
        this.timezoneId = timezoneId;
    }

}
