package de.perdian.flightlog.modules.flights.web;

import de.perdian.flightlog.modules.wizard.services.WizardData;

interface FlightsWizardService {

    FlightEditor enhanceFlightEditor(FlightEditor editor, FlightWizardEditor flightWizardEditor, WizardData wizardData);

}
