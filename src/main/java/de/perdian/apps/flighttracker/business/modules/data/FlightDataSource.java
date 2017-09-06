package de.perdian.apps.flighttracker.business.modules.data;

import java.time.LocalDate;

public interface FlightDataSource {

    FlightData lookupFlightData(String airlineCode, String flightNumber, LocalDate departureDate);

}
