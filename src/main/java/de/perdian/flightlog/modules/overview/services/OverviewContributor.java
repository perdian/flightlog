package de.perdian.flightlog.modules.overview.services;

import java.util.List;

import de.perdian.flightlog.modules.flights.model.FlightBean;
import de.perdian.flightlog.modules.overview.model.OverviewBean;
import de.perdian.flightlog.modules.users.persistence.UserEntity;

public interface OverviewContributor {

    void contributeTo(OverviewBean overviewBean, List<FlightBean> flights, UserEntity user);

}
