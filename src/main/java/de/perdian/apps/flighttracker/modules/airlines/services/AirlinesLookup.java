package de.perdian.apps.flighttracker.modules.airlines.services;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

public interface AirlinesLookup {

    AirlineBean loadAirlineByCode(String airlineCode, UserEntity user);
    AirlineBean loadAirlineByName(String airlineName, UserEntity user);

    default void updateAirlineByCode(String airlineCode, AirlineBean airlineBean, UserEntity user) {
        // Do nothing and simply ignore the request to update the internal data
    }

    default void removeAirlineByCode(String airlineCode, UserEntity user) {
        // Do nothing and simply ignore the request to update the internal data
    }

}
