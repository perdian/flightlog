package de.perdian.apps.flighttracker.modules.airlines.persistence;

import java.io.IOException;
import java.util.Collections;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

public class AirlinesRepositoryImplTest {

    @Test
    public void loadAirlineByIataCode() {

        AirlineEntity airlineEntity = new AirlineEntity();
        airlineEntity.setName("Colonial Movers");

        AirlinesRepositoryImpl repositoryImpl = new AirlinesRepositoryImpl();
        repositoryImpl.setAirlineBeansByIataCode(Collections.singletonMap("IATA_CODE", airlineEntity));

        Assertions.assertEquals(airlineEntity, repositoryImpl.loadAirlineByIataCode("IATA_CODE"));
        Assertions.assertNull(repositoryImpl.loadAirlineByIataCode("INVALID"));
        Assertions.assertNull(repositoryImpl.loadAirlineByIataCode(null));

    }

    @Test
    public void loadAirlineByName() {

        AirlineEntity airlineEntity = new AirlineEntity();
        airlineEntity.setName("Colonial Movers");

        AirlinesRepositoryImpl repositoryImpl = new AirlinesRepositoryImpl();
        repositoryImpl.setAirlineBeans(Collections.singletonList(airlineEntity));

        Assertions.assertEquals(airlineEntity, repositoryImpl.loadAirlineByName("Colonial Movers"));
        Assertions.assertNull(repositoryImpl.loadAirlineByName("INVALID"));
        Assertions.assertNull(repositoryImpl.loadAirlineByName(null));

    }

    @Test
    public void initialize() throws IOException {

        AirlinesRepositoryImpl repositoryImpl = new AirlinesRepositoryImpl();
        repositoryImpl.setResourceLoader(new DefaultResourceLoader());
        repositoryImpl.initialize();

        MatcherAssert.assertThat(repositoryImpl.getAirlineBeans().size(), Matchers.greaterThan(0));
        MatcherAssert.assertThat(repositoryImpl.getAirlineBeansByIataCode().size(), Matchers.greaterThan(0));
        Assertions.assertEquals("Lufthansa", repositoryImpl.getAirlineBeansByIataCode().get("LH").getName());

    }

}
