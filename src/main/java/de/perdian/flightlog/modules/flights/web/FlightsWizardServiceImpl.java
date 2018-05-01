package de.perdian.flightlog.modules.flights.web;

import java.time.Duration;
import java.time.Instant;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.perdian.flightlog.modules.airlines.model.AirlineBean;
import de.perdian.flightlog.modules.airlines.services.AirlinesService;
import de.perdian.flightlog.modules.airports.persistence.AirportEntity;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.modules.users.persistence.UserEntity;
import de.perdian.flightlog.modules.wizard.services.WizardData;
import de.perdian.flightlog.modules.wizard.services.WizardDataService;
import de.perdian.flightlog.support.FlightlogHelper;

@Service
class FlightsWizardServiceImpl implements FlightsWizardService {

    private WizardDataService wizardDataService = null;
    private AirportsRepository airportsRepository = null;
    private AirlinesService airlinesService = null;

    @Override
    public FlightEditor enhanceFlightEditor(FlightEditor flightEditor, FlightWizardEditor flightWizardEditor, WizardData wizardData, UserEntity user) {

        flightEditor.setAirlineCode(flightWizardEditor.getWizAirlineCode());
        flightEditor.setFlightNumber(flightWizardEditor.getWizFlightNumber());
        flightEditor.setAircraftType(wizardData.getAircraftType());
        flightEditor.setAircraftRegistration(wizardData.getAircraftRegistration());
        flightEditor.setAircraftName(wizardData.getAircraftName());
        flightEditor.setDepartureDateLocal(FlightlogHelper.formatDate(wizardData.getDepartureDateLocal()));
        flightEditor.setDepartureTimeLocal(FlightlogHelper.formatTime(wizardData.getDepartureTimeLocal()));
        flightEditor.setDepartureAirportCode(wizardData.getDepartureAirportCode());
        flightEditor.setArrivalAirportCode(wizardData.getArrivalAirportCode());
        flightEditor.setArrivalDateLocal(FlightlogHelper.formatDate(wizardData.getArrivalDateLocal()));
        flightEditor.setArrivalTimeLocal(FlightlogHelper.formatTime(wizardData.getArrivalTimeLocal()));

        AirportEntity departureAirport = StringUtils.isEmpty(wizardData.getDepartureAirportCode()) ? null : this.getAirportsRepository().loadAirportByIataCode(wizardData.getDepartureAirportCode().toUpperCase());
        if (departureAirport != null) {
            flightEditor.setDepartureAirportCode(departureAirport.getIataCode());
            flightEditor.setDepartureAirportCountryCode(departureAirport.getCountryCode());
            flightEditor.setDepartureAirportName(departureAirport.getName());
        }
        AirportEntity arrivalAirport = StringUtils.isEmpty(wizardData.getArrivalAirportCode()) ? null : this.getAirportsRepository().loadAirportByIataCode(wizardData.getArrivalAirportCode().toUpperCase());
        if (arrivalAirport != null) {
            flightEditor.setArrivalAirportCode(arrivalAirport.getIataCode());
            flightEditor.setArrivalAirportCountryCode(arrivalAirport.getCountryCode());
            flightEditor.setArrivalAirportName(arrivalAirport.getName());
        }

        if (departureAirport != null && arrivalAirport != null) {

            Integer flightDistance = FlightlogHelper.computeDistanceInKilometers(departureAirport.getLongitude(), departureAirport.getLatitude(), arrivalAirport.getLongitude(), arrivalAirport.getLatitude());
            flightEditor.setFlightDistance(flightDistance == null ? null : flightDistance.toString());

            if (wizardData.getDepartureDateLocal() != null && wizardData.getDepartureTimeLocal() != null && wizardData.getArrivalDateLocal() != null && wizardData.getArrivalTimeLocal() != null) {
                Instant departureInstant = wizardData.getDepartureTimeLocal().atDate(wizardData.getDepartureDateLocal()).atZone(departureAirport.getTimezoneId()).toInstant();
                Instant arrivalInstant = wizardData.getArrivalTimeLocal().atDate(wizardData.getArrivalDateLocal()).atZone(arrivalAirport.getTimezoneId()).toInstant();
                flightEditor.setFlightDuration(FlightlogHelper.formatDuration(Duration.between(departureInstant, arrivalInstant)));
            }

            AirlineBean airlineEntity = this.getAirlinesService().loadAirlineByCode(flightWizardEditor.getWizAirlineCode(), user);
            if (airlineEntity != null) {
                flightEditor.setAirlineName(airlineEntity.getName());
            }

        }
        return flightEditor;

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
