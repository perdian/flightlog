package de.perdian.apps.flighttracker.business.modules.users;

import de.perdian.apps.flighttracker.persistence.entities.UserEntity;

public interface UsersUpdateService {

    UserEntity saveUser(UserEntity userEntity);

}
