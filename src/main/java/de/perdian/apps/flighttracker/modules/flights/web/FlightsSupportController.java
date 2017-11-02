package de.perdian.apps.flighttracker.modules.flights.web;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.perdian.apps.flighttracker.modules.airports.persistence.AirportEntity;
import de.perdian.apps.flighttracker.modules.airports.persistence.AirportsRepository;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;

@RestController
public class FlightsSupportController {

    private AirportsRepository airportsRepository = null;

    @RequestMapping("/flights/computedistance")
    public Integer computeDistance(
        @RequestParam("departureAirportCode") String departureAirportCode,
        @RequestParam("arrivalAirportCode") String arrivalAirportCode
    ) {
        AirportEntity departureAirport = this.getAirportsRepository().loadAirportByIataCode(departureAirportCode);
        AirportEntity arrivalAirport = this.getAirportsRepository().loadAirportByIataCode(arrivalAirportCode);
        if (departureAirport == null || arrivalAirport == null) {
            return null;
        } else {
            return FlighttrackerHelper.computeDistanceInKilometers(departureAirport.getLongitude(), departureAirport.getLatitude(), arrivalAirport.getLongitude(), arrivalAirport.getLatitude());
        }
    }

    @RequestMapping("/flights/computeduration")
    public String computeDuration(
        @RequestParam("departureAirportCode") String departureAirportCode,
        @RequestParam("departureDate") String departureDateString,
        @RequestParam("departureTime") String departureTimeString,
        @RequestParam("arrivalAirportCode") String arrivalAirportCode,
        @RequestParam("arrivalDate") String arrivalDateString,
        @RequestParam("arrivalTime") String arrivalTimeString
    ) {
        AirportEntity departureAirport = this.getAirportsRepository().loadAirportByIataCode(departureAirportCode);
        AirportEntity arrivalAirport = this.getAirportsRepository().loadAirportByIataCode(arrivalAirportCode);
        if (departureAirport == null || arrivalAirport == null) {
            return null;
        } else {
            try {

                LocalDate departureDate = FlighttrackerHelper.parseLocalDate(departureDateString);
                LocalTime departureTime = FlighttrackerHelper.parseLocalTime(departureTimeString);
                LocalDate arrivalDate = FlighttrackerHelper.parseLocalDate(arrivalDateString);
                LocalTime arrivalTime = FlighttrackerHelper.parseLocalTime(arrivalTimeString);

                Duration duration = FlighttrackerHelper.computeDuration(departureAirport.getTimezoneId(), departureDate, departureTime, arrivalAirport.getTimezoneId(), arrivalDate, arrivalTime);
                return duration == null ? null : FlighttrackerHelper.formatDuration(duration);

            } catch (Exception e) {
                return null;
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

}
