package de.perdian.flightlog.modules.airlines.persistence;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AirlineEntity implements Serializable {

    static final long serialVersionUID = 1L;

    private String name = null;
    private String code = null;
    private String countryCode = null;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[name=").append(this.getName());
        result.append(",code=").append(this.getCode());
        result.append(",countryCode=").append(this.getCountryCode());
        return result.append("]").toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof AirlineEntity) {
            EqualsBuilder equalsBuilder = new EqualsBuilder();
            equalsBuilder.append(this.getCode(), ((AirlineEntity)that).getCode());
            equalsBuilder.append(this.getCountryCode(), ((AirlineEntity)that).getCountryCode());
            equalsBuilder.append(this.getName(), ((AirlineEntity)that).getName());
            return equalsBuilder.isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(this.getName());
        hashCodeBuilder.append(this.getCode());
        hashCodeBuilder.append(this.getCountryCode());
        return hashCodeBuilder.toHashCode();
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
