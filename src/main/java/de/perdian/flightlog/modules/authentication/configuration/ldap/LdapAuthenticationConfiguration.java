package de.perdian.flightlog.modules.authentication.configuration.ldap;

import de.perdian.flightlog.modules.authentication.configuration.AbstractAuthenticationConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

import java.util.List;

@Configuration
@ConditionalOnExpression("#{environment['FLIGHTLOG_AUTHENTICATION_TYPE'] != null and environment['FLIGHTLOG_AUTHENTICATION_TYPE'].toUpperCase() == 'LDAP'}")
class LdapAuthenticationConfiguration extends AbstractAuthenticationConfiguration {

    private String userSearchBase = null;
    private String userSearchFilter = null;
    private String userDnPatterns = null;
    private String serverUrl = null;
    private String baseDn = null;
    private String bindUserDn = null;
    private String bindUserPassword = null;

    @Override
    protected void configureSecurityFilterChainHttpSecurity(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/authentication/login/form");
            formLogin.loginProcessingUrl("/authentication/login/submit");
        });
    }

    @Bean
    LdapAuthenticationUserDetailsContextMapper ldapAuthenticationUserDetailsContextMapper() {
        return new LdapAuthenticationUserDetailsContextMapper();
    }

    @Bean
    AuthenticationManager authenticationManager(LdapAuthenticationUserDetailsContextMapper ldapAuthenticationUserDetailsContextMapper) {
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(List.of(this.getServerUrl()), this.getBaseDn());
        contextSource.setUserDn(StringUtils.defaultIfEmpty(this.getBindUserDn(), null));
        contextSource.setPassword(StringUtils.defaultIfEmpty(this.getBindUserPassword(), null));
        contextSource.afterPropertiesSet();

        LdapBindAuthenticationManagerFactory ldapBindAuthenticationManagerFactory = new LdapBindAuthenticationManagerFactory(contextSource);
        ldapBindAuthenticationManagerFactory.setUserSearchBase(StringUtils.defaultIfEmpty(this.getUserSearchBase(), null));
        ldapBindAuthenticationManagerFactory.setUserSearchFilter(StringUtils.defaultIfEmpty(this.getUserSearchFilter(), null));
        ldapBindAuthenticationManagerFactory.setUserDnPatterns(StringUtils.defaultIfEmpty(this.getUserDnPatterns(), null));
        ldapBindAuthenticationManagerFactory.setUserDetailsContextMapper(ldapAuthenticationUserDetailsContextMapper);
        return ldapBindAuthenticationManagerFactory.createAuthenticationManager();
    }

    String getUserSearchBase() {
        return this.userSearchBase;
    }
    @Value("${FLIGHTLOG_AUTHENTICATION_LDAP_USER_SEARCH_BASE:ou=users}")
    void setUserSearchBase(String userSearchBase) {
        this.userSearchBase = userSearchBase;
    }

    String getUserSearchFilter() {
        return this.userSearchFilter;
    }
    @Value("${FLIGHTLOG_AUTHENTICATION_LDAP_USER_SEARCH_FILTER:}")
    void setUserSearchFilter(String userSearchFilter) {
        this.userSearchFilter = userSearchFilter;
    }

    String getUserDnPatterns() {
        return this.userDnPatterns;
    }
    @Value("${FLIGHTLOG_AUTHENTICATION_LDAP_USER_DN_PATTERNS:uid={0},ou=users}")
    void setUserDnPatterns(String userDnPatterns) {
        this.userDnPatterns = userDnPatterns;
    }

    String getServerUrl() {
        return this.serverUrl;
    }
    @Value("${FLIGHTLOG_AUTHENTICATION_LDAP_SERVER_URL}")
    void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    String getBaseDn() {
        return this.baseDn;
    }
    @Value("${FLIGHTLOG_AUTHENTICATION_LDAP_BASE_DN:}")
    void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    String getBindUserDn() {
        return this.bindUserDn;
    }
    @Value("${FLIGHTLOG_AUTHENTICATION_LDAP_BIND_USER_DN:}")
    void setBindUserDn(String bindUserDn) {
        this.bindUserDn = bindUserDn;
    }

    String getBindUserPassword() {
        return this.bindUserPassword;
    }
    @Value("${FLIGHTLOG_AUTHENTICATION_LDAP_BIND_USER_PASSWORD:}")
    void setBindUserPassword(String bindUserPassword) {
        this.bindUserPassword = bindUserPassword;
    }

}
