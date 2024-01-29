package de.perdian.flightlog.modules.airports.web;

import de.perdian.flightlog.modules.airports.persistence.AirportEntity;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.modules.flights.shared.model.Airport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AirportsController {

    private AirportsRepository airportsRepository = null;

    @GetMapping(path = "/airport/{airportCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Airport> doAirport(@PathVariable String airportCode) {
        AirportEntity airportEntity = this.getAirportsRepository().loadAirportByIataCode(airportCode);
        return airportEntity == null ? ResponseEntity.noContent().build() : ResponseEntity.ofNullable(new Airport(airportEntity));
    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

}
