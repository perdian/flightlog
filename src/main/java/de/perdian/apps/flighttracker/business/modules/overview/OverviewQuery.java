package de.perdian.apps.flighttracker.business.modules.overview;

import java.io.Serializable;
import java.util.Collection;

import de.perdian.apps.flighttracker.persistence.entities.UserEntity;
import de.perdian.apps.flighttracker.support.types.CabinClass;

public class OverviewQuery implements Serializable {

    static final long serialVersionUID = 1L;

    private Integer year = null;
    private String airlineCode = null;
    private String airportCode = null;
    private String aircraftType = null;
    private CabinClass cabinClass = null;
    private Collection<UserEntity> restrictUsers = null;

    public Integer getYear() {
        return this.year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }

    public Collection<UserEntity> getRestrictUsers() {
        return this.restrictUsers;
    }
    public void setRestrictUsers(Collection<UserEntity> restrictUsers) {
        this.restrictUsers = restrictUsers;
    }

    public String getAirlineCode() {
        return this.airlineCode;
    }
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getAirportCode() {
        return this.airportCode;
    }
    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getAircraftType() {
        return this.aircraftType;
    }
    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public CabinClass getCabinClass() {
        return this.cabinClass;
    }
    public void setCabinClass(CabinClass cabinClass) {
        this.cabinClass = cabinClass;
    }

}
