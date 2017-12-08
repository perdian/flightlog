package de.perdian.apps.flighttracker.modules.airports.persistence;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class AirportEntity implements Serializable {

    static final long serialVersionUID = 1L;

    private String name = null;
    private String city = null;
    private String countryCode = null;
    private String iataCode = null;
    private String icaoCode = null;
    private Float latitude = null;
    private Float longitude = null;
    private ZoneOffset timezoneOffset = null;
    private ZoneId timezoneId = null;
    private String type = null;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[name=").append(this.getName());
        result.append(",city=").append(this.getCity());
        result.append(",countryCode=").append(this.getCountryCode());
        result.append(",iataCode=").append(this.getIataCode());
        result.append(",icaoCode=").append(this.getIcaoCode());
        result.append(",latitude=").append(this.getLatitude());
        result.append(",longitude=").append(this.getLongitude());
        result.append(",timezoneOffset=").append(this.getTimezoneOffset());
        result.append(",timezoneId=").append(this.getTimezoneId());
        result.append(",type=").append(this.getType());
        return result.append("]").toString();
    }

    public String getName() {
        return this.name;
    }
    void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return this.city;
    }
    void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return this.countryCode;
    }
    void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIataCode() {
        return this.iataCode;
    }
    void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getIcaoCode() {
        return this.icaoCode;
    }
    void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }

    public Float getLatitude() {
        return this.latitude;
    }
    void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return this.longitude;
    }
    void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public ZoneOffset getTimezoneOffset() {
        return this.timezoneOffset;
    }
    void setTimezoneOffset(ZoneOffset timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public ZoneId getTimezoneId() {
        return this.timezoneId;
    }
    void setTimezoneId(ZoneId timezoneId) {
        this.timezoneId = timezoneId;
    }

    public String getType() {
        return this.type;
    }
    void setType(String type) {
        this.type = type;
    }

}
