package de.perdian.flightlog.modules.authentication.service.userdetails;

import de.perdian.flightlog.modules.authentication.exceptions.RegistrationRestrictedException;
import de.perdian.flightlog.modules.authentication.persistence.UserEntity;
import de.perdian.flightlog.modules.authentication.persistence.UserRepository;
import de.perdian.flightlog.modules.authentication.service.registration.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
class FlightlogUserDetailsResolverImpl implements FlightlogUserDetailsResolver {

    private static final Logger log = LoggerFactory.getLogger(FlightlogUserDetailsResolverImpl.class);

    private UserRepository userRepository = null;
    private RegistrationService registrationService = null;

    @Override
    public FlightlogUserDetails resolveUserDetails(String username, String authenticationSource) {
        UserEntity userEntity = this.resolveUserEntity(username, authenticationSource);
        return new FlightlogUserDetailsImpl(username, userEntity);
    }

    private UserEntity resolveUserEntity(String username, String authenticationSource) {
        Specification<UserEntity> entitySpecification = this.createUserEntitySpecification(username, authenticationSource);
        UserEntity entity = this.getUserRepository().findOne(entitySpecification).orElse(null);
        if (entity != null) {
            log.debug("Found user '{}' in database (ID: {})", username, entity.getUserId());
        } else {
            if (this.getRegistrationService().checkUsernameRegistrationAllowed(username)) {
                entity = new UserEntity();
                entity.setUsername(username);
                entity.setAuthenticationSource(authenticationSource);
                entity = this.getUserRepository().save(entity);
                log.debug("Created user '{}' in database (ID: {})", username, entity.getUserId());
            } else {
                log.debug("Bocked user '{}' from database creation", username);
                throw new RegistrationRestrictedException("Registration restricted");
            }
        }
        return entity;
    }

    private Specification<UserEntity> createUserEntitySpecification(String username, String authenticationSource) {
        return (root, _, criteriaBuilder) -> criteriaBuilder.and(
            criteriaBuilder.equal(root.get("username"), username),
            criteriaBuilder.equal(root.get("authenticationSource"), authenticationSource)
        );
    }

    UserRepository getUserRepository() {
        return this.userRepository;
    }
    @Autowired
    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    RegistrationService getRegistrationService() {
        return this.registrationService;
    }
    @Autowired
    void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

}
