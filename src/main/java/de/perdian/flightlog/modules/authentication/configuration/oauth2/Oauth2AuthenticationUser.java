package de.perdian.flightlog.modules.authentication.configuration.oauth2;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.authentication.persistence.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;

class Oauth2AuthenticationUser extends DefaultOidcUser implements User {

    static final long serialVersionUID = 1L;

    private UserEntity entity = null;

    Oauth2AuthenticationUser(Collection<? extends GrantedAuthority> mappedAuthorities, OidcIdToken idToken, OidcUserInfo userInfo) {
        super(mappedAuthorities, idToken, userInfo);
    }

    @Override
    public String toString() {
        return "Oauth2AuthenticationUser[" + this.getEntity() + "]";
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public UserEntity getEntity() {
        return this.entity;
    }
    void setEntity(UserEntity entity) {
        this.entity = entity;
    }

}
