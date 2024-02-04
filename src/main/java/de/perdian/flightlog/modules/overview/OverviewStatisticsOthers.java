package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;

import java.util.List;
import java.util.function.Function;

enum OverviewStatisticsOthers {

    DISTINCT_AIRCRAFT_TYPES(flight -> List.of(flight.getAircraft().getType())),
    DISTINCT_AIRPORTS(flight -> List.of(flight.getDepartureContact().getAirport().getCode(), flight.getArrivalContact().getAirport().getCode())),
    DISTINCT_COUNTRIES(flight -> List.of(flight.getDepartureContact().getAirport().getCountryCode(), flight.getArrivalContact().getAirport().getCountryCode())),
    DISTINCT_ROUTES(flight -> List.of(flight.getDepartureContact().getAirport().getCode() + "-" + flight.getArrivalContact().getAirport().getCode())),
    DISTINCT_AIRLINES(flight -> List.of(flight.getAirline().getCode()));

    private Function<Flight, List<Object>> valuesFromFlightFunction = null;

    OverviewStatisticsOthers(Function<Flight, List<Object>> valuesFromFlightFunction) {
        this.setValuesFromFlightFunction(valuesFromFlightFunction);
    }

    Function<Flight, List<Object>> getValuesFromFlightFunction() {
        return this.valuesFromFlightFunction;
    }
    private void setValuesFromFlightFunction(Function<Flight, List<Object>> valuesFromFlightFunction) {
        this.valuesFromFlightFunction = valuesFromFlightFunction;
    }
}
