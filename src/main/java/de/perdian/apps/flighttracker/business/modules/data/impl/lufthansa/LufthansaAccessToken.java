package de.perdian.apps.flighttracker.business.modules.data.impl.lufthansa;

import java.time.Instant;

class LufthansaAccessToken {

    private String value = null;
    private Instant expirationTime = null;

    String getValue() {
        return this.value;
    }
    void setValue(String value) {
        this.value = value;
    }

    Instant getExpirationTime() {
        return this.expirationTime;
    }
    void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

}
