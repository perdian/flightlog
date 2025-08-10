package de.perdian.flightlog.modules.authentication.configuration.oauth2;

import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetails;
import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetailsResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

class Oauth2AuthenticationUserService extends OidcUserService {

    private static final Logger log = LoggerFactory.getLogger(Oauth2AuthenticationUserService.class);

    private FlightlogUserDetailsResolver flightlogUserDetailsResolver = null;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUser delegateUser = super.loadUser(userRequest);

        FlightlogUserDetails flightlogUserDetails = this.getFlightlogUserDetailsResolver().resolveUserDetails(delegateUser.getEmail(), delegateUser.getIssuer().toString());
        Oauth2AuthenticationUser resultUser = new Oauth2AuthenticationUser(delegateUser.getAuthorities(), delegateUser.getIdToken(), delegateUser.getUserInfo());
        resultUser.setUserEntity(flightlogUserDetails.getUserEntity());
        return resultUser;

    }

    FlightlogUserDetailsResolver getFlightlogUserDetailsResolver() {
        return this.flightlogUserDetailsResolver;
    }
    @Autowired
    void setFlightlogUserDetailsResolver(FlightlogUserDetailsResolver flightlogUserDetailsResolver) {
        this.flightlogUserDetailsResolver = flightlogUserDetailsResolver;
    }

}
