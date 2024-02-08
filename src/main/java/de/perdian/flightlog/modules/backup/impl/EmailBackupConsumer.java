package de.perdian.flightlog.modules.backup.impl;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.backup.BackupConsumer;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangeFormat;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangePackage;
import jakarta.activation.DataSource;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@ConditionalOnProperty(value = "flightlog.backup.email.enabled", havingValue = "true")
public class EmailBackupConsumer implements BackupConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailBackupConsumer.class);

    private static DateTimeFormatter EMAIL_SUBJECT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss z");
    private static DateTimeFormatter EMAIL_ATTACHMENT_FILENAME_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmssX");

    private String from = null;
    private String to = null;
    private JavaMailSender mailSender = null;
    private FlightsExchangeFormat backupFormat = FlightsExchangeFormat.XML;

    @Override
    public void consumeBackupPackage(FlightsExchangePackage backupPackage, User user) {
        if (this.getMailSender() == null) {
            log.error("No MailSender available to consume email backup package for user: {}", user);
        } else if (StringUtils.isEmpty(this.getFrom())) {
            log.error("No email 'from' address configured' to send email backup package for user: {}", user);
        } else {

            ZonedDateTime backupTime = backupPackage.getCreationTime().atZone(ZoneId.of("UTC"));
            String emailFrom = this.getFrom();
            String emailTo = StringUtils.defaultIfEmpty(this.getTo(), user.getEntity().getEmail());
            StringBuilder emailSubject = new StringBuilder();
            emailSubject.append("[Flightlog] Flights Backup [").append(EMAIL_SUBJECT_DATE_FORMATTER.format(backupTime)).append("]");

            StringBuilder attachmentFilename = new StringBuilder();
            attachmentFilename.append("flightlog-backup-").append(user.getUsername()).append("-");
            attachmentFilename.append(EMAIL_ATTACHMENT_FILENAME_DATE_FORMATTER.format(backupTime)).append(".").append(this.getBackupFormat().name().toLowerCase());

            try {

                log.info("Sending email backup package with {} flights to address '{}' for user: {}", backupPackage.getFlights().size(), emailTo, user);
                DataSource attachmentDataSource = this.getBackupFormat().toDataSource(backupPackage);
                MimeMessage emailMessage = this.getMailSender().createMimeMessage();
                MimeMessageHelper emailMessageHelper = new MimeMessageHelper(emailMessage, true);
                emailMessageHelper.setFrom(emailFrom);
                emailMessageHelper.setTo(emailTo);
                emailMessageHelper.setSubject(emailSubject.toString());
                emailMessageHelper.setText(this.createEmailBody(backupPackage, user), true);
                emailMessageHelper.addAttachment(attachmentFilename.toString(), attachmentDataSource);
                this.getMailSender().send(emailMessage);

            } catch (Exception e) {
                log.error("Cannot send email backup package to address '{}' for user: ", emailTo, user, e);
            }

        }
    }

    private String createEmailBody(FlightsExchangePackage backupPackage, User user) {
        return """
                <html>
                  <head>
                    <style type="text/css">
                    </style>
                  <body>
                    <div class="title">
                      Flight log backup
                    </div>
                  </body>
                </html>
               """;
    }

    String getFrom() {
        return this.from;
    }
    @Value("${flightlog.backup.email.from:}")
    void setFrom(String from) {
        this.from = from;
    }

    String getTo() {
        return this.to;
    }
    @Value("${flightlog.backup.email.to:}")
    public void setTo(String to) {
        this.to = to;
    }

    JavaMailSender getMailSender() {
        return this.mailSender;
    }
    @Autowired(required = false)
    void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    FlightsExchangeFormat getBackupFormat() {
        return this.backupFormat;
    }
    void setBackupFormat(FlightsExchangeFormat backupFormat) {
        this.backupFormat = backupFormat;
    }

}
