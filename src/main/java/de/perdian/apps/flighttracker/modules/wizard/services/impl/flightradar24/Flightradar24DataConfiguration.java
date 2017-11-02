package de.perdian.apps.flighttracker.modules.wizard.services.impl.flightradar24;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "flighttracker.data.flightradar24")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class Flightradar24DataConfiguration {

    private boolean enabled = false;

    public boolean isEnabled() {
        return this.enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
