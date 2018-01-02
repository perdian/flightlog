package de.perdian.apps.flighttracker.modules.flights.services;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;
import de.perdian.apps.flighttracker.support.persistence.PaginatedList;

public interface FlightsQueryService {

    FlightBean loadFlightById(Long id, UserEntity user);
    PaginatedList<FlightBean> loadFlights(FlightsQuery query);

}
