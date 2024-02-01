package de.perdian.flightlog.modules.overview.api.routes;

import de.perdian.flightlog.modules.airports.model.Airport;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.overview.OverviewQuery;
import de.perdian.flightlog.modules.overview.OverviewQueryService;
import de.perdian.flightlog.modules.overview.api.routes.model.Routes;
import de.perdian.flightlog.modules.overview.api.routes.model.RoutesItem;
import de.perdian.flightlog.modules.overview.api.routes.model.RoutesPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
class RoutesController {

    private OverviewQueryService overviewQueryService = null;

    @RequestMapping(path = "/overview/api/routes", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    Routes createRoutes(OverviewQuery overviewQuery) {

        Map<String, RoutesPoint> airportPointsByCode = new LinkedHashMap<>();
        Map<String, RoutesItem> itemsByKey = new LinkedHashMap<>();
        for (Flight flight : this.getOverviewQueryService().loadFlights(overviewQuery)) {
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

    OverviewQueryService getOverviewQueryService() {
        return this.overviewQueryService;
    }
    @Autowired
    void setOverviewQueryService(OverviewQueryService overviewQueryService) {
        this.overviewQueryService = overviewQueryService;
    }

}
