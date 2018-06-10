package de.perdian.flightlog.modules.registration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "flightlog.registration")
public class RegistrationConfiguration {

    private boolean restricted = true;
    private List<String> emailWhitelist = null;

    public boolean isRestricted() {
        return this.restricted;
    }
    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public List<String> getEmailWhitelist() {
        return this.emailWhitelist;
    }
    public void setEmailWhitelist(List<String> emailWhitelist) {
        this.emailWhitelist = emailWhitelist;
    }

}
