package de.perdian.flightlog.modules.authentication.configuration.fixed;

import de.perdian.flightlog.modules.authentication.persistence.UserEntity;
import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetails;

class FixedAuthenticationUser implements FlightlogUserDetails {

    private UserEntity userEntity = null;

    FixedAuthenticationUser(UserEntity userEntity) {
        this.setUserEntity(userEntity);
    }

    @Override
    public String toString() {
        return "FixedAuthenticationUser[" + this.getUserEntity() + "]";
    }

    @Override
    public String getUsername() {
        return this.getUserEntity().getUsername();
    }

    @Override
    public UserEntity getUserEntity() {
        return this.userEntity;
    }
    private void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

}
