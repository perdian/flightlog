package de.perdian.flightlog.modules.authentication.exceptions;

import org.springframework.security.authentication.AccountStatusException;

public class RegistrationRestrictedException extends AccountStatusException {

    static final long serialVersionUID = 1L;

    public RegistrationRestrictedException(String message) {
        super(message);
    }

}
