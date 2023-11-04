package de.perdian.flightlog.modules.flights.service;

import de.perdian.flightlog.modules.flights.service.model.FlightLookup;
import de.perdian.flightlog.modules.flights.service.model.FlightLookupRequest;

import java.util.List;

public interface FlightLookupSource {

    List<FlightLookup> lookupFlights(FlightLookupRequest flightLookupRequest);

}
