package de.perdian.apps.flighttracker.modules.airlines.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.airlines.persistence.AirlineRepository;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

@Service
class AirlinesServiceImpl implements AirlinesService {

    private List<AirlinesLookup> airlinesLookups = null;
    private AirlineRepository airlineRepository = null;

    @Override
    public AirlineBean loadAirlineByCode(String airlineCode, UserEntity user) {
        return this.getAirlinesLookups().stream()
            .map(lookup -> lookup.loadAirlineByCode(airlineCode, user))
            .filter(airline -> airline != null)
            .findFirst()
            .orElse(null);
    }

    @Override
    public AirlineBean loadAirlineByName(String airlineName, UserEntity user) {
        return this.getAirlinesLookups().stream()
            .map(lookup -> lookup.loadAirlineByName(airlineName, user))
            .filter(airline -> airline != null)
            .findFirst()
            .orElse(null);
    }

    List<AirlinesLookup> getAirlinesLookups() {
        return this.airlinesLookups;
    }
    @Autowired
    void setAirlinesLookups(List<AirlinesLookup> airlinesLookups) {
        this.airlinesLookups = airlinesLookups;
    }

    AirlineRepository getAirlineRepository() {
        return this.airlineRepository;
    }
    @Autowired
    void setAirlineRepository(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

}
