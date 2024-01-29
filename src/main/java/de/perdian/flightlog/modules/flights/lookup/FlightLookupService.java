package de.perdian.flightlog.modules.flights.lookup;

import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.model.FlightLookupRequest;

import java.util.List;

public interface FlightLookupService {

    List<Flight> lookupFlights(FlightLookupRequest flightLookupRequest);

}
