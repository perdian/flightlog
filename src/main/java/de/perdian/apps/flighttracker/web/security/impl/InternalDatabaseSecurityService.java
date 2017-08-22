package de.perdian.apps.flighttracker.web.security.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.perdian.apps.flighttracker.business.modules.users.UsersQuery;
import de.perdian.apps.flighttracker.business.modules.users.UsersQueryService;
import de.perdian.apps.flighttracker.configuration.InternalDatabaseConfiguration;
import de.perdian.apps.flighttracker.persistence.entities.UserEntity;
import de.perdian.apps.flighttracker.web.security.FlighttrackerSecurityService;
import de.perdian.apps.flighttracker.web.security.FlighttrackerUser;

class InternalDatabaseSecurityService extends FlighttrackerSecurityService {

    private static final String AUTHENTICATION_SOURCE_IDENTIFIER = "internaldatabase";

    private static final Logger log = LoggerFactory.getLogger(InternalDatabaseSecurityService.class);
    private UsersQueryService usersQueryService = null;
    private InternalDatabaseConfiguration internalDatabaseConfiguration = null;

    @Override
    protected FlighttrackerUser authenticateUser(String username, String password) throws AuthenticationException {
        if (StringUtils.isEmpty(password)) {
            throw new BadCredentialsException("Password must not be empty!");
        } else {

            log.debug("Query for internal database user with username: {}", username);
            UsersQuery usersQuery = new UsersQuery();
            usersQuery.setRestrictAuthenticationSources(Arrays.asList(InternalDatabaseSecurityService.AUTHENTICATION_SOURCE_IDENTIFIER));
            usersQuery.setRestrictUsernames(Arrays.asList(username));
            List<UserEntity> users = this.getUsersQueryService().loadUsers(usersQuery);
            UserEntity user = users == null || users.isEmpty() ? null : users.get(0);
            if (user == null) {
                log.info("Could not find internal database user with username '{}' for login", username);
                throw new UsernameNotFoundException("Cannot find user: " + username);
            } else {

                StringBuilder comparePassword = new StringBuilder();
                comparePassword.append(Optional.ofNullable(this.getInternalDatabaseConfiguration().getHashSeedPrefix()).orElse(""));
                comparePassword.append(password == null ? "" : password);
                comparePassword.append(Optional.ofNullable(this.getInternalDatabaseConfiguration().getHashSeedPostfix()).orElse(""));

                String passwordHash = DigestUtils.sha256Hex(comparePassword.toString());
                if (!passwordHash.equals(user.getPassword())) {
                    log.info("Could not validate password for internal database user with username '{}' and id '{}' for login", username, user.getUserId());
                    throw new BadCredentialsException("Invalid credentials!");
                } else {
                    log.info("Successfully validate internal database user with username '{}' and id '{}'", username, user.getUserId());
                    FlighttrackerUser responseUser = new FlighttrackerUser();
                    responseUser.setUserEntitiy(user);
                    return responseUser;
                }

            }
        }
    }

    UsersQueryService getUsersQueryService() {
        return this.usersQueryService;
    }
    @Autowired
    void setUsersQueryService(UsersQueryService usersQueryService) {
        this.usersQueryService = usersQueryService;
    }

    InternalDatabaseConfiguration getInternalDatabaseConfiguration() {
        return this.internalDatabaseConfiguration;
    }
    @Autowired
    void setInternalDatabaseConfiguration(InternalDatabaseConfiguration internalDatabaseConfiguration) {
        this.internalDatabaseConfiguration = internalDatabaseConfiguration;
    }

}
