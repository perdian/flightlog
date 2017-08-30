package de.perdian.apps.flighttracker.web.modules.overview;

import java.time.LocalDate;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.perdian.apps.flighttracker.business.modules.flights.FlightsQuery;
import de.perdian.apps.flighttracker.business.modules.flights.FlightsQueryService;
import de.perdian.apps.flighttracker.business.modules.overview.OverviewService;
import de.perdian.apps.flighttracker.business.modules.overview.model.MapModel;
import de.perdian.apps.flighttracker.business.modules.overview.model.OverviewBean;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.web.security.FlighttrackerUser;

@Controller
public class OverviewController {

    private OverviewService overviewService = null;
    private FlightsQueryService flightsQueryService = null;

    @RequestMapping(value = "/")
    public String overview() {
        return "/overview/overview";
    }

    @RequestMapping(value = "/map/data", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public MapModel mapData(@AuthenticationPrincipal FlighttrackerUser user, @ModelAttribute("overviewQuery") OverviewQuery overviewQuery) {
        return this.getOverviewService().loadMap(this.createFlightsQuery(user, overviewQuery));
    }

    @ModelAttribute(name = "overview")
    public OverviewBean overview(@AuthenticationPrincipal FlighttrackerUser authenticationPrincipal, @ModelAttribute("overviewQuery") OverviewQuery overviewQuery) {
        return this.getOverviewService().loadOverview(this.createFlightsQuery(authenticationPrincipal, overviewQuery));

    }

    private FlightsQuery createFlightsQuery(FlighttrackerUser authenticationPrincipal, OverviewQuery overviewQuery) {
        FlightsQuery flightsQuery = new FlightsQuery();
        flightsQuery.setRestrictUsers(authenticationPrincipal == null ? null : authenticationPrincipal.toUserEntities());
        flightsQuery.setRestrictAirlineCodes(StringUtils.isEmpty(overviewQuery.getAirlineCode()) || ".".equalsIgnoreCase(overviewQuery.getAirlineCode()) ? null : Collections.singleton(overviewQuery.getAirlineCode()));
        flightsQuery.setRestrictAircraftTypes(StringUtils.isEmpty(overviewQuery.getAircraftType()) || ".".equalsIgnoreCase(overviewQuery.getAircraftType()) ? null : Collections.singleton(overviewQuery.getAircraftType()));
        flightsQuery.setRestrictAirportCodes(StringUtils.isEmpty(overviewQuery.getAirportCode()) || ".".equalsIgnoreCase(overviewQuery.getAirportCode()) ? null : Collections.singleton(overviewQuery.getAirportCode()));
        flightsQuery.setRestrictCabinClasses(StringUtils.isEmpty(overviewQuery.getCabinClass()) || ".".equalsIgnoreCase(overviewQuery.getCabinClass()) ? null : Collections.singleton(CabinClass.valueOf(overviewQuery.getCabinClass())));
        flightsQuery.setRestrictFlightReasons(StringUtils.isEmpty(overviewQuery.getFlightReason()) || ".".equalsIgnoreCase(overviewQuery.getFlightReason()) ? null : Collections.singleton(FlightReason.valueOf(overviewQuery.getFlightReason())));
        if (overviewQuery.getYear() != null && !Integer.valueOf(0).equals(overviewQuery.getYear())) {
            flightsQuery.setMinimumDepartureDateLocal(LocalDate.of(overviewQuery.getYear(), 1, 1));
            flightsQuery.setMaximumArrivalDateLocal(LocalDate.of(overviewQuery.getYear(), 12, 31));
        }
        return flightsQuery;
    }

    @ModelAttribute(name = "overviewQuery")
    public OverviewQuery overviewQuery() {
        return new OverviewQuery();
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
