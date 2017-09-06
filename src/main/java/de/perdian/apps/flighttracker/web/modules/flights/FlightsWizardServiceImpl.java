package de.perdian.apps.flighttracker.web.modules.flights;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.perdian.apps.flighttracker.persistence.entities.AirlineEntity;
import de.perdian.apps.flighttracker.persistence.entities.AirportEntity;
import de.perdian.apps.flighttracker.persistence.repositories.AirlinesRepository;
import de.perdian.apps.flighttracker.persistence.repositories.AirportsRepository;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;

@Service
class FlightsWizardServiceImpl implements FlightsWizardService {

    private AirportsRepository airportsRepository = null;
    private AirlinesRepository airlinesRepository = null;

    @Override
    public void enhanceFlightEditor(FlightEditor editor, FlightsWizardData data) {

        LocalDate departureDateLocal = FlighttrackerHelper.parseLocalDate(data.getWizDepartureDateLocal());
        LocalTime departureTimeLocal = FlighttrackerHelper.parseLocalTime(data.getWizDepartureTimeLocal());
        String departureAirportCode = Optional.ofNullable(data.getWizDepartureAirportCode()).map(String::toUpperCase).orElse(null);
        String arrivalAirportCode = Optional.ofNullable(data.getWizArrivalAirportCode()).map(String::toUpperCase).orElse(null);
        String airlineCode = Optional.ofNullable(data.getWizAirlineCode()).map(String::toUpperCase).orElse(null);
        if (StringUtils.isEmpty(departureAirportCode) || StringUtils.isEmpty(arrivalAirportCode)) {

            // Try to compute the departure and arrival airport by looking up the data via the flight number
            String flightNumber = data.getWizFlightNumber();
            if (!StringUtils.isEmpty(airlineCode) && !StringUtils.isEmpty(flightNumber)) {
                // TODO: Fetch data for airline code and flight number from an external provider
                // and update departure airport code as well as arrival airport code (perhaps even
                // departure and arrival times if available from external provider)
            }

        }
        AirportEntity departureAirport = this.getAirportsRepository().loadAirportByIataCode(departureAirportCode);
        AirportEntity arrivalAirport = this.getAirportsRepository().loadAirportByIataCode(arrivalAirportCode);
        AirlineEntity airline = this.getAirlinesRepository().loadAirlineByIataCode(airlineCode);

        editor.setDepartureDateLocal(FlighttrackerHelper.formatDate(departureDateLocal));
        editor.setDepartureTimeLocal(FlighttrackerHelper.formatTime(departureTimeLocal));
        editor.setDepartureAirportCode(departureAirport == null ? departureAirportCode : departureAirport.getIataCode());
        editor.setDepartureAirportCountryCode(departureAirport == null ? null : departureAirport.getCountryCode());
        editor.setDepartureAirportName(departureAirport == null ? null : departureAirport.getName());
        editor.setArrivalAirportCode(arrivalAirport == null ? arrivalAirportCode : arrivalAirport.getIataCode());
        editor.setArrivalAirportCountryCode(arrivalAirport == null ? null : arrivalAirport.getCountryCode());
        editor.setArrivalAirportName(arrivalAirport == null ? null : arrivalAirport.getName());
        editor.setAirlineCode(airline == null ? airlineCode : airline.getIataCode());
        editor.setAirlineName(airline == null ? null : airline.getName());

        if (departureAirport != null && arrivalAirport != null) {
            Integer flightDistance = FlighttrackerHelper.computeDistanceInKilometers(departureAirport.getLongitude(), departureAirport.getLatitude(), arrivalAirport.getLongitude(), arrivalAirport.getLatitude());
            editor.setFlightDistance(flightDistance == null ? null : flightDistance.toString());
            if (flightDistance != null && departureDateLocal != null && departureTimeLocal != null && departureAirport.getTimezoneId() != null && arrivalAirport.getTimezoneId() != null) {

                // Let's assume our plane travels at 800 km per hour and extrapolate the time when it will arrive
                double hoursRequiredForFlightDistance = flightDistance.doubleValue() / 800d;
                Instant departureTime = departureTimeLocal.atDate(departureDateLocal).atZone(departureAirport.getTimezoneId()).toInstant();
                Instant arrivalTime = departureTime.plus((long)hoursRequiredForFlightDistance, ChronoUnit.HOURS);
                ZonedDateTime arrivalTimeZoned = arrivalTime.atZone(arrivalAirport.getTimezoneId());
                editor.setArrivalDateLocal(FlighttrackerHelper.formatDate(arrivalTimeZoned.toLocalDate()));

            }
        }

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
