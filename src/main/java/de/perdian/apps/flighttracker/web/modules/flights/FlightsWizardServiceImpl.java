package de.perdian.apps.flighttracker.web.modules.flights;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.perdian.apps.flighttracker.business.modules.data.FlightData;
import de.perdian.apps.flighttracker.business.modules.data.FlightDataService;
import de.perdian.apps.flighttracker.persistence.entities.AirlineEntity;
import de.perdian.apps.flighttracker.persistence.entities.AirportEntity;
import de.perdian.apps.flighttracker.persistence.repositories.AirlinesRepository;
import de.perdian.apps.flighttracker.persistence.repositories.AirportsRepository;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;

@Service
class FlightsWizardServiceImpl implements FlightsWizardService {

    private FlightDataService flightDataService = null;
    private AirportsRepository airportsRepository = null;
    private AirlinesRepository airlinesRepository = null;

    @Override
    public void enhanceFlightEditor(FlightEditor editor, FlightsWizardData data) {

        editor.setAirlineCode(data.getWizAirlineCode());
        editor.setFlightNumber(data.getWizFlightNumber());

        LocalDate departureDateLocal = FlighttrackerHelper.parseLocalDate(data.getWizDepartureDateLocal());
        String airlineCode = Optional.ofNullable(data.getWizAirlineCode()).map(String::toUpperCase).orElse(null);
        String flightNumber = data.getWizFlightNumber();
        if (!StringUtils.isEmpty(airlineCode) && !StringUtils.isEmpty(flightNumber)) {
            FlightData flightData = this.getFlightDataService().lookupFlightData(airlineCode, flightNumber, departureDateLocal);
            if (flightData != null) {

                editor.setAircraftType(flightData.getAircraftType());
                editor.setAircraftRegistration(flightData.getAircraftRegistration());
                editor.setAircraftName(flightData.getAircraftName());
                editor.setDepartureDateLocal(FlighttrackerHelper.formatDate(flightData.getDepartureDateLocal()));
                editor.setDepartureTimeLocal(FlighttrackerHelper.formatTime(flightData.getDepartureTimeLocal()));
                editor.setDepartureAirportCode(flightData.getDepartureAirportCode());
                editor.setArrivalAirportCode(flightData.getArrivalAirportCode());
                editor.setArrivalDateLocal(FlighttrackerHelper.formatDate(flightData.getArrivalDateLocal()));
                editor.setArrivalTimeLocal(FlighttrackerHelper.formatTime(flightData.getArrivalTimeLocal()));

                AirportEntity departureAirport = StringUtils.isEmpty(flightData.getDepartureAirportCode()) ? null : this.getAirportsRepository().loadAirportByIataCode(flightData.getDepartureAirportCode().toUpperCase());
                if (departureAirport != null) {
                    editor.setDepartureAirportCode(departureAirport.getIataCode());
                    editor.setDepartureAirportCountryCode(departureAirport.getCountryCode());
                    editor.setDepartureAirportName(departureAirport.getName());
                }
                AirportEntity arrivalAirport = StringUtils.isEmpty(flightData.getArrivalAirportCode()) ? null : this.getAirportsRepository().loadAirportByIataCode(flightData.getArrivalAirportCode().toUpperCase());
                if (arrivalAirport != null) {
                    editor.setArrivalAirportCode(arrivalAirport.getIataCode());
                    editor.setArrivalAirportCountryCode(arrivalAirport.getCountryCode());
                    editor.setArrivalAirportName(arrivalAirport.getName());
                }

                if (departureAirport != null && arrivalAirport != null) {

                    Integer flightDistance = FlighttrackerHelper.computeDistanceInKilometers(departureAirport.getLongitude(), departureAirport.getLatitude(), arrivalAirport.getLongitude(), arrivalAirport.getLatitude());
                    editor.setFlightDistance(flightDistance == null ? null : flightDistance.toString());

                    if (flightData.getDepartureDateLocal() != null && flightData.getDepartureTimeLocal() != null && flightData.getArrivalDateLocal() != null && flightData.getArrivalTimeLocal() != null) {
                        Instant departureInstant = flightData.getDepartureTimeLocal().atDate(flightData.getDepartureDateLocal()).atZone(departureAirport.getTimezoneId()).toInstant();
                        Instant arrivalInstant = flightData.getArrivalTimeLocal().atDate(flightData.getArrivalDateLocal()).atZone(arrivalAirport.getTimezoneId()).toInstant();
                        editor.setFlightDuration(FlighttrackerHelper.formatDuration(Duration.between(departureInstant, arrivalInstant)));
                    }

                    AirlineEntity airlineEntity = this.getAirlinesRepository().loadAirlineByIataCode(data.getWizAirlineCode());
                    if (airlineEntity != null) {
                        editor.setAirlineName(airlineEntity.getName());
                    }

                }
            }
        }

    }

    FlightDataService getFlightDataService() {
        return this.flightDataService;
    }
    @Autowired
    void setFlightDataService(FlightDataService flightDataService) {
        this.flightDataService = flightDataService;
    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

    AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

}
