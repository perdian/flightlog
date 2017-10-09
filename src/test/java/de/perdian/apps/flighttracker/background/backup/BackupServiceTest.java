package de.perdian.apps.flighttracker.background.backup;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.google.common.jimfs.Jimfs;

import de.perdian.apps.flighttracker.business.modules.importexport.ImportExportService;
import de.perdian.apps.flighttracker.business.modules.importexport.data.DataItem;
import de.perdian.apps.flighttracker.business.modules.users.UsersQueryService;
import de.perdian.apps.flighttracker.persistence.entities.UserEntity;

public class BackupServiceTest {

    @Test
    public void initialize() throws IOException {
        try (FileSystem dummyFileSystem = Jimfs.newFileSystem()) {

            Path dummyPath = dummyFileSystem.getPath("dummy");

            TaskScheduler taskScheduler = Mockito.mock(TaskScheduler.class);
            BackupConfiguration backupConfiguration = new BackupConfiguration();
            backupConfiguration.setCron("0 0 5 * * 1");
            backupConfiguration.setTarget(dummyPath);

            BackupService backupService = new BackupService();
            backupService.setBackupConfiguration(backupConfiguration);
            backupService.setTaskScheduler(taskScheduler);
            backupService.initialize();

            ArgumentCaptor<CronTrigger> triggerCaptor = ArgumentCaptor.forClass(CronTrigger.class);
            Mockito.verify(taskScheduler).schedule(Mockito.any(), triggerCaptor.capture());
            Assert.assertEquals("0 0 5 * * 1", triggerCaptor.getValue().getExpression());

        }
    }

    @Test
    public void initializeNoTargetFile() {

        TaskScheduler taskScheduler = Mockito.mock(TaskScheduler.class);
        BackupConfiguration backupConfiguration = new BackupConfiguration();
        backupConfiguration.setCron("0 0 5 * * 1");
        backupConfiguration.setTarget(null);

        BackupService backupService = new BackupService();
        backupService.setBackupConfiguration(backupConfiguration);
        backupService.setTaskScheduler(taskScheduler);
        backupService.initialize();

        Mockito.verifyNoMoreInteractions(taskScheduler);

    }

    @Test
    public void initializeNoCronExpression() throws IOException {
        try (FileSystem dummyFileSystem = Jimfs.newFileSystem()) {

            Path dummyPath = dummyFileSystem.getPath("dummy");

            TaskScheduler taskScheduler = Mockito.mock(TaskScheduler.class);
            BackupConfiguration backupConfiguration = new BackupConfiguration();
            backupConfiguration.setCron(null);
            backupConfiguration.setTarget(dummyPath);

            BackupService backupService = new BackupService();
            backupService.setBackupConfiguration(backupConfiguration);
            backupService.setTaskScheduler(taskScheduler);
            backupService.initialize();

            Mockito.verifyNoMoreInteractions(taskScheduler);

        }
    }

    @Test
    public void executeBackup() throws IOException {

        UserEntity userA = new UserEntity();
        userA.setUserId(Long.valueOf("1"));
        UserEntity userB = new UserEntity();
        userB.setUserId(Long.valueOf("2"));
        UsersQueryService usersQueryService = Mockito.mock(UsersQueryService.class);
        Mockito.when(usersQueryService.loadUsers(Mockito.any())).thenReturn(Arrays.asList(userA, userB));

        DataItem dataItemB1 = new DataItem();
        DataItem dataItemB2 = new DataItem();
        DataItem dataItemNull = new DataItem();
        ImportExportService importExportService = Mockito.mock(ImportExportService.class);
        Mockito.when(importExportService.exportDataItems(Mockito.eq(userB))).thenReturn(Arrays.asList(dataItemB1));
        Mockito.when(importExportService.exportDataItems(Mockito.eq(userB))).thenReturn(Arrays.asList(dataItemB2));
        Mockito.when(importExportService.exportDataItems(Mockito.eq(null))).thenReturn(Arrays.asList(dataItemNull));

        try (FileSystem dummyFileSystem = Jimfs.newFileSystem()) {

            Path dummyPath = dummyFileSystem.getPath("dummy");

            Clock clock = Mockito.mock(Clock.class);
            Mockito.when(clock.instant()).thenReturn(Instant.parse("2017-10-09T10:15:30.00Z"));
            BackupService backupService = new BackupService();
            backupService.setClock(clock);
            backupService.setImportExportService(importExportService);
            backupService.setUsersQueryService(usersQueryService);
            backupService.executeBackup(dummyPath);

            Path backupFilePath = dummyPath.resolve("flighttracker-backup-20171009-101530.zip");
            Assert.assertTrue(Files.exists(backupFilePath));
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(backupFilePath))) {
                List<String> zipEntryNames = new ArrayList<>();
                for (ZipEntry zipEntry = zipInputStream.getNextEntry(); zipEntry != null; zipEntry = zipInputStream.getNextEntry()) {
                    zipEntryNames.add(zipEntry.getName());
                }
                Assert.assertEquals(3, zipEntryNames.size());
                Assert.assertThat(zipEntryNames, Matchers.hasItem("user-undefined/flights.xml"));
                Assert.assertThat(zipEntryNames, Matchers.hasItem("user-2/user.xml"));
                Assert.assertThat(zipEntryNames, Matchers.hasItem("user-2/flights.xml"));
            }

        }
    }


}
