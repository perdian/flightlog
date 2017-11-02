package de.perdian.apps.flighttracker.modules.overview.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.perdian.apps.flighttracker.modules.flights.services.FlightsQuery;
import de.perdian.apps.flighttracker.modules.flights.services.FlightsQueryService;
import de.perdian.apps.flighttracker.modules.flights.services.FlightsQuery.TimePeriod;
import de.perdian.apps.flighttracker.modules.overview.model.MapModel;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewBean;
import de.perdian.apps.flighttracker.modules.overview.services.OverviewService;
import de.perdian.apps.flighttracker.modules.security.web.FlighttrackerUser;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightDistance;
import de.perdian.apps.flighttracker.support.types.FlightReason;

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
    public MapModel mapData(@AuthenticationPrincipal FlighttrackerUser authenticationPrincipal, @ModelAttribute("overviewQuery") OverviewQuery overviewQuery) {
        return this.getOverviewService().loadMap(this.createFlightsQuery(authenticationPrincipal, overviewQuery));
    }

    @ModelAttribute(name = "overview")
    public OverviewBean overview(@AuthenticationPrincipal FlighttrackerUser authenticationPrincipal, @ModelAttribute("overviewQuery") OverviewQuery overviewQuery) {
        return this.getOverviewService().loadOverview(this.createFlightsQuery(authenticationPrincipal, overviewQuery));

    }

    private FlightsQuery createFlightsQuery(FlighttrackerUser authenticationPrincipal, OverviewQuery overviewQuery) {
        FlightsQuery flightsQuery = new FlightsQuery();
        flightsQuery.setRestrictUsers(authenticationPrincipal == null ? null : authenticationPrincipal.toUserEntities());
        flightsQuery.setRestrictAirlineCodes(overviewQuery.getAirlineCode());
        flightsQuery.setRestrictAircraftTypes(overviewQuery.getAircraftType());
        flightsQuery.setRestrictAirportCodes(overviewQuery.getAirportCode());
        flightsQuery.setRestrictTimePeriods(this.computeTimePeriods(overviewQuery.getYear()));
        flightsQuery.setRestrictCabinClasses(this.computeEnumValues(CabinClass.class, overviewQuery.getCabinClass()));
        flightsQuery.setRestrictFlightReasons(this.computeEnumValues(FlightReason.class, overviewQuery.getFlightReason()));
        flightsQuery.setRestrictFlightDistances(this.computeEnumValues(FlightDistance.class, overviewQuery.getFlightDistance()));
        return flightsQuery;
    }

    private List<TimePeriod> computeTimePeriods(Collection<String> yearValues) {
        List<FlightsQuery.TimePeriod> timePeriods = new ArrayList<>();
        if (yearValues != null && !yearValues.isEmpty()) {
            for (String yearValue : yearValues) {
                if (yearValue != null && !yearValue.isEmpty()) {
                    int year = Integer.parseInt(yearValue);
                    TimePeriod timePeriod = new TimePeriod();
                    timePeriod.setMinimumDepartureDateLocal(LocalDate.of(year, 1, 1));
                    timePeriod.setMaximumArrivalDateLocal(LocalDate.of(year, 12, 31));
                    timePeriods.add(timePeriod);
                }
            }
        }
        return timePeriods.isEmpty() ? null : timePeriods;
    }

    private <E extends Enum<E>> Collection<E> computeEnumValues(Class<E> enumClass, Collection<String> stringValues) {
        Set<E> activeEnums = EnumSet.noneOf(enumClass);
        for (E enumValue : enumClass.getEnumConstants()) {
            if (stringValues != null && stringValues.contains(enumValue.name())) {
                activeEnums.add(enumValue);
            }
        }
        return activeEnums.isEmpty() ? null : activeEnums;
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
