package de.perdian.flightlog.modules.security.web.impl;

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

import de.perdian.flightlog.modules.security.web.AuthenticationProviderSkeleton;
import de.perdian.flightlog.modules.security.web.FlightlogUser;
import de.perdian.flightlog.modules.users.persistence.UserEntity;
import de.perdian.flightlog.modules.users.services.UsersQuery;
import de.perdian.flightlog.modules.users.services.UsersQueryService;

class InternalDatabaseSecurityService extends AuthenticationProviderSkeleton {

    private static final String AUTHENTICATION_SOURCE_IDENTIFIER = "internaldatabase";

    private static final Logger log = LoggerFactory.getLogger(InternalDatabaseSecurityService.class);
    private UsersQueryService usersQueryService = null;
    private InternalDatabaseConfiguration internalDatabaseConfiguration = null;

    @Override
    protected FlightlogUser authenticateUser(String username, String password) throws AuthenticationException {
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
                    FlightlogUser responseUser = new FlightlogUser();
                    responseUser.setUserEntity(user);
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
