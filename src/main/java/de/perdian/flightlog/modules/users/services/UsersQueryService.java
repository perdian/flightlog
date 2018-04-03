package de.perdian.flightlog.modules.users.services;

import java.util.List;

import de.perdian.flightlog.modules.users.persistence.UserEntity;

public interface UsersQueryService {

    List<UserEntity> loadUsers(UsersQuery usersQuery);

}
