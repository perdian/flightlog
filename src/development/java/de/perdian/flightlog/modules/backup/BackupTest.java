package de.perdian.flightlog.modules.backup;

import de.perdian.flightlog.FlightlogApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class BackupTest {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(FlightlogApplication.class);
        try (ConfigurableApplicationContext applicationContext = springApplication.run(args)) {
            BackupExecutor backupExecutor = applicationContext.getBean(BackupExecutor.class);
            backupExecutor.executeBackup();
        }
        System.exit(0);
    }

}
