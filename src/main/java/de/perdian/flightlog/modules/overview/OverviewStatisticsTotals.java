package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;

import java.util.function.Function;

enum OverviewStatisticsTotals {

    TOTAL_NUMBER(flight -> 1),
    DISTANCE_KM(flight -> flight.getFlightDistance()),
    DISTANCE_MILES(flight -> flight.getFlightDistance() == null ? null : Math.round(flight.getFlightDistance()  / 1.609d)),
    DURATION_HOURS(flight -> flight.getFlightDuration() == null ? null : flight.getFlightDuration().toMinutes() / 60d),
    DURATION_DAYS(flight -> flight.getFlightDuration() == null ? null : (flight.getFlightDuration().toMinutes() / 60d / 24d));

    private Function<Flight, Number> resultFromMatchingFlightFunction = null;

    OverviewStatisticsTotals(Function<Flight, Number> resultFromMatchingFlightFunction) {
        this.setResultFromMatchingFlightFunction(resultFromMatchingFlightFunction);
    }

    Function<Flight, Number> getResultFromMatchingFlightFunction() {
        return this.resultFromMatchingFlightFunction;
    }
    private void setResultFromMatchingFlightFunction(Function<Flight, Number> resultFromMatchingFlightFunction) {
        this.resultFromMatchingFlightFunction = resultFromMatchingFlightFunction;
    }

}
