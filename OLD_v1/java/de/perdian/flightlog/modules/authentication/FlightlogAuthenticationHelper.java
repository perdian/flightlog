package de.perdian.flightlog.modules.authentication;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FlightlogAuthenticationHelper {

    private FlightlogAuthenticationSettings settings = null;
    private String googleClientId = null;
    private String googleClientSecret = null;

    public boolean isLocalEnabled() {
        return this.getSettings().getLocal().isEnabled() || this.getSettings().getLdap().isEnabled();
    }

    public boolean isGoogleEnabled() {
        return StringUtils.isNotEmpty(this.getGoogleClientId()) && StringUtils.isNotEmpty(this.getGoogleClientSecret());
    }

    FlightlogAuthenticationSettings getSettings() {
        return this.settings;
    }
    @Autowired
    void setSettings(FlightlogAuthenticationSettings settings) {
        this.settings = settings;
    }

    String getGoogleClientId() {
        return this.googleClientId;
    }
    @Value("#{environment['spring.security.oauth2.client.registration.google.client-id']}")
    void setGoogleClientId(String googleClientId) {
        this.googleClientId = googleClientId;
    }

    String getGoogleClientSecret() {
        return this.googleClientSecret;
    }
    @Value("#{environment['spring.security.oauth2.client.registration.google.client-secret']}")
    void setGoogleClientSecret(String googleClientSecret) {
        this.googleClientSecret = googleClientSecret;
    }

}
