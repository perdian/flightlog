package de.perdian.apps.flighttracker.modules.overview.services;

import java.util.List;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewBean;

public interface OverviewContributor {

    void contributeTo(OverviewBean overviewBean, List<FlightBean> flights);

}
