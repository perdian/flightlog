package de.perdian.flightlog.modules.authentication.oauth2;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import de.perdian.flightlog.modules.authentication.FlightlogUser;
import de.perdian.flightlog.modules.users.persistence.UserEntity;

class OAuth2FlightlogUser extends DefaultOidcUser implements FlightlogUser {

    static final long serialVersionUID = 1L;

    private UserEntity userEntity = null;
    private String information = null;

    OAuth2FlightlogUser(Collection<? extends GrantedAuthority> mappedAuthorities, OidcIdToken idToken, OidcUserInfo userInfo) {
        super(new HashSet<>(mappedAuthorities), idToken, userInfo);
    }

    @Override
    public UserEntity getUserEntity() {
        return this.userEntity;
    }
    void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public String getInformation() {
        return this.information;
    }
    void setInformation(String information) {
        this.information = information;
    }

}