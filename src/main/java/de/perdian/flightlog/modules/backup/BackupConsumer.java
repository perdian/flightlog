package de.perdian.flightlog.modules.backup;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangePackage;

@FunctionalInterface
public interface BackupConsumer {

    void consumeBackupPackage(FlightsExchangePackage backupPackage, User user);

}
