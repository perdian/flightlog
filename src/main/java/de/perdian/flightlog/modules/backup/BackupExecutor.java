package de.perdian.flightlog.modules.backup;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.authentication.persistence.UserEntity;
import de.perdian.flightlog.modules.authentication.persistence.UserRepository;
import de.perdian.flightlog.modules.backup.persistence.BackupEntity;
import de.perdian.flightlog.modules.backup.persistence.BackupRepository;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangePackage;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangePackageFlight;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

class BackupExecutor {

    private static final Logger log = LoggerFactory.getLogger(BackupExecutor.class);

    private boolean onlyForNewContent = true;
    private UserRepository userRepository = null;
    private BackupRepository backupRepository = null;
    private FlightsExchangeService flightsExchangeService = null;
    private List<BackupConsumer> backupConsumers = Collections.emptyList();

    void executeBackup() {
        List<UserEntity> allUsers = this.getUserRepository().findAll();
        log.debug("Creating backups for {} users", allUsers.size());
        allUsers.forEach(userEntity -> this.executeBackupForUser(userEntity));
    }

    private void executeBackupForUser(UserEntity userEntity) {

        User backupUser = new BackupUser(userEntity);
        FlightsExchangePackage exchangePackage = this.getFlightsExchangeService().createPackage(backupUser);
        List<FlightsExchangePackageFlight> exchangeFlights = exchangePackage.getFlights();
        if (exchangeFlights == null || exchangeFlights.isEmpty()) {
            log.debug("No flights that could be backed up for user: {}", backupUser);
        } else {

            Instant latestFlightUpdateInstant = exchangePackage.getFlights().stream()
                .map(FlightsExchangePackageFlight::getLastUpdatedAt)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);

            Specification<BackupEntity> latestBackupSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), userEntity);
            Sort latestBackupSort = Sort.by(Sort.Order.desc("latestFlightUpdateInstant"));
            Page<BackupEntity> latestBackupEntities = this.getBackupRepository().findAll(latestBackupSpecification, PageRequest.of(0, 1, latestBackupSort));
            BackupEntity latestBackupEntity = latestBackupEntities.isEmpty() ? null : latestBackupEntities.getContent().getFirst();
            Instant latestBackupInstant = latestBackupEntity == null ? null : latestBackupEntity.getLatestFlightUpdateInstant();

            if (this.isOnlyForNewContent() && latestBackupInstant != null && latestFlightUpdateInstant != null && !latestFlightUpdateInstant.isAfter(latestBackupInstant)) {
                log.debug("Backup for user {} is not necessary as no flights have been updated since last backup ({})", backupUser, latestBackupInstant);
            } else {

                log.debug("Backing up {} flights for user: {} [{} consumers]", exchangeFlights.size(), backupUser, this.getBackupConsumers().size());
                for (BackupConsumer backupConsumer : this.getBackupConsumers()) {
                    backupConsumer.consumeBackupPackage(exchangePackage, backupUser);
                }

                BackupEntity newBackupEntity = new BackupEntity();
                newBackupEntity.setBackupInstant(Instant.now());
                newBackupEntity.setLatestFlightUpdateInstant(latestFlightUpdateInstant);
                newBackupEntity.setUser(userEntity);
                this.getBackupRepository().save(newBackupEntity);

            }

        }

    }

    private static class BackupUser implements User {

        private UserEntity entity = null;

        BackupUser(UserEntity entity) {
            this.setEntity(entity);
        }

        @Override
        public String toString() {
            return "BackupUser[" + this.getEntity() + "]";
        }

        @Override
        public String getUsername() {
            return this.getEntity().getUsername();
        }

        @Override
        public UserEntity getEntity() {
            return this.entity;
        }
        private void setEntity(UserEntity entity) {
            this.entity = entity;
        }

    }

    boolean isOnlyForNewContent() {
        return this.onlyForNewContent;
    }
    void setOnlyForNewContent(boolean onlyForNewContent) {
        this.onlyForNewContent = onlyForNewContent;
    }

    UserRepository getUserRepository() {
        return this.userRepository;
    }
    @Autowired
    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    BackupRepository getBackupRepository() {
        return this.backupRepository;
    }
    @Autowired
    void setBackupRepository(BackupRepository backupRepository) {
        this.backupRepository = backupRepository;
    }

    FlightsExchangeService getFlightsExchangeService() {
        return this.flightsExchangeService;
    }
    @Autowired
    void setFlightsExchangeService(FlightsExchangeService flightsExchangeService) {
        this.flightsExchangeService = flightsExchangeService;
    }

    List<BackupConsumer> getBackupConsumers() {
        return this.backupConsumers;
    }
    @Autowired(required = false)
    void setBackupConsumers(List<BackupConsumer> backupConsumers) {
        this.backupConsumers = backupConsumers;
    }

}
