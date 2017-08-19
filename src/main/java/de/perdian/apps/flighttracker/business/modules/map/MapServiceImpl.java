package de.perdian.apps.flighttracker.business.modules.map;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.perdian.apps.flighttracker.business.modules.flights.FlightsQuery;
import de.perdian.apps.flighttracker.business.modules.flights.FlightsQueryService;
import de.perdian.apps.flighttracker.business.modules.flights.model.AirportBean;
import de.perdian.apps.flighttracker.business.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.business.modules.map.model.MapModel;
import de.perdian.apps.flighttracker.business.modules.map.model.MapModelAirport;
import de.perdian.apps.flighttracker.business.modules.map.model.MapModelPoint;
import de.perdian.apps.flighttracker.business.modules.map.model.MapModelRoute;

@Service
public class MapServiceImpl implements MapService {

    private FlightsQueryService flightsQueryService = null;

    @Override
    public MapModel loadMap(MapQuery mapQuery) {

        FlightsQuery flightsQuery = this.createFlightsQuery(mapQuery);
        List<FlightBean> flights = this.getFlightsQueryService().loadFlights(flightsQuery).getItems();
        Map<String, List<FlightBean>> flightsByRoute = new HashMap<>();
        for (FlightBean flight : flights) {
            flightsByRoute.compute(flight.getDepartureContact().getAirport().getCode() + "-" + flight.getArrivalContact().getAirport().getCode(), (k, v) -> v == null ? new ArrayList<>() : v).add(flight);
        }

        Map<String, AirportBean> airportsByCode = new HashMap<>();

        List<MapModelRoute> routes = new ArrayList<>(flightsByRoute.size());
        for (Map.Entry<String, List<FlightBean>> flightsEntry : flightsByRoute.entrySet()) {
            AirportBean departureAirport = flightsEntry.getValue().get(0).getDepartureContact().getAirport();
            AirportBean arrivalAirport = flightsEntry.getValue().get(0).getArrivalContact().getAirport();
            if (departureAirport != null && departureAirport.getLatitude() != null && departureAirport.getLongitude() != null && arrivalAirport != null && arrivalAirport.getLatitude() != null && arrivalAirport.getLongitude() != null) {

                airportsByCode.putIfAbsent(departureAirport.getCode(), departureAirport);
                airportsByCode.putIfAbsent(arrivalAirport.getCode(), arrivalAirport);

                MapModelRoute route = new MapModelRoute();
                route.setRoute(departureAirport.getCode() + "-" + arrivalAirport.getCode());
                route.setStartPoint(new MapModelPoint(departureAirport.getLatitude(), departureAirport.getLongitude()));
                route.setEndPoint(new MapModelPoint(arrivalAirport.getLatitude(), arrivalAirport.getLongitude()));
                route.setWeight(this.computeWeight(flightsEntry.getValue().size()));
                routes.add(route);

            }
        }

        List<MapModelAirport> airports = new ArrayList<>(airportsByCode.size());
        for (Map.Entry<String, AirportBean> airportEntry : airportsByCode.entrySet()) {
            MapModelAirport airport = new MapModelAirport();
            airport.setCode(airportEntry.getValue().getCode());
            airport.setPoint(new MapModelPoint(airportEntry.getValue().getLatitude(), airportEntry.getValue().getLongitude()));
            airports.add(airport);
        }

        MapModel mapModel = new MapModel();
        mapModel.setRoutes(routes);
        mapModel.setAirports(airports);
        return mapModel;

    }

    private FlightsQuery createFlightsQuery(MapQuery mapQuery) {
        FlightsQuery flightsQuery = new FlightsQuery();
        if (mapQuery.getYear() != null) {
            flightsQuery.setMinimumDepartureDateLocal(LocalDate.of(mapQuery.getYear(), 1, 1));
            flightsQuery.setMaximumArrivalDateLocal(LocalDate.of(mapQuery.getYear(), 12, 31));
        }
        return flightsQuery;
    }

    private int computeWeight(int numberOfFlightsForRoute) {
        if (numberOfFlightsForRoute <= 1) {
            return 1;
        } else if (numberOfFlightsForRoute <= 5) {
            return 3;
        } else if (numberOfFlightsForRoute <= 10) {
            return 5;
        } else if (numberOfFlightsForRoute <= 20) {
            return 7;
        } else {
            return 9;
        }
    }

    FlightsQueryService getFlightsQueryService() {
        return this.flightsQueryService;
    }
    @Autowired
    void setFlightsQueryService(FlightsQueryService flightsQueryService) {
        this.flightsQueryService = flightsQueryService;
    }

}
