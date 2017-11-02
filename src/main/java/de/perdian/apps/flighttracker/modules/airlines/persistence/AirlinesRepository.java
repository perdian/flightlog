package de.perdian.apps.flighttracker.modules.airlines.persistence;

public interface AirlinesRepository {

    AirlineEntity loadAirlineByIataCode(String iataAirlineCode);
    AirlineEntity loadAirlineByName(String airlineName);

}
