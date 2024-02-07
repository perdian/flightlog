package de.perdian.flightlog.modules.backup;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
@ConditionalOnExpression("T(org.apache.commons.lang3.StringUtils).isNotEmpty('${flightlog.backup.cron:}')")
public class BackupConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BackupConfiguration.class);

    private String cron = null;
    private boolean onlyForNewContent = true;
    private TaskScheduler taskScheduler = null;

    @PostConstruct
    void initialize() {
        log.info("Scheduling regular backups for cron pattern: ", this.getCron());
        this.getTaskScheduler().schedule(() -> this.backupExecutor().executeBackup(),new CronTrigger(this.getCron()));
    }

    @Bean
    BackupExecutor backupExecutor() {
        BackupExecutor backupExecutor = new BackupExecutor();
        backupExecutor.setOnlyForNewContent(this.isOnlyForNewContent());
        return backupExecutor;
    }

    String getCron() {
        return this.cron;
    }
    @Value("${flightlog.backup.cron}")
    void setCron(String cron) {
        this.cron = cron;
    }

    boolean isOnlyForNewContent() {
        return this.onlyForNewContent;
    }
    @Value("${flightlog.backup.onlyForNewContent:true}")
    void setOnlyForNewContent(boolean onlyForNewContent) {
        this.onlyForNewContent = onlyForNewContent;
    }

    TaskScheduler getTaskScheduler() {
        return this.taskScheduler;
    }
    @Autowired
    void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

}
