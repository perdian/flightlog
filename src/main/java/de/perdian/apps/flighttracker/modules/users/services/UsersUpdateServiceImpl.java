package de.perdian.apps.flighttracker.modules.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;
import de.perdian.apps.flighttracker.modules.users.persistence.UsersRepository;

@Service
class UsersUpdateServiceImpl implements UsersUpdateService {

    private UsersRepository usersRepository = null;

    @Override
    @Transactional
    public UserEntity saveUser(UserEntity userEntity) {
        return this.getUsersRepository().save(userEntity);
    }

    UsersRepository getUsersRepository() {
        return this.usersRepository;
    }
    @Autowired
    void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

}
