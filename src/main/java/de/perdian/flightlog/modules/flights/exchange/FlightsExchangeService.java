package de.perdian.flightlog.modules.flights.exchange;

import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetails;

import java.util.List;

public interface FlightsExchangeService {

    List<FlightsExchangePackageFlight> importPackage(FlightsExchangePackage exchangePackage, FlightlogUserDetails targetUserDetails);

    FlightsExchangePackage createPackage(FlightlogUserDetails sourceUserDetails);

}
