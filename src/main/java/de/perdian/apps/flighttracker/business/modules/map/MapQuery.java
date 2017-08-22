package de.perdian.apps.flighttracker.business.modules.map;

import java.io.Serializable;
import java.util.Collection;

import de.perdian.apps.flighttracker.persistence.entities.UserEntity;

public class MapQuery implements Serializable {

    static final long serialVersionUID = 1L;

    private Integer year = null;
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

}
