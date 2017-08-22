package de.perdian.apps.flighttracker.web.security.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

import de.perdian.apps.flighttracker.web.security.FlighttrackerSecurityConfigurerAdapter;
import de.perdian.apps.flighttracker.web.security.FlighttrackerSecurityService;

@Configuration
@ConditionalOnExpression("#{environment['flighttracker.authentication.type'] eq 'internaldatabase'}")
class InternalDatabaseSecurityConfigurerAdapter extends FlighttrackerSecurityConfigurerAdapter {

    @Override
    protected FlighttrackerSecurityService createSecurityService() {
        return new InternalDatabaseSecurityService();
    }

}
