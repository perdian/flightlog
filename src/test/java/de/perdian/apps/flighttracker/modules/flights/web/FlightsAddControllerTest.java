package de.perdian.apps.flighttracker.modules.flights.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.flights.services.FlightsUpdateService;
import de.perdian.apps.flighttracker.support.web.MessageSeverity;
import de.perdian.apps.flighttracker.support.web.Messages;

public class FlightsAddControllerTest {

    @Test
    public void doAddGetWizard() {

        FlightEditor editor = new FlightEditor();
        FlightsWizardData wizardData = new FlightsWizardData();

        FlightsWizardService wizardService = Mockito.mock(FlightsWizardService.class);

        FlightsAddController controller = new FlightsAddController();
        controller.setFlightsWizardService(wizardService);
        Assertions.assertEquals("/flights/add", controller.doAddGetWizard(editor, wizardData));

        Mockito.verify(wizardService).enhanceFlightEditor(Mockito.eq(editor), Mockito.eq(wizardData));

    }

    @Test
    public void doAddPostWithBindingErrors() {

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);

        Messages messages = new Messages();

        FlightsAddController controller = new FlightsAddController();
        controller.setMessageSource(Mockito.mock(MessageSource.class));

        Assertions.assertEquals("/flights/add", controller.doAddPost(null, null, bindingResult, messages, null));
        Assertions.assertEquals(1, messages.getMessagesBySeverity().size());
        Assertions.assertEquals(1, messages.getMessagesBySeverity().get(MessageSeverity.ERROR).size());

    }

    @Test
    public void doAddPost() {

        FlightEditor flightEditor = Mockito.mock(FlightEditor.class);
        Mockito.when(flightEditor.getEntityId()).thenReturn(Long.valueOf(42));

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);

        Messages messages = new Messages();

        FlightBean insertedFlightBean = new FlightBean();
        insertedFlightBean.setEntityId(Long.valueOf(43));

        FlightsUpdateService flightsUpdateService = Mockito.mock(FlightsUpdateService.class);
        Mockito.when(flightsUpdateService.saveFlight(Mockito.any())).thenReturn(insertedFlightBean);

        FlightsAddController controller = new FlightsAddController();
        controller.setFlightsUpdateService(flightsUpdateService);

        Assertions.assertEquals("redirect:/flights/edit/43?updated=true", controller.doAddPost(null, flightEditor, bindingResult, messages, null));
        Assertions.assertEquals(0, messages.getMessagesBySeverity().size());

        Mockito.verify(flightsUpdateService).saveFlight(Mockito.any());

    }

}
