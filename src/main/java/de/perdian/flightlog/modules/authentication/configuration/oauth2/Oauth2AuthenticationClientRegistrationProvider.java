package de.perdian.flightlog.modules.authentication.configuration.oauth2;

import org.springframework.security.oauth2.client.registration.ClientRegistration;

public interface Oauth2AuthenticationClientRegistrationProvider {

    ClientRegistration createRegistration(String registrationId);

}
