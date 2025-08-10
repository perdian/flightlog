package de.perdian.flightlog.modules.flights.update;

import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetails;
import de.perdian.flightlog.modules.flights.shared.model.Flight;

public interface FlightUpdateService {

    Flight saveFlight(Flight flight, FlightlogUserDetails user);
    void deleteFlight(Flight flight, FlightlogUserDetails user);

}
