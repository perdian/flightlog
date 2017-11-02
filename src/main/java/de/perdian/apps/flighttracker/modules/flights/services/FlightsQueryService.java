package de.perdian.apps.flighttracker.modules.flights.services;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.support.persistence.PaginatedList;

public interface FlightsQueryService {

    FlightBean loadFlightById(Long id);
    PaginatedList<FlightBean> loadFlights(FlightsQuery query);

}
