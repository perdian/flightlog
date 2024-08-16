package de.perdian.flightlog.modules.authentication.configuration.oauth2.impl;

import de.perdian.flightlog.modules.authentication.configuration.oauth2.Oauth2AuthenticationClientRegistrationProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

@Component
@Order(0)
class GoogleClientRegistrationProvider implements Oauth2AuthenticationClientRegistrationProvider {

    private String googleClientId = null;
    private String googleClientSecret = null;

    @Override
    public ClientRegistration createRegistration(String registrationId) {
        if (StringUtils.isNotEmpty(this.getGoogleClientId()) || StringUtils.isNotEmpty(this.getGoogleClientSecret())) {
            return CommonOAuth2Provider.GOOGLE.getBuilder(registrationId)
                .clientId(this.getGoogleClientId())
                .clientSecret(this.getGoogleClientSecret())
                .build();
        } else {
            return null;
        }
    }

    String getGoogleClientId() {
        return this.googleClientId;
    }
    @Value("${FLIGHTLOG_AUTHENTICATION_OAUTH2_GOOGLE_CLIENT_ID:}")
    void setGoogleClientId(String googleClientId) {
        this.googleClientId = googleClientId;
    }

    String getGoogleClientSecret() {
        return this.googleClientSecret;
    }
    @Value("${FLIGHTLOG_AUTHENTICATION_OAUTH2_GOOGLE_CLIENT_SECRET:}")
    void setGoogleClientSecret(String googleClientSecret) {
        this.googleClientSecret = googleClientSecret;
    }

}
