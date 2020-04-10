package de.perdian.flightlog.modules.airports.persistence;

import java.io.IOException;
import java.util.Collections;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

public class AirportsRepositoryImplTest {

    @Test
    public void loadAirportByIataCode() {

        AirportEntity airportEntity = new AirportEntity();

        AirportsRepositoryImpl repositoryImpl = new AirportsRepositoryImpl();
        repositoryImpl.setAirportBeansByIataCode(Collections.singletonMap("CGN", airportEntity));

        Assertions.assertEquals(airportEntity, repositoryImpl.loadAirportByIataCode("CGN"));
        Assertions.assertNull(repositoryImpl.loadAirportByIataCode("INVALID"));
        Assertions.assertNull(repositoryImpl.loadAirportByIataCode(null));

    }

    @Test
    public void initialize() throws IOException {

        AirportsRepositoryImpl repositoryImpl = new AirportsRepositoryImpl();
        repositoryImpl.setResourceLoader(new DefaultResourceLoader());
        repositoryImpl.initialize();

        MatcherAssert.assertThat(repositoryImpl.getAirportBeansByIataCode().size(), Matchers.greaterThan(0));
        Assertions.assertEquals("Cologne Bonn Airport", repositoryImpl.getAirportBeansByIataCode().get("CGN").getName());

    }

}
