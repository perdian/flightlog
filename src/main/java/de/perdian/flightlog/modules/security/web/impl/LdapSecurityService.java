package de.perdian.flightlog.modules.security.web.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import de.perdian.flightlog.modules.security.web.AuthenticationProviderSkeleton;
import de.perdian.flightlog.modules.security.web.FlightlogUser;
import de.perdian.flightlog.modules.users.persistence.UserEntity;
import de.perdian.flightlog.modules.users.services.UsersQuery;
import de.perdian.flightlog.modules.users.services.UsersQueryService;
import de.perdian.flightlog.modules.users.services.UsersUpdateService;

class LdapSecurityService extends AuthenticationProviderSkeleton {

    private static final String AUTHENTICATION_SOURCE_IDENTIFIER = "ldap";

    private UsersQueryService usersQueryService = null;
    private UsersUpdateService usersUpdateService = null;
    private LdapConfiguration ldapConfiguration = null;
    private LdapTemplate ldapTemplate = null;

    @Override
    protected FlightlogUser authenticateUser(String username, String password) throws AuthenticationException {

        Filter ldapUserFilter = new EqualsFilter(this.getLdapConfiguration().getUsernameField(), username);
        boolean ldapAuthenticationResult = this.ldapTemplate.authenticate(this.getLdapConfiguration().getUserDn(), ldapUserFilter.encode(), password);
        if (!ldapAuthenticationResult) {
            throw new BadCredentialsException("Cannot authenticate user: " + username);
        } else {

            UsersQuery usersQuery = new UsersQuery();
            usersQuery.setRestrictAuthenticationSources(Arrays.asList(LdapSecurityService.AUTHENTICATION_SOURCE_IDENTIFIER));
            usersQuery.setRestrictUsernames(Arrays.asList(username));
            List<UserEntity> userEntities = this.getUsersQueryService().loadUsers(usersQuery);
            UserEntity userEntity = userEntities == null || userEntities.isEmpty() ? null : userEntities.get(0);
            if (userEntity == null) {
                UserEntity newUserEntity = new UserEntity();
                newUserEntity.setAuthenticationSource(LdapSecurityService.AUTHENTICATION_SOURCE_IDENTIFIER);
                newUserEntity.setUsername(username);
                return this.createUser(this.getUsersUpdateService().saveUser(newUserEntity));
            } else {
                return this.createUser(userEntity);
            }

        }

    }

    private FlightlogUser createUser(UserEntity userEntitiy) {
        FlightlogUser user = new FlightlogUser();
        user.setUserEntity(userEntitiy);
        return user;
    }

    UsersQueryService getUsersQueryService() {
        return this.usersQueryService;
    }
    @Autowired
    void setUsersQueryService(UsersQueryService usersQueryService) {
        this.usersQueryService = usersQueryService;
    }

    UsersUpdateService getUsersUpdateService() {
        return this.usersUpdateService;
    }
    @Autowired
    void setUsersUpdateService(UsersUpdateService usersUpdateService) {
        this.usersUpdateService = usersUpdateService;
    }

    LdapConfiguration getLdapConfiguration() {
        return this.ldapConfiguration;
    }
    @Autowired
    void setLdapConfiguration(LdapConfiguration ldapConfiguration) {
        this.ldapConfiguration = ldapConfiguration;
    }

    LdapTemplate getLdapTemplate() {
        return this.ldapTemplate;
    }
    @Autowired
    void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

}
