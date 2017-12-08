package de.perdian.apps.flighttracker.modules.airports.persistence;

import java.io.IOException;
import java.util.Collections;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;

public class AirportsRepositoryImplTest {

    @Test
    public void loadAirportByIataCode() {

        AirportEntity airportEntity = new AirportEntity();

        AirportsRepositoryImpl repositoryImpl = new AirportsRepositoryImpl();
        repositoryImpl.setAirportBeansByIataCode(Collections.singletonMap("CGN", airportEntity));

        Assert.assertEquals(airportEntity, repositoryImpl.loadAirportByIataCode("CGN"));
        Assert.assertNull(repositoryImpl.loadAirportByIataCode("INVALID"));
        Assert.assertNull(repositoryImpl.loadAirportByIataCode(null));

    }

    @Test
    public void initialize() throws IOException {

        AirportsRepositoryImpl repositoryImpl = new AirportsRepositoryImpl();
        repositoryImpl.setResourceLoader(new DefaultResourceLoader());
        repositoryImpl.initialize();

        Assert.assertThat(repositoryImpl.getAirportBeansByIataCode().size(), Matchers.greaterThan(0));
        Assert.assertEquals("Cologne Bonn Airport", repositoryImpl.getAirportBeansByIataCode().get("CGN").getName());

    }

}
