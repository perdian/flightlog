package de.perdian.flightlog.modules.users.services;

import de.perdian.flightlog.modules.users.persistence.UserEntity;

public interface UsersUpdateService {

    UserEntity saveUser(UserEntity userEntity);

}
