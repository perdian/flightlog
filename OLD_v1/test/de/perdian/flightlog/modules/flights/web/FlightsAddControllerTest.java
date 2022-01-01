package de.perdian.flightlog.modules.flights.web;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import de.perdian.flightlog.modules.flights.model.FlightBean;
import de.perdian.flightlog.modules.flights.services.FlightsUpdateService;
import de.perdian.flightlog.modules.wizard.services.WizardData;
import de.perdian.flightlog.modules.wizard.services.WizardDataService;
import de.perdian.flightlog.support.web.MessageSeverity;
import de.perdian.flightlog.support.web.Messages;

public class FlightsAddControllerTest {

    @Test
    public void doAddWizardPostWithOneResult() {

        FlightEditor editor = new FlightEditor();
        FlightWizardEditor flightWizardEditor = new FlightWizardEditor();
        flightWizardEditor.setWizAirlineCode("LH");
        flightWizardEditor.setWizDepartureAirportCode("CGN");
        flightWizardEditor.setWizDepartureDateLocal("1969-07-20");
        flightWizardEditor.setWizFlightNumber("123");

        WizardData wizardData = new WizardData();
        WizardDataService wizardDataService = Mockito.mock(WizardDataService.class);
        Mockito.when(wizardDataService.createData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(1969, 7, 20)), Mockito.eq("CGN"))).thenReturn(Collections.singletonList(wizardData));

        FlightsWizardService wizardService = Mockito.mock(FlightsWizardService.class);

        Model model = new ExtendedModelMap();

        FlightsAddController controller = new FlightsAddController();
        controller.setWizardDataService(wizardDataService);
        controller.setFlightsWizardService(wizardService);

        Assertions.assertEquals("/flights/add", controller.doAddWizardPost(editor, flightWizardEditor, model));
        Assertions.assertEquals(0, model.asMap().size());

        Mockito.verify(wizardService).enhanceFlightEditor(Mockito.eq(editor), Mockito.eq(flightWizardEditor), Mockito.eq(wizardData));

    }

    @Test
    public void doAddWizardPostWithMultipleResult() {

        FlightEditor editor = new FlightEditor();
        FlightWizardEditor flightWizardEditor = new FlightWizardEditor();
        flightWizardEditor.setWizAirlineCode("LH");
        flightWizardEditor.setWizDepartureAirportCode("CGN");
        flightWizardEditor.setWizDepartureDateLocal("1969-07-20");
        flightWizardEditor.setWizFlightNumber("123");

        WizardData wizardData1 = new WizardData();
        WizardData wizardData2 = new WizardData();
        WizardDataService wizardDataService = Mockito.mock(WizardDataService.class);
        Mockito.when(wizardDataService.createData(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(wizardData1, wizardData2));

        FlightsWizardService wizardService = Mockito.mock(FlightsWizardService.class);

        Model model = new ExtendedModelMap();

        FlightsAddController controller = new FlightsAddController();
        controller.setWizardDataService(wizardDataService);
        controller.setFlightsWizardService(wizardService);

        Assertions.assertEquals("/flights/add", controller.doAddWizardPost(editor, flightWizardEditor, model));
        Assertions.assertEquals(1, model.asMap().size());
        Assertions.assertNotNull(model.asMap().get("wizardFlightEditors"));
        Assertions.assertEquals(2, ((List<?>)model.asMap().get("wizardFlightEditors")).size());

        Mockito.verify(wizardService).enhanceFlightEditor(Mockito.any(), Mockito.eq(flightWizardEditor), Mockito.eq(wizardData1));
        Mockito.verify(wizardService).enhanceFlightEditor(Mockito.any(), Mockito.eq(flightWizardEditor), Mockito.eq(wizardData1));

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
        Mockito.when(flightEditor.getEntityId()).thenReturn(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"));

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);

        Messages messages = new Messages();

        FlightBean insertedFlightBean = new FlightBean();
        insertedFlightBean.setEntityId(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cff"));

        FlightsUpdateService flightsUpdateService = Mockito.mock(FlightsUpdateService.class);
        Mockito.when(flightsUpdateService.saveFlight(Mockito.any(), Mockito.any())).thenReturn(insertedFlightBean);

        FlightsAddController controller = new FlightsAddController();
        controller.setFlightsUpdateService(flightsUpdateService);

        Assertions.assertEquals("redirect:/flights/edit/c2bb2c43-e029-4cc2-a80c-7445cdea0cff?updated=true", controller.doAddPost(null, flightEditor, bindingResult, messages, null));
        Assertions.assertEquals(0, messages.getMessagesBySeverity().size());

        Mockito.verify(flightsUpdateService).saveFlight(Mockito.any(), Mockito.any());

    }

}
