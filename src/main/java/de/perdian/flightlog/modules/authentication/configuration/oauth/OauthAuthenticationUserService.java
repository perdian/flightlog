package de.perdian.flightlog.modules.authentication.configuration.oauth;

import de.perdian.flightlog.modules.authentication.exceptions.RegistrationRestrictedException;
import de.perdian.flightlog.modules.authentication.persistence.UserEntity;
import de.perdian.flightlog.modules.authentication.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

class OauthAuthenticationUserService extends OidcUserService {

    private static final Logger log = LoggerFactory.getLogger(OauthAuthenticationUserService.class);

    private UserRepository userRepository = null;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUser delegateUser = super.loadUser(userRequest);

        OauthAuthenticationUser resultUser = new OauthAuthenticationUser(delegateUser.getAuthorities(), delegateUser.getIdToken(), delegateUser.getUserInfo());
        resultUser.setEntity(this.ensureUserEntity(delegateUser));
        return resultUser;

    }

    private UserEntity ensureUserEntity(OidcUser oidcUser) {

        Specification<UserEntity> entitySpecification = (root, query, criteriaBuilder) -> criteriaBuilder.and(
            criteriaBuilder.equal(root.get("authenticationSource"), oidcUser.getIssuer().toString()),
            criteriaBuilder.equal(root.get("username"), oidcUser.getEmail())
        );

        UserEntity entity = this.getUserRepository().findOne(entitySpecification).orElse(null);
        if (entity == null) {
            if (this.checkUserAllowedToRegister(oidcUser)) {
                entity = new UserEntity();
                entity.setUsername(oidcUser.getEmail());
                entity.setAuthenticationSource(oidcUser.getIssuer().toString());
                entity = this.getUserRepository().save(entity);
                log.debug("Created user '{}' in database (ID: {})", oidcUser.getEmail(), entity.getUserId());
            } else {
                log.debug("Bocked user '{}' from database creation", oidcUser.getEmail());
                throw new RegistrationRestrictedException("Registration restricted");
            }
        } else {
            log.debug("Found user '{}' in database (ID: {})", oidcUser.getEmail(), entity.getUserId());
        }
        return entity;

    }

    private boolean checkUserAllowedToRegister(OidcUser oidcUser) {
        return true;
    }

    UserRepository getUserRepository() {
        return this.userRepository;
    }
    @Autowired
    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
