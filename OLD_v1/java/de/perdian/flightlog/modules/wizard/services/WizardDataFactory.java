package de.perdian.flightlog.modules.wizard.services;

import java.time.LocalDate;
import java.util.List;

public interface WizardDataFactory {

    List<WizardData> createData(String airlineCode, String flightNumber, LocalDate departureDate, String departureAirportCode);

}
