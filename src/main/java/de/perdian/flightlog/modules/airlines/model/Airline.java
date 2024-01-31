package de.perdian.flightlog.modules.airlines.model;

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

    public Airline clone() {
        try {
            return (Airline)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cannot clone class: " + this.getClass().getName(), e);
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
