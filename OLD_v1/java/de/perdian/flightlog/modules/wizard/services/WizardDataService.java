package de.perdian.flightlog.modules.wizard.services;

import java.time.LocalDate;
import java.util.List;

public interface WizardDataService {

    /**
     * Try to lookup all available data about the given flight on the
     * specific date passed to the method.
     *
     * @param airlineCode
     *      the airline code for which the flight was operated
     * @param flightNumber
     *      the flight number
     * @param departureDate
     *      the departure date (relative to the timezone of the departure
     *      airport) for which the information should be looked up
     * @param departureAirportCode
     *      the deparutre airport
     * @return
     *      the flight data that could be computed by this data source.
     *      In cases the flight could not be found, {@code null} is
     *      returned.
     */
    List<WizardData> createData(String airlineCode, String flightNumber, LocalDate departureDate, String departureAirportCode);

}
