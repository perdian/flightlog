package de.perdian.apps.flighttracker.business.modules.overview;

import de.perdian.apps.flighttracker.business.modules.flights.FlightsQuery;
import de.perdian.apps.flighttracker.business.modules.overview.model.MapModel;
import de.perdian.apps.flighttracker.business.modules.overview.model.OverviewBean;

public interface OverviewService {

    OverviewBean loadOverview(FlightsQuery flightsQuery);
    MapModel loadMap(FlightsQuery flightsQuery);

}
