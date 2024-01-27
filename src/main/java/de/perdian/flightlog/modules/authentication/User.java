package de.perdian.flightlog.modules.authentication;

import de.perdian.flightlog.modules.authentication.persistence.UserEntity;

public interface User {

    String getUsername();

    UserEntity getEntity();

}
