package de.perdian.apps.flighttracker.web.modules.flights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import de.perdian.apps.flighttracker.business.modules.flights.FlightsQuery;
import de.perdian.apps.flighttracker.business.modules.flights.FlightsQueryService;

@Controller
public class FlightsListController {

    private FlightsQueryService flightsQueryService = null;
    private MessageSource messageSource = null;

    @RequestMapping(value = "/flights/list")
    public String doList(Model model) {
        return this.doList(0, model);
    }

    @RequestMapping(value = "/flights/list/{page}")
    public String doList(@PathVariable("page") int page, Model model) {

        FlightsQuery flightsQuery = new FlightsQuery();
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
