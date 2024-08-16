package de.perdian.flightlog.modules.authentication.configuration.oauth2;

import de.perdian.flightlog.modules.authentication.configuration.AbstractAuthenticationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.List;

@Configuration
@ConditionalOnExpression("#{environment['FLIGHTLOG_AUTHENTICATION_TYPE'] == null or environment['FLIGHTLOG_AUTHENTICATION_TYPE'].toUpperCase() == 'OAUTH2'}")
class Oauth2AuthenticationConfiguration extends AbstractAuthenticationConfiguration {

    static final String REGISTRATION_ID = "flightlog";

    private List<Oauth2AuthenticationClientRegistrationProvider> registrationProviders = null;

    @Override
    protected void configureSecurityFilterChainHttpSecurity(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.oauth2Login(oauthLogin -> {
            oauthLogin.loginPage("/authentication/login/oauth2");
            oauthLogin.userInfoEndpoint(userInfoEndpointCustomizer -> {
                userInfoEndpointCustomizer.oidcUserService(this.oidcUserService());
            });
        });

    }

    @Bean
    Oauth2AuthenticationUserService oidcUserService() {
        return new Oauth2AuthenticationUserService();
    }

    @Bean
    ClientRegistration clientRegistration() {
        for (Oauth2AuthenticationClientRegistrationProvider registrationProvider : this.getRegistrationProviders()) {
            ClientRegistration registration = registrationProvider.createRegistration(REGISTRATION_ID);
            if (registration != null) {
                return registration;
            }
        }
        throw new IllegalArgumentException("Cannot create OAuth2 ClientRegistration instance from any provider");
    }

    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.clientRegistration());
    }

    List<Oauth2AuthenticationClientRegistrationProvider> getRegistrationProviders() {
        return this.registrationProviders;
    }
    @Autowired
    void setRegistrationProviders(List<Oauth2AuthenticationClientRegistrationProvider> registrationProviders) {
        this.registrationProviders = registrationProviders;
    }

}
