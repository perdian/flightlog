package de.perdian.apps.flighttracker.modules.airlines.services;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;

public interface AirlinesService {

    AirlineBean loadAirlineByIataCode(String iataAirlineCode);
    AirlineBean loadAirlineByName(String airlineName);

}
