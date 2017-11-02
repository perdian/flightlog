package de.perdian.apps.flighttracker.modules.airports.web;

import java.io.Serializable;

public class Airport implements Serializable {

    static final long serialVersionUID = 1L;

    private String code = null;
    private String countryCode = null;
    private String name = null;
    private Float longitude = null;
    private Float latitude = null;
    private String timezoneId = null;

    public String getCode() {
        return this.code;
    }
    void setCode(String code) {
        this.code = code;
    }

    public String getCountryCode() {
        return this.countryCode;
    }
    void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public String getTimezoneId() {
        return this.timezoneId;
    }
    void setTimezoneId(String timezoneId) {
        this.timezoneId = timezoneId;
    }

}
