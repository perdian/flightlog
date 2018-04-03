package de.perdian.flightlog.modules.airlines.services;

import java.util.List;

import de.perdian.flightlog.modules.airlines.model.AirlineBean;
import de.perdian.flightlog.modules.users.persistence.UserEntity;

public interface AirlinesService {

    AirlineBean loadAirlineByCode(String airlineCode, UserEntity user);
    AirlineBean loadAirlineByName(String airlineName, UserEntity user);

    List<AirlineBean> loadUserSpecificAirlines(UserEntity user);
    AirlineBean updateUserSpecificAirline(UserEntity user, AirlineBean airline);
    void deleteUserSpecificAirline(UserEntity user, AirlineBean airline);

}
