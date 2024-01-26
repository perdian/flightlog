package de.perdian.flightlog.modules.authentication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public abstract class AbstractAuthenticationConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/assets/**").permitAll();
            auth.requestMatchers("/webjars/**").permitAll();
            auth.requestMatchers("/authentication/login/**").permitAll();
            auth.requestMatchers("/authentication/logout/**").permitAll();
            auth.anyRequest().authenticated();
        });

        httpSecurity.logout(logout -> {
            logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/authentication/logout"))
                .logoutSuccessUrl("/authentication/logout/completed");
        });

        this.configureSecurityFilterChainHttpSecurity(httpSecurity);
        return httpSecurity.build();

    }

    protected abstract void configureSecurityFilterChainHttpSecurity(HttpSecurity httpSecurity) throws Exception;

}
