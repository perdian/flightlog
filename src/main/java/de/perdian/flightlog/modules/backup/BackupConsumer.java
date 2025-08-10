package de.perdian.flightlog.modules.backup;

import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetails;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangePackage;

@FunctionalInterface
public interface BackupConsumer {

    void consumeBackupPackage(FlightsExchangePackage backupPackage, FlightlogUserDetails userDetails);

}
