package de.perdian.apps.flighttracker.modules.flights.web;

import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

interface FlightsWizardService {

    void enhanceFlightEditor(FlightEditor editor, FlightsWizardData data, UserEntity user);

}
