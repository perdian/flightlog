package de.perdian.flightlog.modules.overview.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.perdian.flightlog.modules.authentication.FlightlogUser;
import de.perdian.flightlog.modules.flights.editor.FlightBean;
import de.perdian.flightlog.modules.flights.services.FlightsQuery;
import de.perdian.flightlog.modules.flights.services.FlightsQuery.TimePeriod;
import de.perdian.flightlog.modules.flights.services.FlightsQueryService;
import de.perdian.flightlog.modules.overview.model.MapModel;
import de.perdian.flightlog.modules.overview.model.OverviewBean;
import de.perdian.flightlog.modules.overview.services.MapService;
import de.perdian.flightlog.modules.overview.services.OverviewService;
import de.perdian.flightlog.support.persistence.PaginatedList;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightDistance;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.FlightType;

@Controller
public class OverviewController {

    private OverviewService overviewService = null;
    private MapService mapService = null;
    private FlightsQueryService flightsQueryService = null;

    @RequestMapping(value = "/")
    public String overview() {
        return "/overview/overview";
    }

    @RequestMapping(value = "/overview/map", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public MapModel mapData(FlightlogUser user, @ModelAttribute("overviewQuery") OverviewQuery overviewQuery) {
        return this.getMapService().loadMap(this.createFlightsQuery(user, overviewQuery));
    }

    @RequestMapping(value = "/overview/flights")
    public String flights(FlightlogUser user, OverviewQuery overviewQuery, Model model) {
        PaginatedList<FlightBean> flights = this.getFlightsQueryService().loadFlights(this.createFlightsQuery(user, overviewQuery));
        model.addAttribute("flights", flights);
        return "/overview/flights";
    }

    @ModelAttribute(name = "overview")
    public OverviewBean overview(FlightlogUser user, @ModelAttribute("overviewQuery") OverviewQuery overviewQuery) {
        return this.getOverviewService().loadOverview(this.createFlightsQuery(user, overviewQuery));
    }

    private FlightsQuery createFlightsQuery(FlightlogUser user, OverviewQuery overviewQuery) {
        FlightsQuery flightsQuery = new FlightsQuery();
        flightsQuery.setRestrictUser(user == null ? null : user.getUserEntity());
        flightsQuery.setRestrictAirlineCodes(overviewQuery.getAirlineCode());
        flightsQuery.setRestrictAircraftTypes(overviewQuery.getAircraftType());
        flightsQuery.setRestrictAirportCodes(overviewQuery.getAirportCode());
        flightsQuery.setRestrictTimePeriods(this.computeTimePeriods(overviewQuery.getYear()));
        flightsQuery.setRestrictCabinClasses(this.computeEnumValues(CabinClass.class, overviewQuery.getCabinClass()));
        flightsQuery.setRestrictFlightReasons(this.computeEnumValues(FlightReason.class, overviewQuery.getFlightReason()));
        flightsQuery.setRestrictFlightDistances(this.computeEnumValues(FlightDistance.class, overviewQuery.getFlightDistance()));
        flightsQuery.setRestrictFlightTypes(this.computeEnumValues(FlightType.class, overviewQuery.getFlightType()));
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

    MapService getMapService() {
        return this.mapService;
    }
    @Autowired
    void setMapService(MapService mapService) {
        this.mapService = mapService;
    }

    FlightsQueryService getFlightsQueryService() {
        return this.flightsQueryService;
    }
    @Autowired
    void setFlightsQueryService(FlightsQueryService flightsQueryService) {
        this.flightsQueryService = flightsQueryService;
    }

}
