package de.perdian.flightlog.modules.overview.api.routes;

import de.perdian.flightlog.modules.airports.model.Airport;
import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetailsHolder;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.service.FlightQuery;
import de.perdian.flightlog.modules.flights.shared.service.FlightQueryService;
import de.perdian.flightlog.modules.overview.api.routes.model.Routes;
import de.perdian.flightlog.modules.overview.api.routes.model.RoutesItem;
import de.perdian.flightlog.modules.overview.api.routes.model.RoutesPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
class RoutesController {

    private FlightlogUserDetailsHolder flightlogUserDetailsHolder = null;
    private FlightQueryService flightQueryService = null;

    @RequestMapping(path = "/overview/api/routes", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    Routes createRoutes(FlightQuery flightQuery) {

        Map<String, RoutesPoint> airportPointsByCode = new LinkedHashMap<>();
        Map<String, RoutesItem> itemsByKey = new LinkedHashMap<>();
        List<Flight> filteredFlights = this.getFlightQueryService().loadFlights(flightQuery.clone().withUserDetails(this.getFlightlogUserDetailsHolder().getCurrentUserDetails()));
        for (Flight flight : filteredFlights) {
            Airport departureAirport = flight.getDepartureContact().getAirport();
            Airport arrivalAirport = flight.getArrivalContact().getAirport();
            if (departureAirport.getLatitude() != null && departureAirport.getLongitude() != null && arrivalAirport.getLatitude() != null && arrivalAirport.getLongitude() != null) {
                airportPointsByCode.computeIfAbsent(departureAirport.getCode(), departureAirportCode -> this.createRoutesPoint(departureAirport));
                airportPointsByCode.computeIfAbsent(arrivalAirport.getCode(), arrivalAirportCode -> this.createRoutesPoint(arrivalAirport));
                String routeKey = departureAirport.getCode() + "-" + arrivalAirport.getCode();
                RoutesItem item = itemsByKey.get(routeKey);
                if (item == null) {
                    item = new RoutesItem();
                    item.setDeparturePoint(this.createRoutesPoint(departureAirport));
                    item.setArrivalPoint(this.createRoutesPoint(arrivalAirport));
                    itemsByKey.put(routeKey, item);
                }
                item.incrementCounter();
            }
        }

        Routes routes = new Routes();
        routes.setUniqueAirports(airportPointsByCode.entrySet().stream().map(Map.Entry::getValue).toList());
        routes.setItems(itemsByKey.entrySet().stream().map(Map.Entry::getValue).toList());
        return routes;

    }

    private RoutesPoint createRoutesPoint(Airport airport) {
        RoutesPoint routesPoint = new RoutesPoint();
        routesPoint.setAirportCode(airport.getCode());
        routesPoint.setLatitude(airport.getLatitude());
        routesPoint.setLongitude(airport.getLongitude());
        return routesPoint;
    }

    FlightQueryService getFlightQueryService() {
        return this.flightQueryService;
    }
    @Autowired
    void setFlightQueryService(FlightQueryService flightQueryService) {
        this.flightQueryService = flightQueryService;
    }

    FlightlogUserDetailsHolder getFlightlogUserDetailsHolder() {
        return this.flightlogUserDetailsHolder;
    }
    @Autowired
    void setFlightlogUserDetailsHolder(FlightlogUserDetailsHolder flightlogUserDetailsHolder) {
        this.flightlogUserDetailsHolder = flightlogUserDetailsHolder;
    }

}
