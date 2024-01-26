package de.perdian.flightlog.modules.authentication.configuration.oauth;

import de.perdian.flightlog.modules.authentication.configuration.AbstractAuthenticationConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@ConditionalOnProperty(name = "flightlog.authentication.type", havingValue = "oauth")
public class OauthAuthenticationConfiguration extends AbstractAuthenticationConfiguration {

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
    OauthAuthenticationUserService oidcUserService() {
        return new OauthAuthenticationUserService();
    }

}
