package de.perdian.apps.flighttracker.web.modules.overview;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import de.perdian.apps.flighttracker.business.modules.flights.FlightsQuery;
import de.perdian.apps.flighttracker.business.modules.flights.FlightsQueryService;
import de.perdian.apps.flighttracker.business.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.web.security.FlighttrackerUser;

@ControllerAdvice(assignableTypes = OverviewController.class)
public class OverviewQueryHelperFactory {

    private FlightsQueryService flightsQueryService = null;

    @ModelAttribute(name = "overviewQueryHelper")
    public OverviewQueryHelper overviewQueryHelper(@AuthenticationPrincipal FlighttrackerUser authenticationPrincipal) {

        FlightsQuery flightsQuery = new FlightsQuery();
        flightsQuery.setPageSize(Integer.MAX_VALUE);
        flightsQuery.setRestrictUsers(authenticationPrincipal == null ? null : authenticationPrincipal.toUserEntities());
        List<FlightBean> flights = this.getFlightsQueryService().loadFlights(flightsQuery).getItems();

        OverviewQueryHelper queryHelper = new OverviewQueryHelper();
        queryHelper.setAvailableYears(this.collectAvailableYears(flights));
        queryHelper.setAvailableAirlines(this.collectAvailableAirlines(flights));
        queryHelper.setAvailableAirports(this.collectAvailableAirports(flights));
        queryHelper.setAvailableAircraftTypes(this.collectAvailableAircraftTypes(flights));
        queryHelper.setAvailableCabinClasses(this.collectAvailableCabinClasses(flights));
        queryHelper.setAvailableFlightReasons(this.collectAvailableFlightReasons(flights));
        return queryHelper;

    }

    private List<OverviewQueryHelperItem> collectAvailableAircraftTypes(List<FlightBean> flights) {
        return null;
    }

    private List<OverviewQueryHelperItem> collectAvailableFlightReasons(List<FlightBean> flights) {
        return null;
    }

    private List<OverviewQueryHelperItem> collectAvailableCabinClasses(List<FlightBean> flights) {
        return null;
    }

    private List<OverviewQueryHelperItem> collectAvailableAirports(List<FlightBean> flights) {
        return null;
    }

    private List<OverviewQueryHelperItem> collectAvailableAirlines(List<FlightBean> flights) {
        return flights.stream()
            .map(FlightBean::getAirline)
            .filter(Objects::nonNull)
            .filter(airline -> !StringUtils.isEmpty(airline.getCode()) && !StringUtils.isEmpty(airline.getName()))
            .map(airline -> new OverviewQueryHelperItem(airline.getName(), airline.getCode()))
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    private SortedSet<Integer> collectAvailableYears(List<FlightBean> flights) {
        TreeSet<Integer> availableYears = new TreeSet<>((i1, i2) -> Integer.compare(i2, i1));
        availableYears.addAll(flights.stream()
            .flatMap(flight -> Arrays.asList(flight.getDepartureContact().getDateLocal() == null ? null : flight.getDepartureContact().getDateLocal().getYear(), flight.getArrivalContact().getDateLocal() == null ? null : flight.getArrivalContact().getDateLocal().getYear()).stream())
            .filter(Objects::nonNull)
            .collect(Collectors.toSet()));
        return availableYears;
    }

    FlightsQueryService getFlightsQueryService() {
        return this.flightsQueryService;
    }
    @Autowired
    void setFlightsQueryService(FlightsQueryService flightsQueryService) {
        this.flightsQueryService = flightsQueryService;
    }

}
