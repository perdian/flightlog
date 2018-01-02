package de.perdian.apps.flighttracker.modules.airlines.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.airlines.services.AirlinesService;
import de.perdian.apps.flighttracker.modules.airlines.web.AirlineController.AirlineNotFoundException;

public class AirlineControllerTest {

    @Test
    public void doAirline() {

        AirlineBean airlineEntity = Mockito.mock(AirlineBean.class);
        Mockito.when(airlineEntity.getCode()).thenReturn("LH");
        Mockito.when(airlineEntity.getName()).thenReturn("Lufthansa");
        AirlinesService airlinesService = Mockito.mock(AirlinesService.class);
        Mockito.when(airlinesService.loadAirlineByCode(Mockito.eq("LH"), Mockito.any())).thenReturn(airlineEntity);

        AirlineController airlineController = new AirlineController();
        airlineController.setAirlinesService(airlinesService);

        Airline airline = airlineController.doAirline(null, "LH");
        Assertions.assertEquals("LH", airline.getCode());
        Assertions.assertEquals("Lufthansa", airline.getName());

        Mockito.verify(airlinesService).loadAirlineByCode(Mockito.eq("LH"), Mockito.any());
        Mockito.verifyNoMoreInteractions(airlinesService);

    }

    @Test
    public void doAirlineNoAirlineFound() {
        Assertions.assertThrows(AirlineNotFoundException.class, () -> {
            AirlineController airlineController = new AirlineController();
            airlineController.setAirlinesService(Mockito.mock(AirlinesService.class));
            airlineController.doAirline(null, "LH");
        });
    }

}
