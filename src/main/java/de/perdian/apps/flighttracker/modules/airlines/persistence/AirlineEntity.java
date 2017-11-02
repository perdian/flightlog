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

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return this.alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIataCode() {
        return this.iataCode;
    }
    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getIcaoCode() {
        return this.icaoCode;
    }
    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }

    public String getCallsign() {
        return this.callsign;
    }
    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getCountryCode() {
        return this.countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


}
