package de.perdian.flightlog.modules.flights.shared.service;

import de.perdian.flightlog.modules.flights.shared.model.Flight;

import java.util.List;

public interface FlightQueryService {

    List<Flight> loadFlights(FlightQuery query);

}
