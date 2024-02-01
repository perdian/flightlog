package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.authentication.UserHolder;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.service.FlightQuery;
import de.perdian.flightlog.modules.flights.shared.service.FlightQueryService;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightDistance;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.FlightType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Controller
class OverviewController {

    private UserHolder userHolder = null;
    private FlightQueryService flightQueryService = null;

    @RequestMapping({ "/", "/overview" })
    String doOverview(Model model) {
        return "overview";
    }

    @ModelAttribute("filteredFlights")
    List<Flight> filteredFlights(OverviewQuery filteredFlightsQuery) {
        FlightQuery flightQuery = filteredFlightsQuery.toFlightQuery().withUser(this.getUserHolder().getCurrentUser());
        return this.getFlightQueryService().loadFlights(flightQuery);
    }

    @ModelAttribute("allFlights")
    List<Flight> allFlights() {
        return this.getFlightQueryService().loadFlights(new FlightQuery().withUser(this.getUserHolder().getCurrentUser()));
    }

    @ModelAttribute("overviewQuery")
    OverviewQuery overviewQuery() {
        return new OverviewQuery();
    }

    @ModelAttribute("overviewQueryValues")
    OverviewQueryValues overviewQueryValues(@ModelAttribute("allFlights") List<Flight> allFlights) {
        OverviewQueryValues queryValues = new OverviewQueryValues();
        queryValues.setYears(this.createQueryValuesItemsForYears(allFlights));
        queryValues.setAirlines(this.createQueryValuesItemsForAirlines(allFlights));
        queryValues.setAirports(this.createQueryValuesItemsForAirports(allFlights));
        queryValues.setAircraftTypes(this.createQueryValuesItemsForAircraftTypes(allFlights));
        queryValues.setCabinClasses(this.createQueryValuesItemsForEnum(CabinClass.class));
        queryValues.setFlightReasons(this.createQueryValuesItemsForEnum(FlightReason.class));
        queryValues.setFlightDistances(this.createQueryValuesItemsForEnum(FlightDistance.class));
        queryValues.setFlightTypes(this.createQueryValuesItemsForEnum(FlightType.class));
        return queryValues;
    }

    private List<OverviewQueryValues.OverviewQueryValuesItem> createQueryValuesItemsForYears(List<Flight> allFlights) {
        return allFlights.stream()
            .map(flight -> flight.getDepartureContact().getDateLocal().getYear())
            .distinct()
            .sorted(Comparator.reverseOrder())
            .map(year -> OverviewQueryValues.OverviewQueryValuesItem.forText(String.valueOf(year), String.valueOf(year)))
            .toList();
    }

    private List<OverviewQueryValues.OverviewQueryValuesItem> createQueryValuesItemsForAirlines(List<Flight> allFlights) {
        return allFlights.stream()
            .map(flight -> flight.getAirline())
            .map(airline -> OverviewQueryValues.OverviewQueryValuesItem.forText(airline.getName(), airline.getCode()))
            .distinct()
            .sorted(OverviewQueryValues.OverviewQueryValuesItem::compareByText)
            .toList();
    }

    private List<OverviewQueryValues.OverviewQueryValuesItem> createQueryValuesItemsForAirports(List<Flight> allFlights) {
        return allFlights.stream()
            .flatMap(flight -> Stream.of(flight.getDepartureContact().getAirport(), flight.getArrivalContact().getAirport()))
            .map(airport -> OverviewQueryValues.OverviewQueryValuesItem.forText(airport.getName(), airport.getCode()))
            .distinct()
            .sorted(OverviewQueryValues.OverviewQueryValuesItem::compareByText)
            .toList();
    }

    private List<OverviewQueryValues.OverviewQueryValuesItem> createQueryValuesItemsForAircraftTypes(List<Flight> allFlights) {
        return allFlights.stream()
            .map(flight -> flight.getAircraft().getType())
            .map(aircraftType -> OverviewQueryValues.OverviewQueryValuesItem.forText(aircraftType))
            .distinct()
            .sorted(OverviewQueryValues.OverviewQueryValuesItem::compareByText)
            .toList();
    }

    private <E extends Enum<E>> List<OverviewQueryValues.OverviewQueryValuesItem> createQueryValuesItemsForEnum(Class<E> enumClass) {
        return Stream.of(enumClass.getEnumConstants())
            .map(enumInstance -> OverviewQueryValues.OverviewQueryValuesItem.forTextKey(enumClass.getSimpleName() + "." + enumInstance.name(), enumInstance.name()))
            .toList();
    }

    UserHolder getUserHolder() {
        return this.userHolder;
    }
    @Autowired
    void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

    FlightQueryService getFlightQueryService() {
        return this.flightQueryService;
    }
    @Autowired
    void setFlightQueryService(FlightQueryService flightQueryService) {
        this.flightQueryService = flightQueryService;
    }

}
