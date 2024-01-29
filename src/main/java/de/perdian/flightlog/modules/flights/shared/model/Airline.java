package de.perdian.flightlog.modules.flights.shared.model;

import de.perdian.flightlog.modules.airlines.persistence.AirlineEntity;

public class Airline {

    private String name = null;
    private String code = null;
    private String countryCode = null;

    public Airline() {
    }

    public Airline(AirlineEntity airlineEntity) {
        this.setCode(airlineEntity.getCode());
        this.setCountryCode(airlineEntity.getCountryCode());
        this.setName(airlineEntity.getName());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[code=").append(this.getCode());
        result.append(",countryCode=").append(this.getCountryCode());
        result.append(",name=").append(this.getName());
        return result.append("]").toString();
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
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

}
