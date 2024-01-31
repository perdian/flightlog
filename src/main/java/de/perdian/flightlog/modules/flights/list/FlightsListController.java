package de.perdian.flightlog.modules.flights.list;

import de.perdian.flightlog.modules.authentication.UserHolder;
import de.perdian.flightlog.modules.flights.shared.service.FlightQuery;
import de.perdian.flightlog.modules.flights.shared.service.FlightQueryService;
import de.perdian.flightlog.support.pagination.PaginationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/flights")
public class FlightsListController {

    private FlightQueryService queryService = null;
    private UserHolder userHolder = null;

    @RequestMapping(path = "/list")
    String doList(Model model) {
        return this.doList(0, model);
    }

    @RequestMapping(path = "/list/{pageNumber}")
    String doList(@PathVariable("pageNumber") int pageNumber, Model model) {
        PaginationRequest paginationRequest = new PaginationRequest(pageNumber, 75);
        model.addAttribute("flights", this.getQueryService().loadFlights(new FlightQuery(this.getUserHolder().getCurrentUser()), paginationRequest));
        return "flights/list";
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
