package de.perdian.apps.flighttracker.modules.airports.persistence;

public interface AirportsRepository {

    AirportEntity loadAirportByIataCode(String iataAirportCode);

}
