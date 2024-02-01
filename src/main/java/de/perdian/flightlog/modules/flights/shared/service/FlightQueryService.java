package de.perdian.flightlog.modules.flights.shared.service;

import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.support.pagination.PaginatedList;
import de.perdian.flightlog.support.pagination.PaginationRequest;

import java.util.List;

public interface FlightQueryService {

    default List<Flight> loadFlights(FlightQuery query) {
        return this.loadFlightsPaginated(query, null).getItems();
    }

    PaginatedList<Flight> loadFlightsPaginated(FlightQuery query, PaginationRequest paginationRequest);

}
