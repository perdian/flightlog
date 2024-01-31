package de.perdian.flightlog.modules.flights.lookup;

import de.perdian.flightlog.modules.flights.shared.model.Flight;

import java.util.List;

public interface FlightLookupSource {

    List<Flight> lookupFlights(FlightLookupRequest flightLookupRequest);

}
