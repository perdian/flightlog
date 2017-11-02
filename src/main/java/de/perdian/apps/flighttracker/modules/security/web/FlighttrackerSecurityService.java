package de.perdian.apps.flighttracker.modules.security.web;

import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public abstract class FlighttrackerSecurityService implements AuthenticationProvider, UserDetailsService {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AbstractAuthenticationToken sourceToken = (AbstractAuthenticationToken)authentication;
        FlighttrackerUser user = this.authenticateUser(sourceToken.getName(), (String)sourceToken.getCredentials());
        return new UsernamePasswordAuthenticationToken(user, authentication, null);
    }

    protected abstract FlighttrackerUser authenticateUser(String username, String password) throws AuthenticationException;

    @Override
    public boolean supports(Class<?> authentication) {
        return AbstractAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, "password", Collections.emptyList());
    }

}
