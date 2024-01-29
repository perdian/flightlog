package de.perdian.flightlog.modules.flights.support;

import de.perdian.flightlog.modules.airports.persistence.AirportEntity;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.modules.flights.shared.model.Airport;
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

        AirportEntity departureAirportEntity = this.getAirportsRepository().loadAirportByIataCode(departureAirportCode);
        AirportEntity arrivalAirportEntity = this.getAirportsRepository().loadAirportByIataCode(arrivalAirportCode);
        if (departureAirportEntity == null || arrivalAirportEntity == null) {
            return ResponseEntity.notFound().build();
        } else {

            LocalDateTime localDepartureDateTime = departureDateLocal == null || departureTimeLocal == null ? null : departureTimeLocal.atDate(departureDateLocal);
            ZonedDateTime zonedDepartureDateTime = localDepartureDateTime == null || departureAirportEntity.getTimezoneId() == null ? null : localDepartureDateTime.atZone(departureAirportEntity.getTimezoneId());
            LocalDateTime localArrivalDateTime = arrivalDateLocal == null || arrivalTimeLocal == null ? null : arrivalTimeLocal.atDate(arrivalDateLocal);
            ZonedDateTime zonedArrivalDateTime = localArrivalDateTime == null || arrivalAirportEntity.getTimezoneId() == null ? null : localArrivalDateTime.atZone(arrivalAirportEntity.getTimezoneId());

            FlightRoute flightRoute = new FlightRoute();
            flightRoute.setDepartureAirport(new Airport(departureAirportEntity));
            flightRoute.setArrivalAirport(new Airport(arrivalAirportEntity));
            flightRoute.setDistance(FlightlogHelper.computeDistanceInKilometers(departureAirportEntity.getLongitude(), departureAirportEntity.getLatitude(), arrivalAirportEntity.getLongitude(), arrivalAirportEntity.getLatitude()));
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
