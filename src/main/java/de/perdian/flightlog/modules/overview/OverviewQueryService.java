package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;

import java.util.List;

public interface OverviewQueryService {

    List<Flight> loadFlights(OverviewQuery overviewQuery);

}
