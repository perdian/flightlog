package de.perdian.apps.flighttracker.web.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import de.perdian.apps.flighttracker.persistence.entities.UserEntity;

public class FlighttrackerUser implements Serializable {

    static final long serialVersionUID = 1L;

    private UserEntity userEntitiy = null;

    public UserEntity getUserEntitiy() {
        return this.userEntitiy;
    }
    public void setUserEntitiy(UserEntity userEntitiy) {
        this.userEntitiy = userEntitiy;
    }

    public Collection<UserEntity> toUserEntities() {
        return Optional.ofNullable(this.getUserEntitiy()).map(Collections::singleton).orElse(null);
    }

}
