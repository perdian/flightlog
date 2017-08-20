package de.perdian.apps.flighttracker.web.modules.airlines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.perdian.apps.flighttracker.persistence.entities.AirlineEntity;
import de.perdian.apps.flighttracker.persistence.repositories.AirlinesRepository;

/**
 * AJAX target controller to deliver information about airlines during the edit process
 *
 * @author Christian Robert
 */

@RestController
public class AirlineController {

    private AirlinesRepository airlinesRepository = null;

    @RequestMapping(path = "/airline/{airlineCode}", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public Airline doAirline(@PathVariable("airlineCode") String airlineCode) {
        AirlineEntity airlineEntity = this.getAirlinesRepository().loadAirlineByIataCode(airlineCode);
        if (airlineEntity == null) {
            throw new AirlineNotFoundException();
        } else {
            Airline airline = new Airline();
            airline.setCode(airlineEntity.getIataCode());
            airline.setName(airlineEntity.getName());
            return airline;
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class AirlineNotFoundException extends RuntimeException {

        static final long serialVersionUID = 1L;

    }

    AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

}
