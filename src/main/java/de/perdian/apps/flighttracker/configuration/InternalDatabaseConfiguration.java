package de.perdian.apps.flighttracker.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "flighttracker.authentication.internaldatabase")
public class InternalDatabaseConfiguration {

    private String hashSeedPrefix = null;
    private String hashSeedPostfix = null;

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
