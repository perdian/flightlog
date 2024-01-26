package de.perdian.flightlog.modules.flights.service.model;

import de.perdian.flightlog.modules.airports.persistence.AirportEntity;

import java.time.ZoneId;
import java.time.ZoneOffset;

public class Airport {

    static final long serialVersionUID = 1L;

    private String code = null;
    private String countryCode = null;
    private String name = null;
    private Float longitude = null;
    private Float latitude = null;
    private ZoneId timezoneId = null;
    private ZoneOffset timezoneOffset = null;

    public Airport() {
    }

    public Airport(AirportEntity airportEntity) {
        this.setCode(airportEntity.getIataCode());
        this.setCountryCode(airportEntity.getCountryCode());
        this.setName(airportEntity.getName());
        this.setLongitude(airportEntity.getLongitude());
        this.setLatitude(airportEntity.getLatitude());
        this.setTimezoneId(airportEntity.getTimezoneId());
        this.setTimezoneOffset(airportEntity.getTimezoneOffset());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[code=").append(this.getCode());
        result.append(",countryCode=").append(this.getCountryCode());
        result.append(",name=").append(this.getName());
        result.append(",longitude=").append(this.getLatitude());
        result.append(",latitude=").append(this.getLongitude());
        result.append(",timezoneId=").append(this.getTimezoneId());
        result.append(",timezoneOffset=").append(this.getTimezoneOffset());
        return result.append("]").toString();
    }

    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryCode() {
        return this.countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
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

    public ZoneId getTimezoneId() {
        return this.timezoneId;
    }
    public void setTimezoneId(ZoneId timezoneId) {
        this.timezoneId = timezoneId;
    }

    public ZoneOffset getTimezoneOffset() {
        return this.timezoneOffset;
    }
    public void setTimezoneOffset(ZoneOffset timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

}
