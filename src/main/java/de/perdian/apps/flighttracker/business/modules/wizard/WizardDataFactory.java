package de.perdian.apps.flighttracker.business.modules.wizard;

import java.time.LocalDate;

public interface WizardDataFactory {

    WizardData createData(String airlineCode, String flightNumber, LocalDate departureDate);

}
