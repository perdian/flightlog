package de.perdian.flightlog.modules.overview.services;

import de.perdian.flightlog.modules.flights.services.FlightsQuery;
import de.perdian.flightlog.modules.overview.model.MapModel;

public interface MapService {

    MapModel loadMap(FlightsQuery flightsQuery);

}
