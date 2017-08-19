package de.perdian.apps.flighttracker.persistence.repositories;

import de.perdian.apps.flighttracker.persistence.entities.AirportEntity;

public interface AirportsRepository {

    AirportEntity loadAirportByIataCode(String iataAirportCode);

}
