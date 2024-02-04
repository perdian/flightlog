package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.airlines.model.Airline;
import de.perdian.flightlog.modules.airports.model.Airport;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.model.Route;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightDistance;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.stream.Stream;

@ControllerAdvice(assignableTypes = OverviewController.class)
class OverviewControllerAdvice {

    @ModelAttribute(name = "overviewStatisticsTotals", binding = false)
    OverviewStatisticsGroup statisticsTotals(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        return OverviewStatisticsGroupFactory.forEnum(OverviewStatisticsTotals.class, OverviewStatisticsItemHelpers.sumValues((flight, value) -> value.getResultFromMatchingFlightFunction().apply(flight)), false)
            .withTitle(OverviewString.forKey("flightTotals"))
            .createGroup(flights);
    }

    @ModelAttribute(name = "overviewStatisticsOthers", binding = false)
    OverviewStatisticsGroup statisticsOthers(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        return OverviewStatisticsGroupFactory.forEnum(OverviewStatisticsOthers.class, OverviewStatisticsItemHelpers.countDistinctValues((flight, value) -> value.getValuesFromFlightFunction().apply(flight)), false)
            .withTitle(OverviewString.forKey("otherStatistics"))
            .createGroup(flights);
    }

    @ModelAttribute(name = "overviewStatisticsByDistance", binding = false)
    OverviewStatisticsGroup statisticsByDistance(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        return OverviewStatisticsGroupFactory.forEnum(FlightDistance.class, OverviewStatisticsItemHelpers.countMatchingValues(flight -> flight.getFlightDistanceType() == null ? null : List.of(flight.getFlightDistanceType())), false)
            .withTitle(OverviewString.forKey("distances"))
            .createGroup(flights);
    }

    @ModelAttribute(name = "overviewStatisticsByCabinClass", binding = false)
    OverviewStatisticsGroup statisticsByCabinClass(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        return OverviewStatisticsGroupFactory.forEnum(CabinClass.class, OverviewStatisticsItemHelpers.countMatchingValues(flight -> flight.getCabinClass() == null ? null : List.of(flight.getCabinClass())), true)
            .withTitle(OverviewString.forKey("cabinClasses"))
            .createGroup(flights);
    }

    @ModelAttribute(name = "overviewStatisticsByFlightReason", binding = false)
    OverviewStatisticsGroup statisticsByFlightReason(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        return OverviewStatisticsGroupFactory.forEnum(FlightReason.class, OverviewStatisticsItemHelpers.countMatchingValues(flight -> flight.getFlightReason() == null ? null : List.of(flight.getFlightReason())), true)
            .withTitle(OverviewString.forKey("flightReasons"))
            .createGroup(flights);
    }

    @ModelAttribute(name = "overviewStatisticsBySeatType", binding = false)
    OverviewStatisticsGroup statisticsBySeatType(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        return OverviewStatisticsGroupFactory.forEnum(SeatType.class, OverviewStatisticsItemHelpers.countMatchingValues(flight -> flight.getSeatType() == null ? null : List.of(flight.getSeatType())), true)
            .withTitle(OverviewString.forKey("seatTypes"))
            .createGroup(flights);
    }

