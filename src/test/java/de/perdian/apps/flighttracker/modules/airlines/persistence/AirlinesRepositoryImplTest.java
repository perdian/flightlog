package de.perdian.apps.flighttracker.modules.airlines.persistence;

import java.io.IOException;
import java.util.Collections;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;

public class AirlinesRepositoryImplTest {

    @Test
    public void loadAirlineByIataCode() {

        AirlineEntity airlineEntity = new AirlineEntity();
        airlineEntity.setName("Colonial Movers");

        AirlinesRepositoryImpl repositoryImpl = new AirlinesRepositoryImpl();
        repositoryImpl.setAirlineBeansByIataCode(Collections.singletonMap("IATA_CODE", airlineEntity));

        Assert.assertEquals(airlineEntity, repositoryImpl.loadAirlineByIataCode("IATA_CODE"));
        Assert.assertNull(repositoryImpl.loadAirlineByIataCode("INVALID"));
        Assert.assertNull(repositoryImpl.loadAirlineByIataCode(null));

    }

    @Test
    public void loadAirlineByName() {

        AirlineEntity airlineEntity = new AirlineEntity();
        airlineEntity.setName("Colonial Movers");

        AirlinesRepositoryImpl repositoryImpl = new AirlinesRepositoryImpl();
        repositoryImpl.setAirlineBeans(Collections.singletonList(airlineEntity));

        Assert.assertEquals(airlineEntity, repositoryImpl.loadAirlineByName("Colonial Movers"));
        Assert.assertNull(repositoryImpl.loadAirlineByName("INVALID"));
        Assert.assertNull(repositoryImpl.loadAirlineByName(null));

    }

    @Test
    public void initialize() throws IOException {

        AirlinesRepositoryImpl repositoryImpl = new AirlinesRepositoryImpl();
        repositoryImpl.setResourceLoader(new DefaultResourceLoader());
        repositoryImpl.initialize();

        Assert.assertThat(repositoryImpl.getAirlineBeans().size(), Matchers.greaterThan(0));
        Assert.assertThat(repositoryImpl.getAirlineBeansByIataCode().size(), Matchers.greaterThan(0));
        Assert.assertEquals("Lufthansa", repositoryImpl.getAirlineBeansByIataCode().get("LH").getName());

    }

}
