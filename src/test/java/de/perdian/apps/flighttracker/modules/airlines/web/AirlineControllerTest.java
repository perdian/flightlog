package de.perdian.apps.flighttracker.modules.airlines.web;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.perdian.apps.flighttracker.modules.airlines.persistence.AirlineEntity;
import de.perdian.apps.flighttracker.modules.airlines.persistence.AirlinesRepository;
import de.perdian.apps.flighttracker.modules.airlines.web.AirlineController.AirlineNotFoundException;

public class AirlineControllerTest {

    @Test
    public void doAirline() {

        AirlineEntity airlineEntity = Mockito.mock(AirlineEntity.class);
        Mockito.when(airlineEntity.getIataCode()).thenReturn("LH");
        Mockito.when(airlineEntity.getName()).thenReturn("Lufthansa");
        AirlinesRepository airlinesRepository = Mockito.mock(AirlinesRepository.class);
        Mockito.when(airlinesRepository.loadAirlineByIataCode(Mockito.eq("LH"))).thenReturn(airlineEntity);

        AirlineController airlineController = new AirlineController();
        airlineController.setAirlinesRepository(airlinesRepository);

        Airline airline = airlineController.doAirline("LH");
        Assert.assertEquals("LH", airline.getCode());
        Assert.assertEquals("Lufthansa", airline.getName());

        Mockito.verify(airlinesRepository).loadAirlineByIataCode(Mockito.eq("LH"));
        Mockito.verifyNoMoreInteractions(airlinesRepository);

    }

    @Test(expected = AirlineNotFoundException.class)
    public void doAirlineNoAirlineFound() {

        AirlineController airlineController = new AirlineController();
        airlineController.setAirlinesRepository(Mockito.mock(AirlinesRepository.class));

        airlineController.doAirline("LH");

    }

}
