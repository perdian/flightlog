package de.perdian.apps.flighttracker.modules.security.web.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

import de.perdian.apps.flighttracker.modules.security.web.FlighttrackerSecurityConfigurerAdapter;
import de.perdian.apps.flighttracker.modules.security.web.FlighttrackerSecurityService;

@Configuration
@ConditionalOnExpression("#{environment['flighttracker.authentication.type'] eq 'internaldatabase'}")
class InternalDatabaseSecurityConfigurerAdapter extends FlighttrackerSecurityConfigurerAdapter {

    @Override
    protected FlighttrackerSecurityService createSecurityService() {
        return new InternalDatabaseSecurityService();
    }

}
