package de.perdian.flightlog.modules.security.web;

import java.io.Serializable;

import de.perdian.flightlog.modules.users.persistence.UserEntity;

public class FlightlogUser implements Serializable {

    static final long serialVersionUID = 1L;

    private UserEntity userEntity = null;

    public UserEntity getUserEntity() {
        return this.userEntity;
    }
    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

}
