package de.perdian.apps.flighttracker.web.modules.overview;

import java.util.ArrayList;
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
import de.perdian.apps.flighttracker.business.modules.flights.model.AirportBean;
import de.perdian.apps.flighttracker.business.modules.flights.model.AirportContactBean;
import de.perdian.apps.flighttracker.business.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightDistance;
import de.perdian.apps.flighttracker.support.types.FlightReason;
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
        queryHelper.setAvailableCabinClasses(Arrays.asList(CabinClass.values()));
        queryHelper.setAvailableFlightReasons(Arrays.asList(FlightReason.values()));
        queryHelper.setAvailableFlightDistances(Arrays.asList(FlightDistance.values()));
        return queryHelper;

    }

    private List<OverviewQueryHelperItem> collectAvailableAircraftTypes(List<FlightBean> flights) {
        return flights.stream()
            .map(FlightBean::getAircraft)
            .filter(Objects::nonNull)
            .filter(aircraft -> !StringUtils.isEmpty(aircraft.getType()))
            .map(aircraft -> new OverviewQueryHelperItem(aircraft.getType(), aircraft.getType()))
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    private List<OverviewQueryHelperItem> collectAvailableAirports(List<FlightBean> flights) {

        List<AirportBean> allAirports = new ArrayList<>();
        flights.stream()
            .map(FlightBean::getDepartureContact)
            .map(AirportContactBean::getAirport)
            .forEach(allAirports::add);
        flights.stream()
            .map(FlightBean::getArrivalContact)
            .map(AirportContactBean::getAirport)
            .forEach(allAirports::add);

        return allAirports.stream()
            .filter(Objects::nonNull)
            .filter(airport -> !StringUtils.isEmpty(airport.getCode()) && !StringUtils.isEmpty(airport.getName()))
            .map(airport -> new OverviewQueryHelperItem(airport.getName(), airport.getCode()))
            .distinct()
            .sorted()
            .collect(Collectors.toList());

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
