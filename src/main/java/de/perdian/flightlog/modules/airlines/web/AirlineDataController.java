package de.perdian.flightlog.modules.airlines.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.perdian.flightlog.modules.airlines.model.AirlineBean;
import de.perdian.flightlog.modules.airlines.services.AirlinesService;
import de.perdian.flightlog.modules.authentication.FlightlogUser;

/**
 * AJAX target controller to deliver information about airlines during the edit process
 *
 * @author Christian Robert
 */

@RestController
public class AirlineDataController {

    private AirlinesService airlinesService = null;

    @RequestMapping(path = "/airline/data/{airlineCode}", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public AirlineData doAirline(FlightlogUser user, @PathVariable("airlineCode") String airlineCode) {
        AirlineBean airlineBean = this.getAirlinesService().loadAirlineByCode(airlineCode, user == null ? null : user.getUserEntity());
        if (airlineBean == null) {
            throw new AirlineNotFoundException();
        } else {
            AirlineData airline = new AirlineData();
            airline.setCode(airlineBean.getCode());
            airline.setName(airlineBean.getName());
            return airline;
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class AirlineNotFoundException extends RuntimeException {

        static final long serialVersionUID = 1L;

    }

    AirlinesService getAirlinesService() {
        return this.airlinesService;
    }
    @Autowired
    void setAirlinesService(AirlinesService airlinesService) {
        this.airlinesService = airlinesService;
    }

}
