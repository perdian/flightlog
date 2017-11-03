package de.perdian.apps.flighttracker.modules.overview.services;

import de.perdian.apps.flighttracker.modules.flights.services.FlightsQuery;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewBean;

public interface OverviewService {

    OverviewBean loadOverview(FlightsQuery flightsQuery);

}
