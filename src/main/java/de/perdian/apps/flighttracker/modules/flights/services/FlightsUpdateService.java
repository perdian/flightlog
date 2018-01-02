package de.perdian.apps.flighttracker.modules.flights.services;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

public interface FlightsUpdateService {

    FlightBean saveFlight(FlightBean flightBean, UserEntity user);
    void deleteFlight(FlightBean flightBean);

}
