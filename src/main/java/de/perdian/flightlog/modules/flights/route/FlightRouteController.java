package de.perdian.flightlog.modules.flights.route;

import de.perdian.flightlog.modules.airports.model.Airport;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.support.FlightlogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;

@RestController
class FlightRouteController {

    private AirportsRepository airportsRepository = null;

    @PostMapping(path = "/flights/route", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<FlightRoute> doRoute(
        @RequestParam(name = "departureAirportCode") String departureAirportCode,
        @RequestParam(name = "departureDateLocal", required = false) LocalDate departureDateLocal,
        @RequestParam(name = "departureTimeLocal", required = false) LocalTime departureTimeLocal,
        @RequestParam(name = "arrivalAirportCode") String arrivalAirportCode,
        @RequestParam(name = "arrivalDateLocal", required = false) LocalDate arrivalDateLocal,
        @RequestParam(name = "arrivalTimeLocal", required = false) LocalTime arrivalTimeLocal
    ) {

        Airport departureAirport = this.getAirportsRepository().loadAirportByIataCode(departureAirportCode);
        Airport arrivalAirport = this.getAirportsRepository().loadAirportByIataCode(arrivalAirportCode);
        if (departureAirport == null || arrivalAirport == null) {
            return ResponseEntity.notFound().build();
        } else {

            LocalDateTime localDepartureDateTime = departureDateLocal == null || departureTimeLocal == null ? null : departureTimeLocal.atDate(departureDateLocal);
            ZonedDateTime zonedDepartureDateTime = localDepartureDateTime == null || departureAirport.getTimezoneId() == null ? null : localDepartureDateTime.atZone(departureAirport.getTimezoneId());
            LocalDateTime localArrivalDateTime = arrivalDateLocal == null || arrivalTimeLocal == null ? null : arrivalTimeLocal.atDate(arrivalDateLocal);
            ZonedDateTime zonedArrivalDateTime = localArrivalDateTime == null || arrivalAirport.getTimezoneId() == null ? null : localArrivalDateTime.atZone(arrivalAirport.getTimezoneId());

            FlightRoute flightRoute = new FlightRoute();
            flightRoute.setDepartureAirport(departureAirport);
            flightRoute.setArrivalAirport(arrivalAirport);
            flightRoute.setDistance(FlightlogHelper.computeDistanceInKilometers(departureAirport.getLongitude(), departureAirport.getLatitude(), arrivalAirport.getLongitude(), arrivalAirport.getLatitude()));
            flightRoute.setDuration(zonedDepartureDateTime == null || zonedArrivalDateTime == null ? null : Duration.between(zonedDepartureDateTime, zonedArrivalDateTime));
            return ResponseEntity.ofNullable(flightRoute);

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
