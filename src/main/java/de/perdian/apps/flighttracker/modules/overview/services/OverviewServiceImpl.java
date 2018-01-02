package de.perdian.apps.flighttracker.modules.overview.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.flights.services.FlightsQuery;
import de.perdian.apps.flighttracker.modules.flights.services.FlightsQueryService;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewBean;

@Service
class OverviewServiceImpl implements OverviewService {

    private FlightsQueryService flightsQueryService = null;
    private List<OverviewContributor> contributors = null;

    @Override
    public OverviewBean loadOverview(FlightsQuery flightsQuery) {
        List<FlightBean> flights = this.getFlightsQueryService().loadFlights(flightsQuery).getItems();
        OverviewBean overview = new OverviewBean();
        this.getContributors().forEach(contributor -> contributor.contributeTo(overview, flights, flightsQuery.getRestrictUser()));
        return overview;
    }

    FlightsQueryService getFlightsQueryService() {
        return this.flightsQueryService;
    }
    @Autowired
    void setFlightsQueryService(FlightsQueryService flightsQueryService) {
        this.flightsQueryService = flightsQueryService;
    }

    List<OverviewContributor> getContributors() {
        return this.contributors;
    }
    @Autowired
    void setContributors(List<OverviewContributor> contributors) {
        this.contributors = contributors;
    }

}
