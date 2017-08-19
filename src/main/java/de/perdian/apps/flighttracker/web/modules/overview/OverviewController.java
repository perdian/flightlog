package de.perdian.apps.flighttracker.web.modules.overview;

import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import de.perdian.apps.flighttracker.business.modules.flights.FlightsQueryService;
import de.perdian.apps.flighttracker.business.modules.overview.OverviewQuery;
import de.perdian.apps.flighttracker.business.modules.overview.OverviewService;
import de.perdian.apps.flighttracker.business.modules.overview.model.OverviewBean;

@Controller
public class OverviewController {

    private OverviewService overviewService = null;
    private FlightsQueryService flightsQueryService = null;

    @RequestMapping(value = "/")
    public String overview() {
        return "/overview/overview";
    }

    @ModelAttribute(name = "overview")
    public OverviewBean overview(@ModelAttribute("overviewQuery") OverviewQuery overviewQuery) {
        return this.getOverviewService().loadOverview(overviewQuery);
    }

    @ModelAttribute(name = "overviewQuery")
    public OverviewQuery overviewQuery() {
        OverviewQuery overviewQuery = new OverviewQuery();
        return overviewQuery;
    }

    @ModelAttribute(name = "overviewQueryHelper")
    public OverviewQueryHelper overviewQueryHelper() {

        TreeSet<Integer> availableYears = new TreeSet<>((i1, i2) -> Integer.compare(i2, i1));
        availableYears.addAll(this.getFlightsQueryService().loadActiveYears());

        OverviewQueryHelper queryHelper = new OverviewQueryHelper();
        queryHelper.setAvailableYears(availableYears);
        return queryHelper;

    }

    OverviewService getOverviewService() {
        return this.overviewService;
    }
    @Autowired
    void setOverviewService(OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    FlightsQueryService getFlightsQueryService() {
        return this.flightsQueryService;
    }
    @Autowired
    void setFlightsQueryService(FlightsQueryService flightsQueryService) {
        this.flightsQueryService = flightsQueryService;
    }

}
