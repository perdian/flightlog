package de.perdian.flightlog.modules.airlines.model;

import java.util.Objects;

public class Airline implements Cloneable {

    private String name = null;
    private String code = null;
    private String countryCode = null;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[code=").append(this.getCode());
        result.append(",countryCode=").append(this.getCountryCode());
        result.append(",name=").append(this.getName());
        return result.append("]").toString();
    }

    @Override
    public Airline clone() {
        try {
            return (Airline)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cannot clone class: " + this.getClass().getName(), e);
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
        } else if (that instanceof Airline thatAirline) {
            return Objects.equals(this.getCode(), thatAirline.getCode());
        } else {
            return false;
        }
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
