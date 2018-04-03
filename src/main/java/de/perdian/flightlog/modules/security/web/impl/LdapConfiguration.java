package de.perdian.flightlog.modules.security.web.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "flightlog.authentication.ldap")
public class LdapConfiguration {

    private String url = null;
    private String baseDn = null;
    private String usernameField = "uid";
    private String userDn = "";
    private String bindDn = null;
    private String bindPassword = null;

    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getBaseDn() {
        return this.baseDn;
    }
    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    public String getUserDn() {
        return this.userDn;
    }
    public void setUserDn(String userDn) {
        this.userDn = userDn;
    }

    public String getUsernameField() {
        return this.usernameField;
    }
    public void setUsernameField(String usernameField) {
        this.usernameField = usernameField;
    }

    public String getBindDn() {
        return this.bindDn;
    }
    public void setBindDn(String bindDn) {
        this.bindDn = bindDn;
    }

    public String getBindPassword() {
        return this.bindPassword;
    }
    public void setBindPassword(String bindPassword) {
        this.bindPassword = bindPassword;
    }

}
