package de.perdian.flightlog.modules.security.web.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

import de.perdian.flightlog.modules.security.web.AuthenticationProviderSkeleton;
import de.perdian.flightlog.modules.security.web.FlightlogWebSecurityConfigurerAdapter;

@Configuration
@ConditionalOnExpression("#{environment['flightlog.authentication.type'] eq 'internaldatabase'}")
class InternalDatabaseSecurityConfigurerAdapter extends FlightlogWebSecurityConfigurerAdapter {

    @Override
    protected AuthenticationProviderSkeleton createSecurityService() {
        return new InternalDatabaseSecurityService();
    }

}
