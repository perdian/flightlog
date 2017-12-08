package de.perdian.apps.flighttracker.modules.airlines.persistence;

import java.io.Serializable;

public class AirlineEntity implements Serializable {

    static final long serialVersionUID = 1L;

    private String name = null;
    private String alias = null;
    private String iataCode = null;
    private String icaoCode = null;
    private String callsign = null;
    private String countryCode = null;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[name=").append(this.getName());
        result.append(",alias=").append(this.getAlias());
        result.append(",iataCode=").append(this.getIataCode());
        result.append(",icaoCode=").append(this.getIcaoCode());
        result.append(",callsign=").append(this.getCallsign());
        result.append(",countryCode=").append(this.getCountryCode());
        return result.append("]").toString();
    }

    public String getName() {
        return this.name;
    }
    void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return this.alias;
    }
    void setAlias(String alias) {
        this.alias = alias;
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

    public String getCallsign() {
        return this.callsign;
    }
    void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getCountryCode() {
        return this.countryCode;
    }
    void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


}
