package de.perdian.flightlog.modules.airlines.persistence;

public interface AirlinesRepository {

    AirlineEntity loadAirlineByCode(String airlineCode);
    AirlineEntity loadAirlineByName(String airlineName);

}
