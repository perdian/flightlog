package de.perdian.apps.flighttracker.modules.flights.web;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.airlines.services.AirlinesService;
import de.perdian.apps.flighttracker.modules.airports.persistence.AirportEntity;
import de.perdian.apps.flighttracker.modules.airports.persistence.AirportsRepository;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;
import de.perdian.apps.flighttracker.modules.wizard.services.WizardData;
import de.perdian.apps.flighttracker.modules.wizard.services.WizardDataService;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;

@Service
class FlightsWizardServiceImpl implements FlightsWizardService {

    private WizardDataService wizardDataService = null;
    private AirportsRepository airportsRepository = null;
    private AirlinesService airlinesService = null;

    @Override
    public void enhanceFlightEditor(FlightEditor editor, FlightsWizardData data, UserEntity user) {

        editor.setAirlineCode(data.getWizAirlineCode());
        editor.setFlightNumber(data.getWizFlightNumber());

        LocalDate departureDateLocal = FlighttrackerHelper.parseLocalDate(data.getWizDepartureDateLocal());
        String airlineCode = Optional.ofNullable(data.getWizAirlineCode()).map(String::toUpperCase).orElse(null);
        String flightNumber = data.getWizFlightNumber();
        if (!StringUtils.isEmpty(airlineCode) && !StringUtils.isEmpty(flightNumber)) {
            WizardData wizardData = this.getWizardDataService().createData(airlineCode, flightNumber, departureDateLocal);
            if (wizardData != null) {

                editor.setAircraftType(wizardData.getAircraftType());
                editor.setAircraftRegistration(wizardData.getAircraftRegistration());
                editor.setAircraftName(wizardData.getAircraftName());
                editor.setDepartureDateLocal(FlighttrackerHelper.formatDate(wizardData.getDepartureDateLocal()));
                editor.setDepartureTimeLocal(FlighttrackerHelper.formatTime(wizardData.getDepartureTimeLocal()));
                editor.setDepartureAirportCode(wizardData.getDepartureAirportCode());
                editor.setArrivalAirportCode(wizardData.getArrivalAirportCode());
                editor.setArrivalDateLocal(FlighttrackerHelper.formatDate(wizardData.getArrivalDateLocal()));
                editor.setArrivalTimeLocal(FlighttrackerHelper.formatTime(wizardData.getArrivalTimeLocal()));

                AirportEntity departureAirport = StringUtils.isEmpty(wizardData.getDepartureAirportCode()) ? null : this.getAirportsRepository().loadAirportByIataCode(wizardData.getDepartureAirportCode().toUpperCase());
                if (departureAirport != null) {
                    editor.setDepartureAirportCode(departureAirport.getIataCode());
                    editor.setDepartureAirportCountryCode(departureAirport.getCountryCode());
                    editor.setDepartureAirportName(departureAirport.getName());
                }
                AirportEntity arrivalAirport = StringUtils.isEmpty(wizardData.getArrivalAirportCode()) ? null : this.getAirportsRepository().loadAirportByIataCode(wizardData.getArrivalAirportCode().toUpperCase());
                if (arrivalAirport != null) {
                    editor.setArrivalAirportCode(arrivalAirport.getIataCode());
                    editor.setArrivalAirportCountryCode(arrivalAirport.getCountryCode());
                    editor.setArrivalAirportName(arrivalAirport.getName());
                }

                if (departureAirport != null && arrivalAirport != null) {

                    Integer flightDistance = FlighttrackerHelper.computeDistanceInKilometers(departureAirport.getLongitude(), departureAirport.getLatitude(), arrivalAirport.getLongitude(), arrivalAirport.getLatitude());
                    editor.setFlightDistance(flightDistance == null ? null : flightDistance.toString());

                    if (wizardData.getDepartureDateLocal() != null && wizardData.getDepartureTimeLocal() != null && wizardData.getArrivalDateLocal() != null && wizardData.getArrivalTimeLocal() != null) {
                        Instant departureInstant = wizardData.getDepartureTimeLocal().atDate(wizardData.getDepartureDateLocal()).atZone(departureAirport.getTimezoneId()).toInstant();
                        Instant arrivalInstant = wizardData.getArrivalTimeLocal().atDate(wizardData.getArrivalDateLocal()).atZone(arrivalAirport.getTimezoneId()).toInstant();
                        editor.setFlightDuration(FlighttrackerHelper.formatDuration(Duration.between(departureInstant, arrivalInstant)));
                    }

                    AirlineBean airlineEntity = this.getAirlinesService().loadAirlineByCode(data.getWizAirlineCode(), user);
                    if (airlineEntity != null) {
                        editor.setAirlineName(airlineEntity.getName());
                    }

                }
            }
        }

    }

    WizardDataService getWizardDataService() {
        return this.wizardDataService;
    }
    @Autowired
    void setWizardDataService(WizardDataService wizardDataService) {
        this.wizardDataService = wizardDataService;
    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

    AirlinesService getAirlinesService() {
        return this.airlinesService;
    }
    @Autowired
    void setAirlinesService(AirlinesService airlinesService) {
        this.airlinesService = airlinesService;
    }

}
