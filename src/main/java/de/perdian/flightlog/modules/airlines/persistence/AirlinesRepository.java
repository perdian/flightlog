package de.perdian.flightlog.modules.airlines.persistence;

import de.perdian.flightlog.modules.airlines.model.Airline;

public interface AirlinesRepository {

    Airline loadAirlineByCode(String airlineCode);
    Airline loadAirlineByName(String airlineName);

}
