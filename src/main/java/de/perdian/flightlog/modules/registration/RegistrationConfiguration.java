package de.perdian.flightlog.modules.registration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "flightlog.registration")
public class RegistrationConfiguration {

    private boolean restricted = true;

    public boolean isRestricted() {
        return this.restricted;
    }
    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

}
