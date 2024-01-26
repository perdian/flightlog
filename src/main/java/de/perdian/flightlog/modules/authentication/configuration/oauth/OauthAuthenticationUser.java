package de.perdian.flightlog.modules.authentication.configuration.oauth;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.authentication.persistence.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;
import java.util.HashSet;

class OauthAuthenticationUser extends DefaultOidcUser implements User {

    static final long serialVersionUID = 1L;

    private UserEntity entity = null;
    private String information = null;

    OauthAuthenticationUser(Collection<? extends GrantedAuthority> mappedAuthorities, OidcIdToken idToken, OidcUserInfo userInfo) {
        super(new HashSet<>(mappedAuthorities), idToken, userInfo);
    }

    @Override
    public UserEntity getEntity() {
        return this.entity;
    }
    void setEntity(UserEntity entity) {
        this.entity = entity;
    }

    @Override
    public String getInformation() {
        return this.information;
    }
    void setInformation(String information) {
        this.information = information;
    }

}
