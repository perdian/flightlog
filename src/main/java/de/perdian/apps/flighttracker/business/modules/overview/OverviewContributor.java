package de.perdian.apps.flighttracker.business.modules.overview;

import java.util.List;

import de.perdian.apps.flighttracker.business.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.business.modules.overview.model.OverviewBean;

public interface OverviewContributor {

    void contribute(OverviewBean overview, List<FlightBean> flights);

}
