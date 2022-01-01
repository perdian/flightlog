package de.perdian.flightlog.modules.registration;

import de.perdian.flightlog.modules.registration.model.RegistrationRequest;
import de.perdian.flightlog.modules.users.persistence.UserEntity;

public interface RegistrationService {

    UserEntity registerUser(RegistrationRequest registrationRequest);

}
