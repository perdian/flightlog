package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

enum OverviewStatisticsOthers {

    DISTINCT_AIRCRAFT_TYPES(flight -> {
        if (flight.getAircraft() == null || StringUtils.isEmpty(flight.getAircraft().getType())) {
            return Collections.emptyList();
        } else {
            return List.of(flight.getAircraft().getType());
        }
    }),

    DISTINCT_AIRPORTS(flight -> {
        List<Object> airportCodes = new ArrayList<>();
        if (flight.getDepartureContact() != null && flight.getDepartureContact().getAirport() != null && StringUtils.isNotEmpty(flight.getDepartureContact().getAirport().getCode())) {
            airportCodes.add(flight.getDepartureContact().getAirport().getCode());
        }
        if (flight.getArrivalContact() != null && flight.getArrivalContact().getAirport() != null && StringUtils.isNotEmpty(flight.getArrivalContact().getAirport().getCode())) {
            airportCodes.add(flight.getArrivalContact().getAirport().getCode());
        }
        return airportCodes;
    }),

    DISTINCT_COUNTRIES(flight -> {
        List<Object> airportCodes = new ArrayList<>();
        if (flight.getDepartureContact() != null && flight.getDepartureContact().getAirport() != null && StringUtils.isNotEmpty(flight.getDepartureContact().getAirport().getCountryCode())) {
            airportCodes.add(flight.getDepartureContact().getAirport().getCountryCode());
        }
        if (flight.getArrivalContact() != null && flight.getArrivalContact().getAirport() != null && StringUtils.isNotEmpty(flight.getArrivalContact().getAirport().getCountryCode())) {
            airportCodes.add(flight.getArrivalContact().getAirport().getCountryCode());
        }
        return airportCodes;
    }),

    DISTINCT_ROUTES(flight -> {
        String departureAirportCode = flight.getDepartureContact() == null || flight.getDepartureContact().getAirport() == null || StringUtils.isEmpty(flight.getDepartureContact().getAirport().getCode()) ? null : flight.getDepartureContact().getAirport().getCode();
        String arrivalAirportCode = flight.getArrivalContact() == null || flight.getArrivalContact().getAirport() == null || StringUtils.isEmpty(flight.getArrivalContact().getAirport().getCode()) ? null : flight.getArrivalContact().getAirport().getCode();
        if (StringUtils.isEmpty(departureAirportCode) || StringUtils.isEmpty(arrivalAirportCode)) {
            return Collections.emptyList();
        } else {
            return List.of(departureAirportCode + "-" + arrivalAirportCode);
        }
    }),

    DISTINCT_AIRLINES(flight -> {
        if (flight.getAirline() == null || StringUtils.isEmpty(flight.getAirline().getCode())) {
            return Collections.emptyList();
        } else {
            return List.of(flight.getAirline().getCode());
        }
    });

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
