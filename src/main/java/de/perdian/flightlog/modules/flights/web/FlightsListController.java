package de.perdian.flightlog.modules.flights.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import de.perdian.flightlog.modules.flights.services.FlightsQuery;
import de.perdian.flightlog.modules.flights.services.FlightsQueryService;
import de.perdian.flightlog.modules.security.web.FlightlogUser;

@Controller
public class FlightsListController {

    private FlightsQueryService flightsQueryService = null;
    private MessageSource messageSource = null;

    @RequestMapping(value = "/flights/list")
    public String doList(@AuthenticationPrincipal FlightlogUser user, Model model) {
        return this.doList(user, 0, model);
    }

    @RequestMapping(value = "/flights/list/{page}")
    public String doList(@AuthenticationPrincipal FlightlogUser user, @PathVariable("page") int page, Model model) {

        FlightsQuery flightsQuery = new FlightsQuery();
        flightsQuery.setRestrictUser(user == null ? null : user.getUserEntity());
        flightsQuery.setPageSize(Integer.valueOf(100));
        flightsQuery.setPage(page);

        model.addAttribute("flightsQuery", flightsQuery);
        model.addAttribute("flights", this.getFlightsQueryService().loadFlights(flightsQuery));
        return "/flights/list";

    }

    FlightsQueryService getFlightsQueryService() {
        return this.flightsQueryService;
    }
    @Autowired
    void setFlightsQueryService(FlightsQueryService flightsQueryService) {
        this.flightsQueryService = flightsQueryService;
    }

    MessageSource getMessageSource() {
        return this.messageSource;
    }
    @Autowired
    void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

}
