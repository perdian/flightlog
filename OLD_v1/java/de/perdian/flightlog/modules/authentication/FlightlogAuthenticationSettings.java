package de.perdian.flightlog.modules.authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "flightlog.authentication")
public class FlightlogAuthenticationSettings {

    private boolean required = false;
    private FlightlogAuthenticationLocalSettings local = new FlightlogAuthenticationLocalSettings();
    private FlightlogAuthenticationLdapSettings ldap = new FlightlogAuthenticationLdapSettings();
    private FlightlogAuthenticationOauthSettings oauth = new FlightlogAuthenticationOauthSettings();

    @Component
    @ConfigurationProperties(prefix = "flightlog.authentication.local")
    public static class FlightlogAuthenticationLocalSettings {

        private boolean enabled = true;
        private String hashSeedPrefix = null;
        private String hashSeedPostfix = null;

        public boolean isEnabled() {
            return this.enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getHashSeedPrefix() {
            return this.hashSeedPrefix;
        }
        public void setHashSeedPrefix(String hashSeedPrefix) {
            this.hashSeedPrefix = hashSeedPrefix;
        }

        public String getHashSeedPostfix() {
            return this.hashSeedPostfix;
        }
        public void setHashSeedPostfix(String hashSeedPostfix) {
            this.hashSeedPostfix = hashSeedPostfix;
        }

    }

    @Component
    @ConfigurationProperties(prefix = "flightlog.authentication.ldap")
    public static class FlightlogAuthenticationLdapSettings {

        private boolean enabled = false;
        private String url = null;
        private String baseDn = null;
        private String usernameField = "uid";
        private String userDn = "";
        private String bindDn = null;
        private String bindPassword = null;

        public boolean isEnabled() {
            return this.enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

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

        public String getUsernameField() {
            return this.usernameField;
        }
        public void setUsernameField(String usernameField) {
            this.usernameField = usernameField;
        }

        public String getUserDn() {
            return this.userDn;
        }
        public void setUserDn(String userDn) {
            this.userDn = userDn;
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

    @Component
    @ConfigurationProperties(prefix = "flightlog.authentication.oauth")
    public static class FlightlogAuthenticationOauthSettings {

        private boolean enabled = false;

        public boolean isEnabled() {
            return this.enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

    }

    public boolean isRequired() {
        return this.required;
    }
    public void setRequired(boolean required) {
        this.required = required;
    }

    public FlightlogAuthenticationLocalSettings getLocal() {
        return this.local;
    }
    public void setLocal(FlightlogAuthenticationLocalSettings local) {
        this.local = local;
    }

    public FlightlogAuthenticationLdapSettings getLdap() {
        return this.ldap;
    }
    public void setLdap(FlightlogAuthenticationLdapSettings ldap) {
        this.ldap = ldap;
    }

    public FlightlogAuthenticationOauthSettings getOauth() {
        return this.oauth;
    }
    public void setOauth(FlightlogAuthenticationOauthSettings oauth) {
        this.oauth = oauth;
    }

}
