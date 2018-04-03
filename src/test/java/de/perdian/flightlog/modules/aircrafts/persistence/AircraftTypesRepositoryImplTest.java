package de.perdian.flightlog.modules.aircrafts.persistence;

import java.io.IOException;
import java.util.Collections;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import de.perdian.flightlog.modules.aircrafts.persistence.AircraftTypeEntity;
import de.perdian.flightlog.modules.aircrafts.persistence.AircraftTypesRepositoryImpl;

public class AircraftTypesRepositoryImplTest {

    @Test
    public void loadAircraftTypeByCode() {

        AircraftTypeEntity aircraftTypeEntity = new AircraftTypeEntity();
        AircraftTypesRepositoryImpl repositoryImpl = new AircraftTypesRepositoryImpl();
        repositoryImpl.setAircraftTypeBeansByIataCode(Collections.singletonMap("IATA_CODE", aircraftTypeEntity));
        repositoryImpl.setAircraftTypeBeansByIcaoCode(Collections.singletonMap("ICAO_CODE", aircraftTypeEntity));

        Assertions.assertEquals(aircraftTypeEntity, repositoryImpl.loadAircraftTypeByCode("IATA_CODE"));
        Assertions.assertEquals(aircraftTypeEntity, repositoryImpl.loadAircraftTypeByCode("ICAO_CODE"));
        Assertions.assertNull(repositoryImpl.loadAircraftTypeByCode("INVALID"));
        Assertions.assertNull(repositoryImpl.loadAircraftTypeByCode(null));

    }

    @Test
    public void loadAircraftTypeByIataCode() {

        AircraftTypeEntity aircraftTypeEntity = new AircraftTypeEntity();
        AircraftTypesRepositoryImpl repositoryImpl = new AircraftTypesRepositoryImpl();
        repositoryImpl.setAircraftTypeBeansByIataCode(Collections.singletonMap("IATA_CODE", aircraftTypeEntity));
        repositoryImpl.setAircraftTypeBeansByIcaoCode(Collections.singletonMap("ICAO_CODE", aircraftTypeEntity));

        Assertions.assertEquals(aircraftTypeEntity, repositoryImpl.loadAircraftTypeByIataCode("IATA_CODE"));
        Assertions.assertNull(repositoryImpl.loadAircraftTypeByIataCode("ICAO_CODE"));
        Assertions.assertNull(repositoryImpl.loadAircraftTypeByIataCode("INVALID"));
        Assertions.assertNull(repositoryImpl.loadAircraftTypeByIataCode(null));

    }

    @Test
    public void loadAircraftTypeByIcaoCode() {

        AircraftTypeEntity aircraftTypeEntity = new AircraftTypeEntity();
        AircraftTypesRepositoryImpl repositoryImpl = new AircraftTypesRepositoryImpl();
        repositoryImpl.setAircraftTypeBeansByIataCode(Collections.singletonMap("IATA_CODE", aircraftTypeEntity));
        repositoryImpl.setAircraftTypeBeansByIcaoCode(Collections.singletonMap("ICAO_CODE", aircraftTypeEntity));

        Assertions.assertNull(repositoryImpl.loadAircraftTypeByIcaoCode("IATA_CODE"));
        Assertions.assertEquals(aircraftTypeEntity, repositoryImpl.loadAircraftTypeByIcaoCode("ICAO_CODE"));
        Assertions.assertNull(repositoryImpl.loadAircraftTypeByIcaoCode("INVALID"));
        Assertions.assertNull(repositoryImpl.loadAircraftTypeByIcaoCode(null));

    }

    @Test
    public void initialize() throws IOException {

        AircraftTypesRepositoryImpl repositoryImpl = new AircraftTypesRepositoryImpl();
        repositoryImpl.setResourceLoader(new DefaultResourceLoader());
        repositoryImpl.initialize();

        MatcherAssert.assertThat(repositoryImpl.getAircraftTypeBeansByIataCode().size(), Matchers.greaterThan(0));
        Assertions.assertEquals("Airbus A380-800", repositoryImpl.getAircraftTypeBeansByIataCode().get("388").getTitle());
        Assertions.assertEquals("Airbus A380-800", repositoryImpl.getAircraftTypeBeansByIcaoCode().get("A388").getTitle());

    }

}
