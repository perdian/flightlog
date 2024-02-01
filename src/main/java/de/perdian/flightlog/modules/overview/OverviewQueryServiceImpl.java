package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.authentication.UserHolder;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.service.FlightQuery;
import de.perdian.flightlog.modules.flights.shared.service.FlightQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class OverviewQueryServiceImpl implements OverviewQueryService {

    private FlightQueryService flightQueryService = null;
    private UserHolder userHolder = null;

    @Override
    public List<Flight> loadFlights(OverviewQuery overviewQuery) {
        FlightQuery flightQuery = overviewQuery.toFlightQuery(this.getUserHolder().getCurrentUser());
        return this.getFlightQueryService().loadFlights(flightQuery, null).getItems();
    }

    FlightQueryService getFlightQueryService() {
        return this.flightQueryService;
    }
    @Autowired
    void setFlightQueryService(FlightQueryService flightQueryService) {
        this.flightQueryService = flightQueryService;
    }

    UserHolder getUserHolder() {
        return this.userHolder;
    }
    @Autowired
    void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

}
