package de.perdian.flightlog.modules.authentication.local;

import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;

import de.perdian.flightlog.modules.authentication.FlightlogAuthenticationSettings.FlightlogAuthenticationLocalSettings;
import de.perdian.flightlog.modules.users.persistence.UserEntity;
import de.perdian.flightlog.modules.users.services.UsersQuery;
import de.perdian.flightlog.modules.users.services.UsersQueryService;

@Configuration
@ConditionalOnExpression("#{environment['flightlog.authentication.local.enabled'] eq 'true'}")
class LocalAuthenticationProviderDelegateConfigurationLocal {

    @Bean
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    LocalAuthenticationProviderDelegate localAuthenticationProviderDelegateDatabase() {
        return new LocalAuthenticationProviderDelegateDatabase();
    }

    static class LocalAuthenticationProviderDelegateDatabase implements LocalAuthenticationProviderDelegate {

        private static final Logger log = LoggerFactory.getLogger(LocalAuthenticationProviderDelegateDatabase.class);

        private UsersQueryService usersQueryService = null;
        private FlightlogAuthenticationLocalSettings settings = null;

        @Override
        public UserEntity resolveUser(String username, String password) {
            if (StringUtils.isEmpty(password)) {
                throw new BadCredentialsException("Password must not be empty!");
            } else {

                log.debug("Query for internal database user with username: {}", username);
                UsersQuery usersQuery = new UsersQuery();
                usersQuery.setRestrictAuthenticationSources(Collections.singleton("localdatabase"));
                usersQuery.setRestrictUsernames(Collections.singleton(username));
                List<UserEntity> users = this.getUsersQueryService().loadUsers(usersQuery);
                UserEntity user = users == null || users.isEmpty() ? null : users.get(0);
                if (user == null) {
                    log.debug("Could not find internal database user with username '{}' for login", username);
                    return null;
                } else {

                    log.debug("Found user in local database with username '{}' for login: {}", username, user);
                    StringBuilder passwordHashInput = new StringBuilder();
                    passwordHashInput.append(StringUtils.defaultIfEmpty(this.getSettings().getHashSeedPrefix(), ""));
                    passwordHashInput.append(StringUtils.defaultIfEmpty(username, ""));
                    passwordHashInput.append(StringUtils.defaultIfEmpty(password, ""));
                    passwordHashInput.append(StringUtils.defaultIfEmpty(this.getSettings().getHashSeedPostfix(), ""));
                    String passwordHash = DigestUtils.sha256Hex(passwordHashInput.toString());
                    if (!passwordHash.equals(user.getPassword())) {
                        log.info("Could not validate password for internal database user with username '{}' and id '{}' for login", username, user.getUserId());
                        throw new BadCredentialsException("Invalid credentials!");
                    } else {
                        log.info("Successfully validated internal database user with username '{}' and id '{}'", username, user.getUserId());
                        return user;
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

        FlightlogAuthenticationLocalSettings getSettings() {
            return this.settings;
        }
        @Autowired
        void setSettings(FlightlogAuthenticationLocalSettings settings) {
            this.settings = settings;
        }

    }

}