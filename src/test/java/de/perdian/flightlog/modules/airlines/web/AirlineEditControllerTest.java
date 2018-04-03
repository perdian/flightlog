package de.perdian.flightlog.modules.airlines.web;

import java.util.Arrays;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.ui.ExtendedModelMap;

import de.perdian.flightlog.FlightlogTestHelper;
import de.perdian.flightlog.modules.airlines.model.AirlineBean;
import de.perdian.flightlog.modules.airlines.services.AirlinesService;
import de.perdian.flightlog.modules.airlines.web.AirlineEditController;
import de.perdian.flightlog.modules.airlines.web.AirlineEditor;
import de.perdian.flightlog.modules.airlines.web.AirlineEditorList;
import de.perdian.flightlog.modules.security.web.FlightlogUser;
import de.perdian.flightlog.modules.users.persistence.UserEntity;
import de.perdian.flightlog.support.web.Messages;

public class AirlineEditControllerTest {

    @Test
    public void doListGet() {

        UserEntity userEntity = new UserEntity();
        FlightlogUser user = new FlightlogUser();
        user.setUserEntity(userEntity);

        AirlineBean airline1 = FlightlogTestHelper.createAirlineBean("UA", "US", "United");
        AirlineBean airline2 = FlightlogTestHelper.createAirlineBean("LH", "DE", "Lufthansa");
        AirlinesService airlinesService = Mockito.mock(AirlinesService.class);
        Mockito.when(airlinesService.loadUserSpecificAirlines(Mockito.eq(userEntity))).thenReturn(Arrays.asList(airline1, airline2));

        ExtendedModelMap model = new ExtendedModelMap();

        AirlineEditController controller = new AirlineEditController();
        controller.setAirlinesService(airlinesService);
        controller.doListGet(user, model);

        Assertions.assertNotNull(model.get("airlines"));
        Assertions.assertTrue(model.get("airlines") instanceof AirlineEditorList);
        Assertions.assertEquals(airline1, ((AirlineEditorList)model.get("airlines")).getItems().get(0).getAirlineBean());
        Assertions.assertEquals(airline2, ((AirlineEditorList)model.get("airlines")).getItems().get(1).getAirlineBean());

    }

    @Test
    public void doListPost() {

        UserEntity userEntity = new UserEntity();
        FlightlogUser user = new FlightlogUser();
        user.setUserEntity(userEntity);

        AirlineBean airline1 = FlightlogTestHelper.createAirlineBean("UA", "US", "United");
        AirlineBean airline2 = FlightlogTestHelper.createAirlineBean("LH", "DE", "Lufthansa");
        AirlinesService airlinesService = Mockito.mock(AirlinesService.class);
        Mockito.when(airlinesService.loadUserSpecificAirlines(Mockito.eq(userEntity))).thenReturn(Arrays.asList(airline1, airline2));

        ExtendedModelMap model = new ExtendedModelMap();

        AirlineEditor airlineEditor1 = new AirlineEditor();
        airlineEditor1.setAirlineBean(FlightlogTestHelper.createAirlineBean("LH", "DE", "Lufthansa"));
        AirlineEditor airlineEditor2 = new AirlineEditor();
        airlineEditor2.setAirlineBean(FlightlogTestHelper.createAirlineBean("AB", "DE", "Air Berlin"));
        airlineEditor2.setDelete(true);
        AirlineEditor newAirlineEditor = new AirlineEditor();
        newAirlineEditor.setAirlineBean(FlightlogTestHelper.createAirlineBean("UA", "US", "United"));
        AirlineEditorList airlineEditorList = new AirlineEditorList();
        airlineEditorList.setItems(Arrays.asList(airlineEditor1, airlineEditor2));
        airlineEditorList.setNewItem(newAirlineEditor);

        Messages messages = new Messages();
        AirlineEditController controller = new AirlineEditController();
        controller.setMessageSource(Mockito.mock(MessageSource.class));
        controller.setAirlinesService(airlinesService);
        controller.doListPost(user, airlineEditorList, messages, Locale.GERMANY, model);

        Mockito.verify(airlinesService, Mockito.times(1)).updateUserSpecificAirline(Mockito.eq(userEntity), Mockito.eq(airlineEditor1.getAirlineBean()));
        Mockito.verify(airlinesService, Mockito.times(1)).updateUserSpecificAirline(Mockito.eq(userEntity), Mockito.eq(newAirlineEditor.getAirlineBean()));
        Mockito.verify(airlinesService, Mockito.times(1)).deleteUserSpecificAirline(Mockito.eq(userEntity), Mockito.eq(airlineEditor2.getAirlineBean()));
        Mockito.verify(airlinesService, Mockito.times(1)).loadUserSpecificAirlines(Mockito.eq(userEntity));
        Mockito.verifyNoMoreInteractions(airlinesService);

    }

}
