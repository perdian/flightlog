package de.perdian.flightlog.modules.users.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.perdian.flightlog.modules.users.persistence.UserEntity;
import de.perdian.flightlog.modules.users.persistence.UsersRepository;

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
