package de.perdian.flightlog.modules.overview.services;

import de.perdian.flightlog.modules.flights.services.FlightsQuery;
import de.perdian.flightlog.modules.overview.model.OverviewBean;

public interface OverviewService {

    OverviewBean loadOverview(FlightsQuery flightsQuery);

}
