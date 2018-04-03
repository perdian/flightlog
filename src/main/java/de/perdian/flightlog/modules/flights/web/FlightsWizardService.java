package de.perdian.flightlog.modules.flights.web;

import de.perdian.flightlog.modules.users.persistence.UserEntity;

interface FlightsWizardService {

    void enhanceFlightEditor(FlightEditor editor, FlightsWizardData data, UserEntity user);

}
