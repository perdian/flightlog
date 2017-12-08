package de.perdian.apps.flighttracker.modules.aircrafts.persistence;

import java.io.IOException;
import java.util.Collections;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;

public class AircraftTypesRepositoryImplTest {

    @Test
    public void loadAircraftTypeByCode() {

        AircraftTypeEntity aircraftTypeEntity = new AircraftTypeEntity();
        AircraftTypesRepositoryImpl repositoryImpl = new AircraftTypesRepositoryImpl();
        repositoryImpl.setAircraftTypeBeansByIataCode(Collections.singletonMap("IATA_CODE", aircraftTypeEntity));
        repositoryImpl.setAircraftTypeBeansByIcaoCode(Collections.singletonMap("ICAO_CODE", aircraftTypeEntity));

        Assert.assertEquals(aircraftTypeEntity, repositoryImpl.loadAircraftTypeByCode("IATA_CODE"));
        Assert.assertEquals(aircraftTypeEntity, repositoryImpl.loadAircraftTypeByCode("ICAO_CODE"));
        Assert.assertNull(repositoryImpl.loadAircraftTypeByCode("INVALID"));
        Assert.assertNull(repositoryImpl.loadAircraftTypeByCode(null));

    }

    @Test
    public void loadAircraftTypeByIataCode() {

        AircraftTypeEntity aircraftTypeEntity = new AircraftTypeEntity();
        AircraftTypesRepositoryImpl repositoryImpl = new AircraftTypesRepositoryImpl();
        repositoryImpl.setAircraftTypeBeansByIataCode(Collections.singletonMap("IATA_CODE", aircraftTypeEntity));
        repositoryImpl.setAircraftTypeBeansByIcaoCode(Collections.singletonMap("ICAO_CODE", aircraftTypeEntity));

        Assert.assertEquals(aircraftTypeEntity, repositoryImpl.loadAircraftTypeByIataCode("IATA_CODE"));
        Assert.assertNull(repositoryImpl.loadAircraftTypeByIataCode("ICAO_CODE"));
        Assert.assertNull(repositoryImpl.loadAircraftTypeByIataCode("INVALID"));
        Assert.assertNull(repositoryImpl.loadAircraftTypeByIataCode(null));

    }

    @Test
    public void loadAircraftTypeByIcaoCode() {

        AircraftTypeEntity aircraftTypeEntity = new AircraftTypeEntity();
        AircraftTypesRepositoryImpl repositoryImpl = new AircraftTypesRepositoryImpl();
        repositoryImpl.setAircraftTypeBeansByIataCode(Collections.singletonMap("IATA_CODE", aircraftTypeEntity));
        repositoryImpl.setAircraftTypeBeansByIcaoCode(Collections.singletonMap("ICAO_CODE", aircraftTypeEntity));

        Assert.assertNull(repositoryImpl.loadAircraftTypeByIcaoCode("IATA_CODE"));
        Assert.assertEquals(aircraftTypeEntity, repositoryImpl.loadAircraftTypeByIcaoCode("ICAO_CODE"));
        Assert.assertNull(repositoryImpl.loadAircraftTypeByIcaoCode("INVALID"));
        Assert.assertNull(repositoryImpl.loadAircraftTypeByIcaoCode(null));

    }

    @Test
    public void initialize() throws IOException {

        AircraftTypesRepositoryImpl repositoryImpl = new AircraftTypesRepositoryImpl();
        repositoryImpl.setResourceLoader(new DefaultResourceLoader());
        repositoryImpl.initialize();

        Assert.assertThat(repositoryImpl.getAircraftTypeBeansByIataCode().size(), Matchers.greaterThan(0));
        Assert.assertEquals("Airbus A380-800", repositoryImpl.getAircraftTypeBeansByIataCode().get("388").getTitle());
        Assert.assertEquals("Airbus A380-800", repositoryImpl.getAircraftTypeBeansByIcaoCode().get("A388").getTitle());

    }

}
