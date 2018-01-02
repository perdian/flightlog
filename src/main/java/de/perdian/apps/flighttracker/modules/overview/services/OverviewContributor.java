package de.perdian.apps.flighttracker.modules.overview.services;

import java.util.List;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewBean;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

public interface OverviewContributor {

    void contributeTo(OverviewBean overviewBean, List<FlightBean> flights, UserEntity user);

}
