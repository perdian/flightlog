package de.perdian.apps.flighttracker.business.modules.overview;

import de.perdian.apps.flighttracker.business.modules.overview.model.OverviewBean;

public interface OverviewService {

    OverviewBean loadOverview(OverviewQuery query);

}
