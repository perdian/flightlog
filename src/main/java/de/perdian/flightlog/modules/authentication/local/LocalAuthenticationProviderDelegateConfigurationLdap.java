package de.perdian.flightlog.modules.authentication.local;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.security.authentication.BadCredentialsException;

import de.perdian.flightlog.modules.authentication.FlightlogAuthenticationSettings.FlightlogAuthenticationLdapSettings;
import de.perdian.flightlog.modules.users.persistence.UserEntity;
import de.perdian.flightlog.modules.users.services.UsersQuery;
import de.perdian.flightlog.modules.users.services.UsersQueryService;
import de.perdian.flightlog.modules.users.services.UsersUpdateService;

@Configuration
@ConditionalOnExpression("#{environment['flightlog.authentication.ldap.enabled'] eq 'true'}")
class LocalAuthenticationProviderDelegateConfigurationLdap {

    FlightlogAuthenticationLdapSettings settings = null;

    @Bean
    @Order(value = Ordered.LOWEST_PRECEDENCE)
    LocalAuthenticationProviderDelegate localAuthenticationProviderDelegateLdap() {
        return new LocalAuthenticationProviderDelegateLdap();
    }

    @Bean
    LdapContextSource ldapContextSource() {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl(this.getSettings().getUrl());
        ldapContextSource.setBase(this.getSettings().getBaseDn());
        ldapContextSource.setUserDn(this.getSettings().getBindDn());
        ldapContextSource.setPassword(this.getSettings().getBindPassword());
        return ldapContextSource;
    }

    @Bean
    LdapTemplate ldapTemplate() {
        return new LdapTemplate(this.ldapContextSource());
    }

    FlightlogAuthenticationLdapSettings getSettings() {
        return this.settings;
    }
    @Autowired
    void setSettings(FlightlogAuthenticationLdapSettings settings) {
        this.settings = settings;
    }

    static class LocalAuthenticationProviderDelegateLdap implements LocalAuthenticationProviderDelegate {

        private FlightlogAuthenticationLdapSettings settings = null;
        private UsersQueryService usersQueryService = null;
        private UsersUpdateService usersUpdateService = null;
        private LdapTemplate ldapTemplate = null;

        @Override
        public UserEntity resolveUser(String username, String password) {
            Filter ldapUserFilter = new EqualsFilter(this.getSettings().getUsernameField(), username);
            boolean ldapAuthenticationResult = this.getLdapTemplate().authenticate(this.getSettings().getUserDn(), ldapUserFilter.encode(), password);
            if (!ldapAuthenticationResult) {
                throw new BadCredentialsException("Cannot authenticate user: " + username);
            } else {
                UsersQuery usersQuery = new UsersQuery();
                usersQuery.setRestrictAuthenticationSources(Collections.singleton("ldap"));
                usersQuery.setRestrictUsernames(Collections.singleton(username));
                List<UserEntity> userEntities = this.getUsersQueryService().loadUsers(usersQuery);
                UserEntity userEntity = userEntities == null || userEntities.isEmpty() ? null : userEntities.get(0);
                if (userEntity == null) {
                    UserEntity newUserEntity = new UserEntity();
                    newUserEntity.setAuthenticationSource("ldap");
                    newUserEntity.setUsername(username);
                    return this.getUsersUpdateService().saveUser(newUserEntity);
                } else {
                    return userEntity;
                }
            }
        }

        FlightlogAuthenticationLdapSettings getSettings() {
            return this.settings;
        }
        @Autowired
        void setSettings(FlightlogAuthenticationLdapSettings settings) {
            this.settings = settings;
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

        LdapTemplate getLdapTemplate() {
            return this.ldapTemplate;
        }
        @Autowired
        void setLdapTemplate(LdapTemplate ldapTemplate) {
            this.ldapTemplate = ldapTemplate;
        }

    }

}