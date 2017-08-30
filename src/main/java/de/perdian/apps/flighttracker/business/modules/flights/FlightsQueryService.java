package de.perdian.apps.flighttracker.business.modules.flights;

import de.perdian.apps.flighttracker.business.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.persistence.support.PaginatedList;

public interface FlightsQueryService {

    FlightBean loadFlightById(Long id);
    PaginatedList<FlightBean> loadFlights(FlightsQuery query);

}
