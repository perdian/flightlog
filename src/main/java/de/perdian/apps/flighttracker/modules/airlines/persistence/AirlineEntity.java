package de.perdian.apps.flighttracker.modules.airlines.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "airline")
public class AirlineEntity implements Serializable {

    static final long serialVersionUID = 1L;

    private String code = null;
    private String name = null;
    private String countryCode = null;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[code=").append(this.getCode());
        result.append(",name=").append(this.getName());
        result.append(",countryCode=").append(this.getCountryCode());
        return result.append("]").toString();
    }

    @Id
    @Column(length = 2)
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 100)
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 2)
    public String getCountryCode() {
        return this.countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
