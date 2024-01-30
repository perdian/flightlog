package de.perdian.flightlog.modules.flights.exchange;

import de.perdian.flightlog.modules.authentication.User;

import java.util.List;

public interface FlightsExchangeService {

    List<FlightsExchangePackageFlight> importPackage(FlightsExchangePackage exchangePackage, User targetUser);

    FlightsExchangePackage createPackage(User sourceUser);

}
