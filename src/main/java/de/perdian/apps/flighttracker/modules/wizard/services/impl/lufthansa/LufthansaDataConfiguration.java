package de.perdian.apps.flighttracker.modules.wizard.services.impl.lufthansa;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "flighttracker.data.lufthansa")
@Component
class LufthansaDataConfiguration {

    private String clientId = null;
    private String clientSecret = null;

    public String getClientId() {
        return this.clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

}
