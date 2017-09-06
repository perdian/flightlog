package de.perdian.apps.flighttracker.web.modules.flights;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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

        LocalDate departureDateLocal = FlighttrackerHelper.parseLocalDate(data.getWizDepartureDateLocal());
        LocalTime departureTimeLocal = FlighttrackerHelper.parseLocalTime(data.getWizDepartureTimeLocal());
        LocalDate arrivalDateLocal = null;
        LocalTime arrivalTimeLocal = FlighttrackerHelper.parseLocalTime(data.getWizArrivalTimeLocal());
        String departureAirportCode = Optional.ofNullable(data.getWizDepartureAirportCode()).map(String::toUpperCase).orElse(null);
        String arrivalAirportCode = Optional.ofNullable(data.getWizArrivalAirportCode()).map(String::toUpperCase).orElse(null);
        String airlineCode = Optional.ofNullable(data.getWizAirlineCode()).map(String::toUpperCase).orElse(null);
        String flightNumber = data.getWizFlightNumber();
        Duration duration = null;
        if (!StringUtils.isEmpty(airlineCode) && !StringUtils.isEmpty(flightNumber)) {
            FlightData flightData = this.getFlightDataService().lookupFlightData(airlineCode, flightNumber, departureDateLocal);
            if (flightData != null) {
                if (StringUtils.isEmpty(departureAirportCode) && StringUtils.isEmpty(arrivalAirportCode)) {
                    departureAirportCode = flightData.getDepartureAirportCode();
                    arrivalAirportCode = flightData.getArrivalAirportCode();
                    if (flightData.getArrivalDateLocal() != null && flightData.getArrivalTimeLocal() != null) {
                        arrivalDateLocal = flightData.getArrivalDateLocal();
                        arrivalTimeLocal = flightData.getArrivalTimeLocal();
                    }
                }
            }
        }
        AirportEntity departureAirport = this.getAirportsRepository().loadAirportByIataCode(departureAirportCode);
        AirportEntity arrivalAirport = this.getAirportsRepository().loadAirportByIataCode(arrivalAirportCode);
        AirlineEntity airline = this.getAirlinesRepository().loadAirlineByIataCode(airlineCode);

        if (departureAirport != null && arrivalAirport != null) {

            Integer flightDistance = FlighttrackerHelper.computeDistanceInKilometers(departureAirport.getLongitude(), departureAirport.getLatitude(), arrivalAirport.getLongitude(), arrivalAirport.getLatitude());
            editor.setFlightDistance(flightDistance == null ? null : flightDistance.toString());

            if (departureDateLocal != null && departureTimeLocal != null && departureAirport.getTimezoneId() != null && arrivalAirport.getTimezoneId() != null) {
                Instant departureInstant = departureTimeLocal.atDate(departureDateLocal).atZone(departureAirport.getTimezoneId()).toInstant();
                if (arrivalDateLocal == null && arrivalTimeLocal != null) {
                    Instant arrivalInstantWithDepartureDate = arrivalTimeLocal.atDate(departureDateLocal).atZone(arrivalAirport.getTimezoneId()).toInstant();
                    Duration durationBetweenDepartureAndArrival = Duration.between(departureInstant, arrivalInstantWithDepartureDate);
                    if (durationBetweenDepartureAndArrival.toHours() < 0) {
                        arrivalDateLocal = arrivalInstantWithDepartureDate.plus(1, ChronoUnit.DAYS).atZone(arrivalAirport.getTimezoneId()).toLocalDate();
                    } else if (durationBetweenDepartureAndArrival.toHours() >= 24) {
                        arrivalDateLocal = arrivalInstantWithDepartureDate.minus(1, ChronoUnit.DAYS).atZone(arrivalAirport.getTimezoneId()).toLocalDate();
                    } else {
                        arrivalDateLocal = arrivalInstantWithDepartureDate.atZone(arrivalAirport.getTimezoneId()).toLocalDate();
                    }
                } else if (arrivalDateLocal == null && flightDistance != null) {

                    // Let's assume our plane travels at 800 km per hour and extrapolate the time when it will arrive
                    double hoursRequiredForFlightDistance = flightDistance.doubleValue() / 800d;
                    Instant approxArrivalInstant = departureInstant.plus((long)hoursRequiredForFlightDistance, ChronoUnit.HOURS);
                    arrivalDateLocal = approxArrivalInstant.atZone(arrivalAirport.getTimezoneId()).toLocalDate();

                }
                if (arrivalDateLocal != null && arrivalTimeLocal != null) {
                    Instant arrivalInstant = arrivalTimeLocal.atDate(arrivalDateLocal).atZone(arrivalAirport.getTimezoneId()).toInstant();
                    duration = Duration.between(departureInstant, arrivalInstant);
                }
            }

        }

        editor.setDepartureDateLocal(FlighttrackerHelper.formatDate(departureDateLocal));
        editor.setDepartureTimeLocal(FlighttrackerHelper.formatTime(departureTimeLocal));
        editor.setDepartureAirportCode(departureAirport == null ? departureAirportCode : departureAirport.getIataCode());
        editor.setDepartureAirportCountryCode(departureAirport == null ? null : departureAirport.getCountryCode());
        editor.setDepartureAirportName(departureAirport == null ? null : departureAirport.getName());
        editor.setArrivalAirportCode(arrivalAirport == null ? arrivalAirportCode : arrivalAirport.getIataCode());
        editor.setArrivalAirportCountryCode(arrivalAirport == null ? null : arrivalAirport.getCountryCode());
        editor.setArrivalAirportName(arrivalAirport == null ? null : arrivalAirport.getName());
        editor.setArrivalDateLocal(arrivalDateLocal == null ? null : FlighttrackerHelper.formatDate(arrivalDateLocal));
        editor.setArrivalTimeLocal(arrivalTimeLocal == null ? null : FlighttrackerHelper.formatTime(arrivalTimeLocal));
        editor.setAirlineCode(airline == null ? airlineCode : airline.getIataCode());
        editor.setAirlineName(airline == null ? null : airline.getName());
        editor.setFlightNumber(flightNumber);
        editor.setFlightDuration(duration == null ? null : FlighttrackerHelper.formatDuration(duration));

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
