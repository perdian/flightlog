package de.perdian.flightlog.modules.flights.list;

import de.perdian.flightlog.modules.authentication.UserHolder;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.service.FlightQuery;
import de.perdian.flightlog.modules.flights.shared.service.FlightQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/flights")
public class FlightsListController {

    private FlightQueryService queryService = null;
    private UserHolder userHolder = null;

    @RequestMapping(path = "/list")
    String doList() {
        return "flights/list";
    }

    @ModelAttribute("flights")
    List<Flight> flights() {
        return this.getQueryService().loadFlights(new FlightQuery(this.getUserHolder().getCurrentUser()));
    }

    FlightQueryService getQueryService() {
        return this.queryService;
    }
    @Autowired
    void setQueryService(FlightQueryService queryService) {
        this.queryService = queryService;
    }

    UserHolder getUserHolder() {
        return this.userHolder;
    }
    @Autowired
    void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

}
