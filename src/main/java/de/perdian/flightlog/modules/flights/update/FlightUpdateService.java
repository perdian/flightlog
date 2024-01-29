package de.perdian.flightlog.modules.flights.update;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.flights.shared.model.Flight;

public interface FlightUpdateService {

    Flight saveFlight(Flight flight, User user);
    void deleteFlight(Flight flight, User user);

}
