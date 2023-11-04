package de.perdian.flightlog.modules.airports.persistence;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
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
