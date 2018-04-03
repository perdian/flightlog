package de.perdian.flightlog.modules.flights.web;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.perdian.flightlog.modules.airports.persistence.AirportEntity;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.support.FlightlogHelper;

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
            return FlightlogHelper.computeDistanceInKilometers(departureAirport.getLongitude(), departureAirport.getLatitude(), arrivalAirport.getLongitude(), arrivalAirport.getLatitude());
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

                LocalDate departureDate = FlightlogHelper.parseLocalDate(departureDateString);
                LocalTime departureTime = FlightlogHelper.parseLocalTime(departureTimeString);
                LocalDate arrivalDate = FlightlogHelper.parseLocalDate(arrivalDateString);
                LocalTime arrivalTime = FlightlogHelper.parseLocalTime(arrivalTimeString);

                Duration duration = FlightlogHelper.computeDuration(departureAirport.getTimezoneId(), departureDate, departureTime, arrivalAirport.getTimezoneId(), arrivalDate, arrivalTime);
                return duration == null ? null : FlightlogHelper.formatDuration(duration);

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
