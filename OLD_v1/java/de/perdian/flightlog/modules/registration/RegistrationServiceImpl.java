package de.perdian.flightlog.modules.registration;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.perdian.flightlog.modules.registration.exception.RegistrationRestrictedException;
import de.perdian.flightlog.modules.registration.model.RegistrationRequest;
import de.perdian.flightlog.modules.registration.persistence.RegistrationWhitelistRepository;
import de.perdian.flightlog.modules.users.persistence.UserEntity;
import de.perdian.flightlog.modules.users.services.UsersUpdateService;

@Service
class RegistrationServiceImpl implements RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private RegistrationConfiguration registrationConfiguration = null;
    private RegistrationWhitelistRepository registrationWhitelistRepository = null;
    private UsersUpdateService usersUpdateService = null;

    @Override
    public UserEntity registerUser(RegistrationRequest registrationRequest) {

        // First we have to make sure that we are open for registrations
        // at all or otherwise we'll have to block the registration
        // attemps
        if (this.checkRegistrationAllowed(registrationRequest)) {

            log.info("Registering new user: {}", registrationRequest);
            UserEntity newUserEntity = new UserEntity();
            newUserEntity.setAuthenticationSource(registrationRequest.getAuthenticationSource());
            newUserEntity.setUserId(UUID.randomUUID());
            newUserEntity.setUsername(registrationRequest.getUsername());
            return this.getUsersUpdateService().saveUser(newUserEntity);

        } else {

            log.info("Registration blocked for new user: {}", registrationRequest);
            throw new RegistrationRestrictedException("Registration currently not open for user: " + registrationRequest.getEmail());

        }
    }

    private boolean checkRegistrationAllowed(RegistrationRequest registrationRequest) {
        if (this.getRegistrationConfiguration().isRestricted()) {
            return this.checkRegistrationAllowedByConfiguration(registrationRequest)
                || this.checkRegistrationAllowedByDatabase(registrationRequest);
        } else {
            return true;
        }
    }

    private boolean checkRegistrationAllowedByConfiguration(RegistrationRequest registrationRequest) {
        return Optional.ofNullable(this.getRegistrationConfiguration().getEmailWhitelist()).orElseGet(Collections::emptyList).stream()
            .filter(entry -> entry != null && entry.equalsIgnoreCase(registrationRequest.getEmail()))
            .findAny()
            .isPresent();
    }

    private boolean checkRegistrationAllowedByDatabase(RegistrationRequest registrationRequest) {
        long databaseRowCount = this.getRegistrationWhitelistRepository().count((root, query, builder) -> {
            return builder.equal(root.get("email"), registrationRequest.getEmail());
        });
        return databaseRowCount > 0;
    }

    RegistrationConfiguration getRegistrationConfiguration() {
        return this.registrationConfiguration;
    }
    @Autowired
    void setRegistrationConfiguration(RegistrationConfiguration registrationConfiguration) {
        this.registrationConfiguration = registrationConfiguration;
    }

    UsersUpdateService getUsersUpdateService() {
        return this.usersUpdateService;
    }
    @Autowired
    void setUsersUpdateService(UsersUpdateService usersUpdateService) {
        this.usersUpdateService = usersUpdateService;
    }

    RegistrationWhitelistRepository getRegistrationWhitelistRepository() {
        return this.registrationWhitelistRepository;
    }
    @Autowired
    void setRegistrationWhitelistRepository(RegistrationWhitelistRepository registrationWhitelistRepository) {
        this.registrationWhitelistRepository = registrationWhitelistRepository;
    }

}
