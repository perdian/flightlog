package de.perdian.flightlog.modules.airports.web;

import de.perdian.flightlog.modules.airports.model.Airport;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
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
        Airport airport = this.getAirportsRepository().loadAirportByIataCode(airportCode);
        return airport == null ? ResponseEntity.noContent().build() : ResponseEntity.ofNullable(airport);
    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

}
