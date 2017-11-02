package de.perdian.apps.flighttracker.modules.users.services;

import java.util.List;

import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

public interface UsersQueryService {

    List<UserEntity> loadUsers(UsersQuery usersQuery);

}
