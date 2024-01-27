package de.perdian.flightlog.modules.authentication.configuration.fixed;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

class FixedAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    private FixedAuthenticationUser user = null;

    FixedAuthenticationFilter(FixedAuthenticationUser user) {
        this.setUser(user);
        this.setAuthenticationManager(new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                authentication.setAuthenticated(true);
                return authentication;
            }
        });
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return this.getUser();
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return this.getUser().getUsername();
    }

    public FixedAuthenticationUser getUser() {
        return this.user;
    }
    public void setUser(FixedAuthenticationUser user) {
        this.user = user;
    }

}
