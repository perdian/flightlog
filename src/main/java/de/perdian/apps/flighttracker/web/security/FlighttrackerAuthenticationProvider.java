package de.perdian.apps.flighttracker.web.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class FlighttrackerAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AbstractAuthenticationToken sourceToken = (AbstractAuthenticationToken)authentication;
        Object principal = "TEST";
        return new UsernamePasswordAuthenticationToken(principal, authentication, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AbstractAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
