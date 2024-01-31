package de.perdian.flightlog.modules.flights.shared.service;

import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.support.pagination.PaginatedList;
import de.perdian.flightlog.support.pagination.PaginationRequest;

public interface FlightQueryService {

    PaginatedList<Flight> loadFlights(FlightQuery query, PaginationRequest paginationRequest);

}
