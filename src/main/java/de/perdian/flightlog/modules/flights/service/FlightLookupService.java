package de.perdian.flightlog.modules.flights.service;

import de.perdian.flightlog.modules.flights.service.model.Flight;
import de.perdian.flightlog.modules.flights.service.model.FlightLookupRequest;

import java.util.List;

public interface FlightLookupService {

    List<Flight> lookupFlights(FlightLookupRequest flightLookupRequest);

}
