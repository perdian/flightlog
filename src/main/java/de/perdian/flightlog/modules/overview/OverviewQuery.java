package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.flights.shared.service.FlightQuery;

public class OverviewQuery {

    private User user = null;

    public FlightQuery toFlightQuery() {
        FlightQuery flightQuery = new FlightQuery();
        return flightQuery;
    }

    public User getUser() {
        return this.user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
