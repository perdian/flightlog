package de.perdian.flightlog.modules.authentication.configuration.oauth2.impl;

import de.perdian.flightlog.modules.authentication.configuration.oauth2.Oauth2AuthenticationClientRegistrationProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

@Component
@Order(10)
class GenericClientRegistrationProvider implements Oauth2AuthenticationClientRegistrationProvider {

    @Override
    public ClientRegistration createRegistration(String registrationId) {
        return ClientRegistration.withRegistrationId(registrationId)
            .clientId(this.extractRequiredEnvironmentVariable("FLIGHTLOG_AUTHENTICATION_OAUTH2_CLIENT_ID"))
            .clientSecret(this.extractRequiredEnvironmentVariable("FLIGHTLOG_AUTHENTICATION_OAUTH2_CLIENT_SECRET"))
            .clientName(this.extractEnvironmentVariable("FLIGHTLOG_AUTHENTICATION_OAUTH2_CLIENT_NAME", "flightlog"))
            .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
            .authorizationUri(this.extractRequiredEnvironmentVariable("FLIGHTLOG_AUTHENTICATION_OAUTH2_AUTHORIZATION_URI"))
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .tokenUri(this.extractRequiredEnvironmentVariable("FLIGHTLOG_AUTHENTICATION_OAUTH2_TOKEN_URI"))
            .userInfoUri(this.extractRequiredEnvironmentVariable("FLIGHTLOG_AUTHENTICATION_OAUTH2_USER_INFO_URI"))
            .userNameAttributeName(this.extractEnvironmentVariable("FLIGHTLOG_AUTHENTICATION_OAUTH2_USER_NAME_ATTRIBUTE", "name"))
            .jwkSetUri(this.extractRequiredEnvironmentVariable("FLIGHTLOG_AUTHENTICATION_OAUTH2_JWK_SET_URI"))
            .scope("openid", "profile", "email")
            .build();
    }

    private String extractEnvironmentVariable(String environmentVariable, String defaultValue) {
        return StringUtils.defaultIfEmpty(System.getenv(environmentVariable), defaultValue);
    }

    private String extractRequiredEnvironmentVariable(String environmentVariable) {
        String environmentVariableValue = System.getenv(environmentVariable);
        if (StringUtils.isEmpty(environmentVariableValue)) {
            throw new IllegalArgumentException("Required environment variable is not set: " + environmentVariable);
        } else {
            return environmentVariableValue;
        }
    }

}
