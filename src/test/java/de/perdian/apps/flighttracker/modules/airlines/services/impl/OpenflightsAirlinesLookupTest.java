package de.perdian.apps.flighttracker.modules.airlines.services.impl;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.DefaultResourceLoader;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;

public class OpenflightsAirlinesLookupTest {

    @Test
    public void loadAirlineByIataCode() {

        AirlineBean airlineEntity = new AirlineBean();
        airlineEntity.setName("Colonial Movers");

        OpenflightsAirlinesLookup repositoryImpl = new OpenflightsAirlinesLookup();
        repositoryImpl.setAirlineBeansByCode(Collections.singletonMap("IATA_CODE", airlineEntity));

        Assertions.assertEquals(airlineEntity, repositoryImpl.loadAirlineByCode("IATA_CODE", Mockito.any()));
        Assertions.assertNull(repositoryImpl.loadAirlineByCode("INVALID", null));
        Assertions.assertNull(repositoryImpl.loadAirlineByCode(null, null));

    }

    @Test
    public void loadAirlineByName() {

        AirlineBean airlineEntity = new AirlineBean();
        airlineEntity.setName("Colonial Movers");

        OpenflightsAirlinesLookup repositoryImpl = new OpenflightsAirlinesLookup();
        repositoryImpl.setAirlineBeansByCode(Collections.singletonMap("CM", airlineEntity));

        Assertions.assertEquals(airlineEntity, repositoryImpl.loadAirlineByName("Colonial Movers", null));
        Assertions.assertNull(repositoryImpl.loadAirlineByName("INVALID", null));
        Assertions.assertNull(repositoryImpl.loadAirlineByName(null, null));

    }

    @Test
    public void initialize() throws IOException {

        OpenflightsAirlinesLookup repositoryImpl = new OpenflightsAirlinesLookup();
        repositoryImpl.setResourceLoader(new DefaultResourceLoader());
        repositoryImpl.initialize();

        Assertions.assertEquals("Lufthansa", repositoryImpl.getAirlineBeansByCode().get("LH").getName());

    }

}
