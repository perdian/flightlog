package de.perdian.flightlog.modules.authentication.service.userdetails;

import de.perdian.flightlog.modules.authentication.persistence.UserEntity;

class FlightlogUserDetailsImpl implements FlightlogUserDetails {

    private String username = null;
    private UserEntity userEntity = null;

    FlightlogUserDetailsImpl(String username, UserEntity userEntity) {
        this.setUsername(username);
        this.setUserEntity(userEntity);
    }

    @Override
    public String getUsername() {
        return this.username;
    }
    private void setUsername(String username) {
        this.username = username;
    }

    @Override
    public UserEntity getUserEntity() {
        return this.userEntity;
    }
    private void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

}
