package de.perdian.apps.flighttracker.modules.users.services;

import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

public interface UsersUpdateService {

    UserEntity saveUser(UserEntity userEntity);

}
