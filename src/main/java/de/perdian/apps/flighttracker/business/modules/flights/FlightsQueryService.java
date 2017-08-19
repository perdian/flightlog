package de.perdian.apps.flighttracker.business.modules.flights;

import java.util.Collection;

import de.perdian.apps.flighttracker.business.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.persistence.support.PaginatedList;

public interface FlightsQueryService {

    FlightBean loadFlightById(Long id);
    PaginatedList<FlightBean> loadFlights(FlightsQuery query);
    Collection<Integer> loadActiveYears();

}
