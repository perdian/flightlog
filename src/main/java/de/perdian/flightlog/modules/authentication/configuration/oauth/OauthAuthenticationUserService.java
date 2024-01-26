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
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

class OauthAuthenticationUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private static final Logger log = LoggerFactory.getLogger(OauthAuthenticationUserService.class);

    private OidcUserService delegateUserService = new OidcUserService();
    private UserRepository userRepository = null;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUser delegateUser = this.getDelegateUserService().loadUser(userRequest);
        String delegateAuthenticationSource = delegateUser.getIssuer().toString();

        Specification<UserEntity> entitySpecification = (root, query, criteriaBuilder) -> criteriaBuilder.and(
            criteriaBuilder.equal(root.get("authenticationSource"), delegateAuthenticationSource),
            criteriaBuilder.equal(root.get("username"), delegateUser.getEmail())
        );
        UserEntity entity = this.getUserRepository().findOne(entitySpecification).orElse(null);
        if (entity == null) {
            if (this.checkUserAllowedToRegister(delegateUser)) {
                entity = new UserEntity();
                entity.setUsername(delegateUser.getEmail());
                entity.setAuthenticationSource(delegateAuthenticationSource);
                entity = this.getUserRepository().save(entity);
                log.debug("Created user '{}' in database (ID: {})", delegateUser.getEmail(), entity.getUserId());
            } else {
                log.debug("Bocked user '{}' from database creation", delegateUser.getEmail());
                throw new RegistrationRestrictedException("Registration restricted");
            }
        } else {
            log.debug("Found user '{}' in database (ID: {})", delegateUser.getEmail(), entity.getUserId());
        }

        OauthAuthenticationUser resultUser = new OauthAuthenticationUser(delegateUser.getAuthorities(), delegateUser.getIdToken(), delegateUser.getUserInfo());
        resultUser.setEntity(entity);
        resultUser.setInformation(delegateUser.getEmail());
        return resultUser;

    }

    private boolean checkUserAllowedToRegister(OidcUser delegateUser) {
        return true;
    }

    OidcUserService getDelegateUserService() {
        return this.delegateUserService;
    }
    void setDelegateUserService(OidcUserService delegateUserService) {
        this.delegateUserService = delegateUserService;
    }

    UserRepository getUserRepository() {
        return this.userRepository;
    }
    @Autowired
    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
