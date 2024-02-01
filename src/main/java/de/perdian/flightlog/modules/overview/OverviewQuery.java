package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.flights.shared.service.FlightQuery;

public class OverviewQuery {

    public FlightQuery toFlightQuery(User user) {
        FlightQuery flightQuery = new FlightQuery(user);
        return flightQuery;
    }

}
