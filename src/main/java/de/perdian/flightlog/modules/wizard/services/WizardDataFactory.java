package de.perdian.flightlog.modules.wizard.services;

import java.time.LocalDate;

public interface WizardDataFactory {

    WizardData createData(String airlineCode, String flightNumber, LocalDate departureDate);

}
