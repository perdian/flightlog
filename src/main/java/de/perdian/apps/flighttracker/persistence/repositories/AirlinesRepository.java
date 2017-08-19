package de.perdian.apps.flighttracker.persistence.repositories;

import de.perdian.apps.flighttracker.persistence.entities.AirlineEntity;

public interface AirlinesRepository {

    AirlineEntity loadAirlineByIataCode(String iataAirlineCode);
    AirlineEntity loadAirlineByName(String airlineName);

}
