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

    @RequestMapping(path = "/")
    String doIndex(Model model) {
        return "redirect:/flights/list";
    }

    @RequestMapping(path = "/list/all")
    String doListAll(FlightQuery flightQuery, Model model) {
        return this.doList(flightQuery, new PaginationRequest(0, Integer.MAX_VALUE), model);
    }

    @RequestMapping(path = "/list")
    String doListForPageZero(FlightQuery flightQuery, Model model) {
        return this.doListForPage(0, flightQuery, model);
    }

    @RequestMapping(path = "/list/{pageNumber}")
    String doListForPage(@PathVariable int pageNumber, FlightQuery flightQuery, Model model) {
        PaginationRequest paginationRequest = new PaginationRequest(pageNumber, 75);
        return this.doList(flightQuery, paginationRequest, model);
    }

    private String doList(FlightQuery flightQuery, PaginationRequest paginationRequest, Model model) {
        FlightQuery userFlightQuery = flightQuery.clone().withUser(this.getUserHolder().getCurrentUser());
        model.addAttribute("flights", this.getQueryService().loadFlightsPaginated(userFlightQuery, paginationRequest));
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
