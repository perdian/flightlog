package de.perdian.flightlog.modules.authentication.service.userdetails;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
class FlightlogUserDetailsHolderImpl implements FlightlogUserDetailsHolder {

    @Override
    public FlightlogUserDetails getCurrentUserDetails() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext == null ? null : securityContext.getAuthentication();
        Object principal = authentication == null ? null : authentication.getPrincipal();
        if (principal instanceof FlightlogUserDetails userDetails) {
            return userDetails;
        } else {
            throw new IllegalArgumentException("No current user available");
        }
    }

}
