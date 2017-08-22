package de.perdian.apps.flighttracker.business.modules.users;

import java.util.List;

import de.perdian.apps.flighttracker.persistence.entities.UserEntity;

public interface UsersQueryService {

    List<UserEntity> loadUsers(UsersQuery usersQuery);

}