    @ModelAttribute(name = "overviewStatisticsTopAirports", binding = false)
    OverviewStatisticsGroup statisticsTopAirports(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {

        List<OverviewStatisticsItemFactory<Airport>> itemFactories = flights.stream()
            .flatMap(flight -> Stream.of(flight.getDepartureContact().getAirport(), flight.getArrivalContact().getAirport()))
            .distinct()
            .map(airport -> OverviewStatisticsItemFactory.forValue(airport)
                .withItemTitle(OverviewString.forValue(airport.getCode()))
                .withItemDescription(OverviewString.forValue(airport.getName()))
                .withItemIconCode(StringUtils.isEmpty(airport.getCountryCode()) ? null : ("flag " + airport.getCountryCode().toLowerCase()))
                .withItemPercentage(true)
                .withResultValueFunction(OverviewStatisticsItemHelpers.countMatchingValues(flight -> List.of(flight.getDepartureContact().getAirport(), flight.getArrivalContact().getAirport())))
            )
            .toList();

        return OverviewStatisticsGroupFactory.forItemFactories(itemFactories)
            .withTitle(OverviewString.forKey("topAirports"))
            .withItemComparator((i1, i2) -> -1 * OverviewStatisticsItem.compareByValue(i1, i2))
            .withMaxItems(15)
            .createGroup(flights);

    }

    @ModelAttribute(name = "overviewStatisticsTopAirlines", binding = false)
    OverviewStatisticsGroup statisticsTopAirlines(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {

        List<OverviewStatisticsItemFactory<Airline>> itemFactories = flights.stream()
            .flatMap(flight -> Stream.of(flight.getAirline()))
            .distinct()
            .map(airline -> OverviewStatisticsItemFactory.forValue(airline)
                .withItemTitle(OverviewString.forValue(airline.getCode()))
                .withItemDescription(OverviewString.forValue(airline.getName()))
                .withItemIconCode(StringUtils.isEmpty(airline.getCountryCode()) ? null : ("flag " + airline.getCountryCode().toLowerCase()))
                .withItemPercentage(true)
                .withResultValueFunction(OverviewStatisticsItemHelpers.countMatchingValues(flight -> List.of(flight.getAirline())))
            )
            .toList();

        return OverviewStatisticsGroupFactory.forItemFactories(itemFactories)
            .withTitle(OverviewString.forKey("topAirlines"))
            .withItemComparator((i1, i2) -> -1 * OverviewStatisticsItem.compareByValue(i1, i2))
            .withMaxItems(15)
            .createGroup(flights);

    }

    @ModelAttribute(name = "overviewStatisticsTopRoutes", binding = false)
    OverviewStatisticsGroup statisticsTopRoutes(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {

        List<OverviewStatisticsItemFactory<Route>> itemFactories = Route.computeDistinctRoutes(flights).stream()
            .map(route -> OverviewStatisticsItemFactory.forValue(route)
                .withItemTitle(OverviewString.forValue(route.getDepartureAirport().getCode() + " - " + route.getArrivalAirport().getCode()))
                .withItemDescription(OverviewString.forValue(route.computeDistanceString()))
                .withItemPercentage(true)
                .withResultValueFunction(OverviewStatisticsItemHelpers.countMatchingValues(flight -> List.of(new Route(flight.getDepartureContact().getAirport(), flight.getArrivalContact().getAirport()))))
            )
            .toList();

        return OverviewStatisticsGroupFactory.forItemFactories(itemFactories)
            .withTitle(OverviewString.forKey("topRoutes"))
            .withItemComparator((i1, i2) -> -1 * OverviewStatisticsItem.compareByValue(i1, i2))
            .withMaxItems(15)
            .createGroup(flights);

    }

    @ModelAttribute(name = "overviewStatisticsTopAircraftTypes", binding = false)
    OverviewStatisticsGroup statisticsTopAircraftTypes(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {

        List<OverviewStatisticsItemFactory<String>> itemFactories = flights.stream()
            .flatMap(flight -> Stream.of(flight.getAircraft().getType()))
            .distinct()
            .map(aircraftType -> OverviewStatisticsItemFactory.forValue(aircraftType)
                .withItemTitle(OverviewString.forValue(aircraftType))
                .withItemPercentage(true)
                .withResultValueFunction(OverviewStatisticsItemHelpers.countMatchingValues(flight -> List.of(flight.getAircraft().getType())))
            )
            .toList();

        return OverviewStatisticsGroupFactory.forItemFactories(itemFactories)
            .withTitle(OverviewString.forKey("topAircraftTypes"))
            .withItemComparator((i1, i2) -> -1 * OverviewStatisticsItem.compareByValue(i1, i2))
            .withMaxItems(15)
            .createGroup(flights);

    }

}
