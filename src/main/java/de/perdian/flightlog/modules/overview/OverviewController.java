package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.authentication.UserHolder;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.service.FlightQuery;
import de.perdian.flightlog.modules.flights.shared.service.FlightQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
class OverviewController {

    static final String MODEL_ATTRIBUTE_FILTERED_FLIGHTS = "filteredFlights";
    static final String MODEL_ATTRIBUTE_ALL_FLIGHTS = "allFlights";

    private UserHolder userHolder = null;
    private FlightQueryService flightQueryService = null;

    @RequestMapping({ "/", "/overview" })
    String doOverview(Model model) {
        return "overview";
    }

    @ModelAttribute(name = MODEL_ATTRIBUTE_FILTERED_FLIGHTS, binding = false)
    List<Flight> filteredFlights(@ModelAttribute("overviewQuery") FlightQuery filteredFlightsQuery) {
        FlightQuery flightQuery = filteredFlightsQuery.clone().withUser(this.getUserHolder().getCurrentUser());
        return this.getFlightQueryService().loadFlights(flightQuery);
    }

    @ModelAttribute(name = MODEL_ATTRIBUTE_ALL_FLIGHTS, binding = false)
    List<Flight> allFlights() {
        return this.getFlightQueryService().loadFlights(new FlightQuery().withUser(this.getUserHolder().getCurrentUser()));
    }

    @ModelAttribute(name = "overviewQuery")
    FlightQuery overviewQuery() {
        return new FlightQuery();
    }

    UserHolder getUserHolder() {
        return this.userHolder;
    }
    @Autowired
    void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

    FlightQueryService getFlightQueryService() {
        return this.flightQueryService;
    }
    @Autowired
    void setFlightQueryService(FlightQueryService flightQueryService) {
        this.flightQueryService = flightQueryService;
    }

}
