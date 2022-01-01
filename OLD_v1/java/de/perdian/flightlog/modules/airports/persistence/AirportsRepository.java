package de.perdian.flightlog.modules.airports.persistence;

public interface AirportsRepository {

    AirportEntity loadAirportByIataCode(String iataAirportCode);

}
