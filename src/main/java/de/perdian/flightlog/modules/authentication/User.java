package de.perdian.flightlog.modules.authentication;

import de.perdian.flightlog.modules.authentication.persistence.UserEntity;

public interface User {

    UserEntity getEntity();

    String getInformation();

}
