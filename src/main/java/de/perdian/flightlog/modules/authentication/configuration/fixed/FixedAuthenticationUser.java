package de.perdian.flightlog.modules.authentication.configuration.fixed;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.authentication.persistence.UserEntity;

class FixedAuthenticationUser implements User {

    private UserEntity entity = null;

    FixedAuthenticationUser(UserEntity entity) {
        this.setEntity(entity);
    }

    @Override
    public String getUsername() {
        return this.getEntity().getUsername();
    }

    @Override
    public UserEntity getEntity() {
        return this.entity;
    }
    private void setEntity(UserEntity entity) {
        this.entity = entity;
    }

}
