package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightDistance;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.FlightType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@ControllerAdvice(assignableTypes = OverviewController.class)
class OverviewQueryValuesAdvice {

    @ModelAttribute("overviewQueryValues")
    OverviewQueryValues overviewQueryValues(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_ALL_FLIGHTS) List<Flight> allFlights) {
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

    private List<OverviewQueryValuesItem> createQueryValuesItemsForYears(List<Flight> allFlights) {
        return allFlights.stream()
            .map(flight -> flight.getDepartureContact().getDateLocal().getYear())
            .distinct()
            .sorted(Comparator.reverseOrder())
            .map(year -> new OverviewQueryValuesItem(String.valueOf(year)))
            .toList();
    }

    private List<OverviewQueryValuesItem> createQueryValuesItemsForAirlines(List<Flight> allFlights) {
        return allFlights.stream()
            .map(flight -> flight.getAirline())
            .map(airline -> new OverviewQueryValuesItem(airline.getCode(), OverviewString.forValue("[" + airline.getCode() + "] " + airline.getName())))
            .distinct()
            .sorted()
            .toList();
    }

    private List<OverviewQueryValuesItem> createQueryValuesItemsForAirports(List<Flight> allFlights) {
        return allFlights.stream()
            .flatMap(flight -> Stream.of(flight.getDepartureContact().getAirport(), flight.getArrivalContact().getAirport()))
            .map(airport -> new OverviewQueryValuesItem(airport.getCode(), OverviewString.forValue("[" + airport.getCode() + "] " + airport.getName())))
            .distinct()
            .sorted()
            .toList();
    }

    private List<OverviewQueryValuesItem> createQueryValuesItemsForAircraftTypes(List<Flight> allFlights) {
        return allFlights.stream()
            .map(flight -> flight == null || flight.getAircraft() == null ? null : flight.getAircraft().getType())
            .filter(aircraftType -> StringUtils.isNotEmpty(aircraftType))
            .map(aircraftType -> new OverviewQueryValuesItem(aircraftType))
            .distinct()
            .sorted()
            .toList();
    }

    private <E extends Enum<E>> List<OverviewQueryValuesItem> createQueryValuesItemsForEnum(Class<E> enumClass) {
        return Stream.of(enumClass.getEnumConstants())
            .map(enumInstance -> new OverviewQueryValuesItem(enumInstance))
            .toList();
    }

}
