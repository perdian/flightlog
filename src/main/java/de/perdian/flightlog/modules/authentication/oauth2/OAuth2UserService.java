package de.perdian.flightlog.modules.authentication.oauth2;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import de.perdian.flightlog.modules.registration.RegistrationService;
import de.perdian.flightlog.modules.registration.exception.RegistrationRestrictedException;
import de.perdian.flightlog.modules.registration.model.RegistrationRequest;
import de.perdian.flightlog.modules.users.persistence.UserEntity;
import de.perdian.flightlog.modules.users.services.UsersQuery;
import de.perdian.flightlog.modules.users.services.UsersQueryService;

public class OAuth2UserService implements org.springframework.security.oauth2.client.userinfo.OAuth2UserService<OidcUserRequest, OidcUser> {

    private OidcUserService delegate = new OidcUserService();
    private UsersQueryService usersQueryService = null;
    private RegistrationService registrationService = null;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUser delegateUser = this.getDelegate().loadUser(userRequest);

        String userAuthenticationSource = delegateUser.getIssuer().toString();
        String username = delegateUser.getName();

        StringBuilder flightlogUserInformation = new StringBuilder();
        flightlogUserInformation.append(delegateUser.getEmail());
        OAuth2FlightlogUser flightlogUser = new OAuth2FlightlogUser(delegateUser.getAuthorities(), delegateUser.getIdToken(), delegateUser.getUserInfo());
        flightlogUser.setInformation(flightlogUserInformation.toString());

        UsersQuery usersQuery = new UsersQuery();
        usersQuery.setRestrictAuthenticationSources(Collections.singleton(userAuthenticationSource));
        usersQuery.setRestrictUsernames(Collections.singleton(username));
        List<UserEntity> userEntities = this.getUsersQueryService().loadUsers(usersQuery);
        if (userEntities == null || userEntities.isEmpty()) {
            RegistrationRequest registrationRequest = new RegistrationRequest();
            registrationRequest.setAuthenticationSource(userAuthenticationSource);
            registrationRequest.setUsername(username);
            registrationRequest.setEmail(delegateUser.getEmail());
            UserEntity registeredUser = this.getRegistrationService().registerUser(registrationRequest);
            if (registeredUser == null) {
                throw new RegistrationRestrictedException("Registration restricted");
            } else {
                flightlogUser.setUserEntity(registeredUser);
            }
        } else {
            flightlogUser.setUserEntity(userEntities.get(0));
        }
        return flightlogUser;

    }

    OidcUserService getDelegate() {
        return this.delegate;
    }
    void setDelegate(OidcUserService delegate) {
        this.delegate = delegate;
    }

    UsersQueryService getUsersQueryService() {
        return this.usersQueryService;
    }
    @Autowired
    void setUsersQueryService(UsersQueryService usersQueryService) {
        this.usersQueryService = usersQueryService;
    }

    RegistrationService getRegistrationService() {
        return this.registrationService;
    }
    @Autowired
    void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

}
