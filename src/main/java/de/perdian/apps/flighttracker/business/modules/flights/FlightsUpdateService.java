package de.perdian.apps.flighttracker.business.modules.flights;

import de.perdian.apps.flighttracker.business.modules.flights.model.FlightBean;

public interface FlightsUpdateService {

    FlightBean saveFlight(FlightBean flightBean);
    void deleteFlight(FlightBean flightBean);

}
