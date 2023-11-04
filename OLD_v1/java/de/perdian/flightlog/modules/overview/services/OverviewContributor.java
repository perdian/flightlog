package de.perdian.flightlog.modules.overview.services;

import java.util.List;

import de.perdian.flightlog.modules.flights.editor.FlightBean;
import de.perdian.flightlog.modules.overview.model.OverviewBean;

public interface OverviewContributor {

    void contributeTo(OverviewBean overviewBean, List<FlightBean> flights);

}
