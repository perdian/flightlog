package de.perdian.flightlog.modules.airlines.persistence;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

public class AirlinesRepositoryImplTest {

    @Test
    public void loadAirlineByIataCode() {

        AirlineEntity airlineEntity = new AirlineEntity();
        airlineEntity.setName("Colonial Movers");

        AirlinesRepositoryImpl repositoryImpl = new AirlinesRepositoryImpl();
        repositoryImpl.setAirlineBeansByCode(Collections.singletonMap("IATA_CODE", airlineEntity));

        Assertions.assertEquals(airlineEntity, repositoryImpl.loadAirlineByCode("IATA_CODE"));
        Assertions.assertNull(repositoryImpl.loadAirlineByCode("INVALID"));
        Assertions.assertNull(repositoryImpl.loadAirlineByCode(null));

    }

    @Test
    public void loadAirlineByName() {

        AirlineEntity airlineEntity = new AirlineEntity();
        airlineEntity.setName("Colonial Movers");

        AirlinesRepositoryImpl repositoryImpl = new AirlinesRepositoryImpl();
        repositoryImpl.setAirlineBeansByCode(Collections.singletonMap("CM", airlineEntity));

        Assertions.assertEquals(airlineEntity, repositoryImpl.loadAirlineByName("Colonial Movers"));
        Assertions.assertNull(repositoryImpl.loadAirlineByName("INVALID"));
        Assertions.assertNull(repositoryImpl.loadAirlineByName(null));

    }

    @Test
    public void initialize() throws IOException {

        AirlinesRepositoryImpl repositoryImpl = new AirlinesRepositoryImpl();
        repositoryImpl.setResourceLoader(new DefaultResourceLoader());
        repositoryImpl.initialize();

        Assertions.assertEquals("Lufthansa", repositoryImpl.getAirlineBeansByCode().get("LH").getName());

    }

}
