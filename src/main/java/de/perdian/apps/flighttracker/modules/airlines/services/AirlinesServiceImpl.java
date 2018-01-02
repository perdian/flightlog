package de.perdian.apps.flighttracker.modules.airlines.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.airlines.persistence.AirlinesRepository;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

@Service
class AirlinesServiceImpl implements AirlinesService {

    private List<AirlinesLookup> airlinesLookups = null;
    private AirlinesRepository airlinesRepository = null;

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

    // TODO: Add update and delete methods to allow managing user specific values

    List<AirlinesLookup> getAirlinesLookups() {
        return this.airlinesLookups;
    }
    @Autowired
    void setAirlinesLookups(List<AirlinesLookup> airlinesLookups) {
        this.airlinesLookups = airlinesLookups;
    }

    AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

}
