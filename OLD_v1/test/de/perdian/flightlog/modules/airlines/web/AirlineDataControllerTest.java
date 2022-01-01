package de.perdian.flightlog.modules.airlines.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.perdian.flightlog.modules.airlines.persistence.AirlineEntity;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;
import de.perdian.flightlog.modules.airlines.web.AirlineDataController.AirlineNotFoundException;

public class AirlineDataControllerTest {

    @Test
    public void doAirline() {

        AirlineEntity airlineEntity = Mockito.mock(AirlineEntity.class);
        Mockito.when(airlineEntity.getCode()).thenReturn("LH");
        Mockito.when(airlineEntity.getName()).thenReturn("Lufthansa");
        AirlinesRepository airlinesRepository = Mockito.mock(AirlinesRepository.class);
        Mockito.when(airlinesRepository.loadAirlineByCode(Mockito.eq("LH"))).thenReturn(airlineEntity);

        AirlineDataController airlineController = new AirlineDataController();
        airlineController.setAirlinesRepository(airlinesRepository);

        AirlineData airline = airlineController.doAirline("LH");
        Assertions.assertEquals("LH", airline.getCode());
        Assertions.assertEquals("Lufthansa", airline.getName());

        Mockito.verify(airlinesRepository).loadAirlineByCode(Mockito.eq("LH"));
        Mockito.verifyNoMoreInteractions(airlinesRepository);

    }

    @Test
    public void doAirlineNoAirlineFound() {
        Assertions.assertThrows(AirlineNotFoundException.class, () -> {
            AirlineDataController airlineController = new AirlineDataController();
            airlineController.setAirlinesRepository(Mockito.mock(AirlinesRepository.class));
            airlineController.doAirline("LH");
        });
    }

}
