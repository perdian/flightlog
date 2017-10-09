package de.perdian.apps.flighttracker.background.backup;

import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import de.perdian.apps.flighttracker.business.modules.importexport.ImportExportService;
import de.perdian.apps.flighttracker.business.modules.importexport.data.DataItem;
import de.perdian.apps.flighttracker.business.modules.importexport.data.impl.XmlDataWriter;
import de.perdian.apps.flighttracker.business.modules.users.UsersQuery;
import de.perdian.apps.flighttracker.business.modules.users.UsersQueryService;
import de.perdian.apps.flighttracker.persistence.entities.UserEntity;

@Service
class BackupService {

    private static final Logger log = LoggerFactory.getLogger(BackupService.class);

    private BackupConfiguration backupConfiguration = null;
    private TaskScheduler taskScheduler = null;
    private UsersQueryService usersQueryService = null;
    private ImportExportService importExportService = null;
    private Clock clock = Clock.systemUTC();

    @PostConstruct
    void initialize() {
        if (StringUtils.isEmpty(this.getBackupConfiguration().getCron())) {
            log.info("No backup cron setting defined. No automatic backup will be performed");
        } else if (this.getBackupConfiguration().getTarget() == null) {
            log.info("No backup target directory defined. No automatic backup will be performed");
        } else {
            log.info("Scheduling backup into target directory '{}' for cron '{}'", this.getBackupConfiguration().getTarget(), this.getBackupConfiguration().getCron());
            this.getTaskScheduler().schedule(() -> this.executeBackup(this.getBackupConfiguration().getTarget()), new CronTrigger(this.getBackupConfiguration().getCron(), TimeZone.getTimeZone(ZoneId.systemDefault())));
        }
    }

    void executeBackup(Path target) {
        try {

            DateTimeFormatter targetDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(ZoneId.of("UTC"));
            StringBuilder targetFileName = new StringBuilder();
            targetFileName.append("flighttracker-backup-").append(targetDateFormatter.format(this.getClock().instant())).append(".zip");
            Path targetFile = target.resolve(targetFileName.toString());
            log.info("Executing backup into target file: {}", targetFile);

            Path targetDirectory = targetFile.getParent();
            if (!Files.exists(targetDirectory)) {
                log.info("Creating backup target directory: {}", targetDirectory);
                Files.createDirectories(targetDirectory);
            }

            List<UserEntity> userEntities = this.getUsersQueryService().loadUsers(new UsersQuery());
            log.debug("Processing {} users into backup archive", userEntities.size());

            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(targetFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE)))) {
                zipOutputStream.setLevel(9);
                this.executeBackupForUser(null, zipOutputStream);
                for (UserEntity userEntity : userEntities) {
                    this.executeBackupForUser(userEntity, zipOutputStream);
                }
                zipOutputStream.flush();
            }
            log.info("Completed creation of backup archive at: {}", targetFile);

        } catch (Exception e) {
            log.warn("Cannot execute backup", e);
        }
    }

    private void executeBackupForUser(UserEntity userEntity, ZipOutputStream zipOutputStream) throws Exception {
        List<DataItem> dataItemsForUser = this.getImportExportService().exportDataItems(userEntity);
        if (dataItemsForUser == null || dataItemsForUser.isEmpty()) {
            log.debug("No entries found for user '{}'. Will not create backup entry for that user.", userEntity);
        } else {

            log.debug("Found {} entries for user '{}'", dataItemsForUser.size(), userEntity);
            String dataEntryDirectoryName = "user-" + (userEntity == null ? "undefined" : userEntity.getUserId());

            if (userEntity != null) {
                zipOutputStream.putNextEntry(new ZipEntry(dataEntryDirectoryName + "/user.xml"));
                DocumentFactory documentFactory = DocumentFactory.getInstance();
                Element userRootElement = documentFactory.createElement("user");
                userRootElement.addElement("authenticationSource").setText(String.valueOf(userEntity.getAuthenticationSource()));
                userRootElement.addElement("userId").setText(String.valueOf(userEntity.getUserId()));
                userRootElement.addElement("username").setText(String.valueOf(userEntity.getUsername()));
                XMLWriter xmlWriter = new XMLWriter(new OutputStreamWriter(zipOutputStream, "UTF-8"), OutputFormat.createPrettyPrint());
                xmlWriter.write(documentFactory.createDocument(userRootElement));
                xmlWriter.flush();
            }

            XmlDataWriter dataWriter = new XmlDataWriter();
            zipOutputStream.putNextEntry(new ZipEntry(dataEntryDirectoryName + "/flights.xml"));
            zipOutputStream.write(dataWriter.writeDataItems(dataItemsForUser).getBytes("UTF-8"));

        }
    }

    BackupConfiguration getBackupConfiguration() {
        return this.backupConfiguration;
    }
    @Autowired
    void setBackupConfiguration(BackupConfiguration backupConfiguration) {
        this.backupConfiguration = backupConfiguration;
    }

    TaskScheduler getTaskScheduler() {
        return this.taskScheduler;
    }
    @Autowired
    void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    UsersQueryService getUsersQueryService() {
        return this.usersQueryService;
    }
    @Autowired
    void setUsersQueryService(UsersQueryService usersQueryService) {
        this.usersQueryService = usersQueryService;
    }

    ImportExportService getImportExportService() {
        return this.importExportService;
    }
    @Autowired
    void setImportExportService(ImportExportService importExportService) {
        this.importExportService = importExportService;
    }

    Clock getClock() {
        return this.clock;
    }
    void setClock(Clock clock) {
        this.clock = clock;
    }

}
