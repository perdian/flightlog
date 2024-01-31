package de.perdian.flightlog.modules.airlines.web;

import de.perdian.flightlog.modules.airlines.model.Airline;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AirlinesController {

    private AirlinesRepository airlinesRepository = null;

    @GetMapping(path = "/airline/{airlineCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Airline> doAirline(@PathVariable String airlineCode) {
        Airline airline = this.getAirlinesRepository().loadAirlineByCode(airlineCode);
        return airline == null ? ResponseEntity.noContent().build() : ResponseEntity.ofNullable(airline);
    }

    AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

}
