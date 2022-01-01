package de.perdian.flightlog.modules.authentication.local;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.perdian.flightlog.modules.users.persistence.UserEntity;

public class LocalAuthenticationProvider implements AuthenticationProvider {

    private List<LocalAuthenticationProviderDelegate> delegates = Collections.emptyList();

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        for (LocalAuthenticationProviderDelegate delegate : this.getDelegates()) {
            UserEntity userEntityFromDelegate = delegate.resolveUser(authentication.getName(), authentication.getCredentials() == null ? null : authentication.getCredentials().toString());
            if (userEntityFromDelegate != null) {
                LocalAuthenticationFlightlogUser responseUser = new LocalAuthenticationFlightlogUser();
                responseUser.setUserEntity(userEntityFromDelegate);
                responseUser.setName(authentication.getName());
                responseUser.setInformation(userEntityFromDelegate.getUsername());
                return new UsernamePasswordAuthenticationToken(responseUser, authentication, null);
            }
        }
        throw new UsernameNotFoundException("Cannot find user for username: " + authentication.getName());
    }

    List<LocalAuthenticationProviderDelegate> getDelegates() {
        return this.delegates;
    }
    @Autowired(required = false)
    void setDelegates(List<LocalAuthenticationProviderDelegate> delegates) {
        this.delegates = delegates;
    }

}
