package de.perdian.flightlog.modules.backup;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "flightlog.backup")
public class BackupConfiguration {

    private String cron = null;
    private Path target = null;

    public String getCron() {
        return this.cron;
    }
    public void setCron(String cron) {
        this.cron = cron;
    }

    public Path getTarget() {
        return this.target;
    }
    public void setTarget(Path target) {
        this.target = target;
    }

}
