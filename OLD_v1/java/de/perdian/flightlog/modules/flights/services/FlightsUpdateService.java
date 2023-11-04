package de.perdian.flightlog.modules.flights.services;

import de.perdian.flightlog.modules.flights.editor.FlightBean;
import de.perdian.flightlog.modules.users.persistence.UserEntity;

public interface FlightsUpdateService {

    FlightBean saveFlight(FlightBean flightBean, UserEntity user);
    void deleteFlight(FlightBean flightBean);

}
