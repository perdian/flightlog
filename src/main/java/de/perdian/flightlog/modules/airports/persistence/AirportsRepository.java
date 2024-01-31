package de.perdian.flightlog.modules.airports.persistence;

import de.perdian.flightlog.modules.airports.model.Airport;

public interface AirportsRepository {

    Airport loadAirportByIataCode(String iataAirportCode);

}
