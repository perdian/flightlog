package de.perdian.flightlog.modules.airports.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.perdian.flightlog.modules.airports.persistence.AirportEntity;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;

/**
 * AJAX target controller to deliver information about airports during the edit process
 *
 * @author Christian Seifert
 */

@RestController
public class AirportController {

    private AirportsRepository airportsRepository = null;

    @RequestMapping(path = "/airport/{airportCode}", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public Airport doAirport(@PathVariable("airportCode") String airportCode) {
        AirportEntity airportEntity = this.getAirportsRepository().loadAirportByIataCode(airportCode);
        if (airportEntity == null) {
            throw new AirportNotFoundException();
        } else {
            Airport airport = new Airport();
            airport.setCode(airportEntity.getIataCode());
            airport.setCountryCode(airportEntity.getCountryCode());
            airport.setLatitude(airportEntity.getLatitude());
            airport.setLongitude(airportEntity.getLongitude());
            airport.setName(airportEntity.getName());
            airport.setTimezoneId(airportEntity.getTimezoneId() == null ? null : airportEntity.getTimezoneId().toString());
            return airport;
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class AirportNotFoundException extends RuntimeException {

        static final long serialVersionUID = 1L;

    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

}
