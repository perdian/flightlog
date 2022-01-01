package de.perdian.flightlog.modules.authentication.local;

import de.perdian.flightlog.modules.authentication.FlightlogUser;
import de.perdian.flightlog.modules.users.persistence.UserEntity;

class LocalAuthenticationFlightlogUser implements FlightlogUser {

    static final long serialVersionUID = 1L;

    private UserEntity userEntity = null;
    private String name = null;
    private String information = null;

    @Override
    public String getName() {
        return this.name;
    }
    void setName(String name) {
        this.name = name;
    }

    @Override
    public UserEntity getUserEntity() {
        return this.userEntity;
    }
    void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public String getInformation() {
        return this.information;
    }
    void setInformation(String information) {
        this.information = information;
    }

}