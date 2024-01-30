package de.perdian.flightlog.modules.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
class UserHolderImpl implements UserHolder {

    @Override
    public User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext == null ? null : securityContext.getAuthentication();
        Object principal = authentication == null ? null : authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        } else {
            throw new IllegalArgumentException("No current user available");
        }
    }

}
