package de.perdian.apps.flighttracker.modules.flights.services;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;

public interface FlightsUpdateService {

    FlightBean saveFlight(FlightBean flightBean);
    void deleteFlight(FlightBean flightBean);

}
