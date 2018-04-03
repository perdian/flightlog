package de.perdian.flightlog.modules.airlines.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.perdian.flightlog.modules.airlines.model.AirlineBean;
import de.perdian.flightlog.modules.airlines.services.AirlinesService;
import de.perdian.flightlog.modules.airlines.web.AirlineData;
import de.perdian.flightlog.modules.airlines.web.AirlineDataController;
import de.perdian.flightlog.modules.airlines.web.AirlineDataController.AirlineNotFoundException;

public class AirlineDataControllerTest {

    @Test
    public void doAirline() {

        AirlineBean airlineEntity = Mockito.mock(AirlineBean.class);
        Mockito.when(airlineEntity.getCode()).thenReturn("LH");
        Mockito.when(airlineEntity.getName()).thenReturn("Lufthansa");
        AirlinesService airlinesService = Mockito.mock(AirlinesService.class);
        Mockito.when(airlinesService.loadAirlineByCode(Mockito.eq("LH"), Mockito.any())).thenReturn(airlineEntity);

        AirlineDataController airlineController = new AirlineDataController();
        airlineController.setAirlinesService(airlinesService);

        AirlineData airline = airlineController.doAirline(null, "LH");
        Assertions.assertEquals("LH", airline.getCode());
        Assertions.assertEquals("Lufthansa", airline.getName());

        Mockito.verify(airlinesService).loadAirlineByCode(Mockito.eq("LH"), Mockito.any());
        Mockito.verifyNoMoreInteractions(airlinesService);

    }

    @Test
    public void doAirlineNoAirlineFound() {
        Assertions.assertThrows(AirlineNotFoundException.class, () -> {
            AirlineDataController airlineController = new AirlineDataController();
            airlineController.setAirlinesService(Mockito.mock(AirlinesService.class));
            airlineController.doAirline(null, "LH");
        });
    }

}
