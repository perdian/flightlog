package de.perdian.flightlog.modules.flights.services;

import java.util.UUID;

import de.perdian.flightlog.modules.flights.editor.FlightBean;
import de.perdian.flightlog.modules.users.persistence.UserEntity;
import de.perdian.flightlog.support.persistence.PaginatedList;

public interface FlightsQueryService {

    FlightBean loadFlightById(UUID id, UserEntity user);
    PaginatedList<FlightBean> loadFlights(FlightsQuery query);

}
