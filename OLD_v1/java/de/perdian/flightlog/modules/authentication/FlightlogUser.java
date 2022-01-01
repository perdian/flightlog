package de.perdian.flightlog.modules.authentication;

import java.io.Serializable;

import org.springframework.security.core.AuthenticatedPrincipal;

import de.perdian.flightlog.modules.users.persistence.UserEntity;

public interface FlightlogUser extends AuthenticatedPrincipal, Serializable {

    UserEntity getUserEntity();

    String getInformation();

}
