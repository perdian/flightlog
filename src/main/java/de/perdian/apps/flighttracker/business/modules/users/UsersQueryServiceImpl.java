package de.perdian.apps.flighttracker.business.modules.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.perdian.apps.flighttracker.persistence.entities.UserEntity;
import de.perdian.apps.flighttracker.persistence.repositories.UsersRepository;

@Service
class UsersQueryServiceImpl implements UsersQueryService {

    private UsersRepository usersRepository = null;

    @Override
    public List<UserEntity> loadUsers(UsersQuery usersQuery) {
        return this.getUsersRepository().findAll((root, query, cb) -> usersQuery.toPredicate(root, query, cb));
    }

    UsersRepository getUsersRepository() {
        return this.usersRepository;
    }
    @Autowired
    void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

}
