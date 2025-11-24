package de.perdian.flightlog.modules.authentication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

public abstract class AbstractAuthenticationConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/error/**").permitAll();
            auth.requestMatchers("/assets/**").permitAll();
            auth.requestMatchers("/webjars/**").permitAll();
            auth.requestMatchers("/authentication/login/**").permitAll();
            auth.requestMatchers("/authentication/logout/**").permitAll();
            auth.anyRequest().authenticated();
        });

        httpSecurity.logout(logout -> {
            logout
                .logoutRequestMatcher(PathPatternRequestMatcher.pathPattern("/authentication/logout"))
                .logoutSuccessUrl("/authentication/logout/completed");
        });

        httpSecurity.csrf(csrfCustomizer -> csrfCustomizer.disable());

        this.configureSecurityFilterChainHttpSecurity(httpSecurity);
        return httpSecurity.build();

    }

    protected abstract void configureSecurityFilterChainHttpSecurity(HttpSecurity httpSecurity) throws Exception;

}
