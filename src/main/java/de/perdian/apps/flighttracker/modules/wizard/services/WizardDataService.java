package de.perdian.apps.flighttracker.modules.wizard.services;

import java.time.LocalDate;

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
     * @return
     *      the flight data that could be computed by this data source.
     *      In cases the flight could not be found, {@code null} is
     *      returned. Also for cases where no exact match could be found
     *      (for example because the same flight number was used multiple
     *      times during a single day by an airline) the result will also
     *      be {@code null} to avoid any strange behaviour.
     */
    WizardData createData(String airlineCode, String flightNumber, LocalDate departureDate);

}
