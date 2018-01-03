package de.perdian.apps.flighttracker.modules.airlines.web;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ExtendedModelMap;

import de.perdian.apps.flighttracker.FlighttrackerTestHelper;
import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.airlines.services.AirlinesService;
import de.perdian.apps.flighttracker.modules.security.web.FlighttrackerUser;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

public class AirlineEditControllerTest {

    @Test
    public void doListGet() {

        UserEntity userEntity = new UserEntity();
        FlighttrackerUser user = new FlighttrackerUser();
        user.setUserEntity(userEntity);

        AirlineBean airline1 = FlighttrackerTestHelper.createAirlineBean("UA", "US", "United");
        AirlineBean airline2 = FlighttrackerTestHelper.createAirlineBean("LH", "DE", "Lufthansa");
        AirlinesService airlinesService = Mockito.mock(AirlinesService.class);
        Mockito.when(airlinesService.loadUserSpecificAirlines(Mockito.eq(userEntity))).thenReturn(Arrays.asList(airline1, airline2));

        ExtendedModelMap model = new ExtendedModelMap();

        AirlineEditController controller = new AirlineEditController();
        controller.setAirlinesService(airlinesService);
        controller.doListGet(user, model);

        Assertions.assertNotNull(model.get("airlines"));
        Assertions.assertTrue(model.get("airlines") instanceof AirlineEditorList);
        Assertions.assertEquals(airline1, ((AirlineEditorList)model.get("airlines")).getAirlines().get(0).getAirlineBean());
        Assertions.assertEquals(airline2, ((AirlineEditorList)model.get("airlines")).getAirlines().get(1).getAirlineBean());

    }

}
