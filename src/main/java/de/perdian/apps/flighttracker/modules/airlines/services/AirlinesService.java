package de.perdian.apps.flighttracker.modules.airlines.services;

import java.util.List;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

public interface AirlinesService {

    AirlineBean loadAirlineByCode(String airlineCode, UserEntity user);
    AirlineBean loadAirlineByName(String airlineName, UserEntity user);

    List<AirlineBean> loadUserSpecificAirlines(UserEntity user);
    AirlineBean updateUserSpecificAirline(UserEntity user, AirlineBean airline);
    void deleteUserSpecificAirline(UserEntity user, AirlineBean airline);

}
