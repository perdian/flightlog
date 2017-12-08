package de.perdian.apps.flighttracker.modules.security.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "flighttracker.authentication")
public class AuthenticationConfiguration {

    private AuthenticationType type = null;

    public AuthenticationType getType() {
        return this.type;
    }
    public void setType(AuthenticationType type) {
        this.type = type;
    }

}
