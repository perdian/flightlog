package de.perdian.flightlog.modules.airlines.web;

import java.io.Serializable;

public class AirlineData implements Serializable {

    static final long serialVersionUID = 1L;

    private String code = null;
    private String name = null;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[code=").append(this.getCode());
        result.append(",name=").append(this.getName());
        return result.append("]").toString();
    }

    public String getCode() {
        return this.code;
    }
    void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }
    void setName(String name) {
        this.name = name;
    }

}
