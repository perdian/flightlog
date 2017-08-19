package de.perdian.apps.flighttracker.business.modules.overview;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.perdian.apps.flighttracker.business.modules.flights.FlightsQuery;
import de.perdian.apps.flighttracker.business.modules.flights.FlightsQueryService;
import de.perdian.apps.flighttracker.business.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.business.modules.overview.model.OverviewBean;

@Service
class OverviewServiceImpl implements OverviewService {

    private List<OverviewContributor> contributors = null;
    private FlightsQueryService flightsQueryService = null;

    @Override
    public OverviewBean loadOverview(OverviewQuery overviewQuery) {

        List<FlightBean> flights = this.loadFlights(overviewQuery);

        OverviewBean overview = new OverviewBean();
        this.getContributors().forEach(contributor -> contributor.contribute(overview, flights));
        return overview;

    }

    private List<FlightBean> loadFlights(OverviewQuery overviewQuery) {
        FlightsQuery flightsQuery = new FlightsQuery();
        if (overviewQuery.getYear() != null) {
            flightsQuery.setMinimumDepartureDateLocal(LocalDate.of(overviewQuery.getYear(), 1, 1));
            flightsQuery.setMaximumArrivalDateLocal(LocalDate.of(overviewQuery.getYear(), 12, 31));
        }
        return this.getFlightsQueryService().loadFlights(flightsQuery).getItems();
    }

    public FlightsQueryService getFlightsQueryService() {
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
