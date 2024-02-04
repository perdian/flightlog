package de.perdian.flightlog.modules.airports.model;

import java.time.*;
import java.util.Objects;

public class Airport {

    static final long serialVersionUID = 1L;

    private String code = null;
    private String countryCode = null;
    private String name = null;
    private String city = null;
    private Float longitude = null;
    private Float latitude = null;
    private ZoneId timezoneId = null;
    private ZoneOffset timezoneOffset = null;
    private String type = null;

    public Instant computeInstant(LocalDate localDate, LocalTime localTime) {
        ZonedDateTime zonedDateTime = this.computeZonedDateTime(localDate, localTime);
        return zonedDateTime == null ? null : zonedDateTime.toInstant();
    }

    public ZonedDateTime computeZonedDateTime(LocalDate localDate, LocalTime localTime) {
        if (localDate == null || localTime == null || this.getTimezoneId() == null) {
            return null;
        } else {
            return localTime.atDate(localDate).atZone(this.getTimezoneId());
        }
    }

    @Override
    public int hashCode() {
        return this.getCode() == null ? 0 : this.getCode().hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof Airport thatAirport) {
            return Objects.equals(this.getCode(), thatAirport.getCode());
        } else {
            return false;
        }
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

    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
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

    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
