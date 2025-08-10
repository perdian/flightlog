package de.perdian.flightlog.modules.authentication.service.userdetails;

import de.perdian.flightlog.modules.authentication.persistence.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public interface FlightlogUserDetails extends UserDetails {

    UserEntity getUserEntity();

    @Override
    default String getPassword() {
        return null;
    }

    @Override
    default Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptySet();
    }

}
