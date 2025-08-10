package de.perdian.flightlog.modules.backup.impl;

import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetails;
import de.perdian.flightlog.modules.backup.BackupConsumer;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangePackage;
import de.perdian.flightlog.modules.flights.exchange.impl.XmlExchangeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@ConditionalOnExpression("T(org.apache.commons.lang3.StringUtils).isNotEmpty('${flightlog.backup.filesystem.directory:}')")
class FileSystemBackupConsumer implements BackupConsumer {

    private static final Logger log = LoggerFactory.getLogger(FileSystemBackupConsumer.class);

    private Path directory = null;
    private XmlExchangeHandler xmlExchangeHandler = new XmlExchangeHandler();

    @Override
    public void consumeBackupPackage(FlightsExchangePackage backupPackage, FlightlogUserDetails userDetails) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmssX");
        String targetFileDate = dateTimeFormatter.format(backupPackage.getCreationTime().atZone(ZoneId.of("UTC")));
        StringBuilder targetFileName = new StringBuilder();
        targetFileName.append("flightlog-backup-").append(userDetails.getUsername());
        targetFileName.append("-").append(targetFileDate).append(".xml");
        Path targetFile = this.getDirectory().resolve(targetFileName.toString());

        if (!Files.exists(targetFile.getParent())) {
            try {
                log.trace("Creating parent directory for backups at: {}", targetFile.getParent().toUri());
                Files.createDirectories(targetFile.getParent());
            } catch (Exception e) {
                log.trace("Cannot create parent directory for backups at: {}", targetFile.getParent().toUri(), e);
            }
        }

        log.debug("Writing backup into file '{}' for user {}", targetFile.toUri(), userDetails);
        try (OutputStream targetFileStream = Files.newOutputStream(targetFile)) {
            this.getXmlExchangeHandler().exportPackage(backupPackage, targetFileStream);
            targetFileStream.flush();
        } catch (IOException e) {
            log.error("Cannot write backup into file '{}' for user {}", targetFile.toUri(), userDetails, e);
        }

    }

    Path getDirectory() {
        return this.directory;
    }
    @Value("${flightlog.backup.filesystem.directory:}")
    void setDirectory(Path directory) {
        this.directory = directory;
    }

    XmlExchangeHandler getXmlExchangeHandler() {
        return this.xmlExchangeHandler;
    }
    void setXmlExchangeHandler(XmlExchangeHandler xmlExchangeHandler) {
        this.xmlExchangeHandler = xmlExchangeHandler;
    }

}
