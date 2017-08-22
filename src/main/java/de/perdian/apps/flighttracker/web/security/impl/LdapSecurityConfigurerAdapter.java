package de.perdian.apps.flighttracker.web.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import de.perdian.apps.flighttracker.configuration.LdapConfiguration;
import de.perdian.apps.flighttracker.web.security.FlighttrackerSecurityConfigurerAdapter;
import de.perdian.apps.flighttracker.web.security.FlighttrackerSecurityService;

@Configuration
@ConditionalOnExpression("#{environment['flighttracker.authentication.type'] eq 'ldap'}")
class LdapSecurityConfigurerAdapter extends FlighttrackerSecurityConfigurerAdapter {

    private LdapConfiguration ldapConfiguration = null;

    @Override
    protected FlighttrackerSecurityService createSecurityService() {
        return new LdapSecurityService();
    }

    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl(this.getLdapConfiguration().getUrl());
        ldapContextSource.setBase(this.getLdapConfiguration().getBaseDn());
        ldapContextSource.setUserDn(this.getLdapConfiguration().getBindDn());
        ldapContextSource.setPassword(this.getLdapConfiguration().getBindPassword());
        return ldapContextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(this.ldapContextSource());
    }

    LdapConfiguration getLdapConfiguration() {
        return this.ldapConfiguration;
    }
    @Autowired
    void setLdapConfiguration(LdapConfiguration ldapConfiguration) {
        this.ldapConfiguration = ldapConfiguration;
    }

}
