package de.perdian.apps.flighttracker.modules.security.web.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

import de.perdian.apps.flighttracker.modules.security.web.FlighttrackerWebSecurityConfigurerAdapter;
import de.perdian.apps.flighttracker.modules.security.web.AuthenticationProviderSkeleton;

@Configuration
@ConditionalOnExpression("#{environment['flighttracker.authentication.type'] eq 'internaldatabase'}")
class InternalDatabaseSecurityConfigurerAdapter extends FlighttrackerWebSecurityConfigurerAdapter {

    @Override
    protected AuthenticationProviderSkeleton createSecurityService() {
        return new InternalDatabaseSecurityService();
    }

}
