package de.perdian.flightlog.modules.airlines.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.perdian.flightlog.modules.airlines.persistence.AirlineEntity;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;

/**
 * AJAX target controller to deliver information about airlines during the edit process
 *
 * @author Christian Seifert
 */

@RestController
public class AirlineDataController {

    private AirlinesRepository airlinesRepository = null;

    @RequestMapping(path = "/airline/data/{airlineCode}", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public AirlineData doAirline(@PathVariable("airlineCode") String airlineCode) {
        AirlineEntity airlineEntity = this.getAirlinesRepository().loadAirlineByCode(airlineCode);
        if (airlineEntity == null) {
            throw new AirlineNotFoundException();
        } else {
            AirlineData airline = new AirlineData();
            airline.setCode(airlineEntity.getCode());
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
