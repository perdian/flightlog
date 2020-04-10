package de.perdian.flightlog.modules.flights.web;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import de.perdian.flightlog.modules.flights.model.FlightBean;
import de.perdian.flightlog.modules.flights.services.FlightsQueryService;
import de.perdian.flightlog.modules.flights.services.FlightsUpdateService;
import de.perdian.flightlog.modules.flights.web.FlightsEditController.FlightNotFoundException;
import de.perdian.flightlog.support.persistence.PaginatedList;
import de.perdian.flightlog.support.web.MessageSeverity;
import de.perdian.flightlog.support.web.Messages;

public class FlightsEditControllerTest {

    @Test
    public void doDeleteGetNotFound() {

        FlightsQueryService queryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(queryService.loadFlights(Mockito.any())).thenReturn(new PaginatedList<>(null, null));

        FlightsEditController controller = new FlightsEditController();
        controller.setFlightsQueryService(queryService);

        Assertions.assertThrows(FlightNotFoundException.class, () -> controller.doDeleteGet(null, UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), null));

    }

    @Test
    public void doDeleteGet() {

        FlightBean flightBean = new FlightBean();
        FlightsQueryService queryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(queryService.loadFlights(Mockito.any())).thenReturn(new PaginatedList<>(Arrays.asList(flightBean), null));

        Model model = Mockito.mock(Model.class);

        FlightsEditController controller = new FlightsEditController();
        controller.setFlightsQueryService(queryService);

        Assertions.assertEquals("/flights/delete", controller.doDeleteGet(null, UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), model));

        Mockito.verify(model).addAttribute(Mockito.eq("flight"), Mockito.eq(flightBean));
        Mockito.verifyNoMoreInteractions(model);

    }

    @Test
    public void doDeletePostNotFound() {

        FlightsQueryService queryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(queryService.loadFlights(Mockito.any())).thenReturn(new PaginatedList<>(null, null));

        FlightsEditController controller = new FlightsEditController();
        controller.setFlightsQueryService(queryService);

        Assertions.assertThrows(FlightNotFoundException.class, () -> controller.doDeletePost(null, UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), null, null, null));

    }

    @Test
    public void doDeletePost() {

        FlightBean flightBean = new FlightBean();
        FlightsQueryService queryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(queryService.loadFlights(Mockito.any())).thenReturn(new PaginatedList<>(Arrays.asList(flightBean), null));

        FlightsUpdateService updateService = Mockito.mock(FlightsUpdateService.class);

        FlightsEditController controller = new FlightsEditController();
        controller.setFlightsQueryService(queryService);
        controller.setFlightsUpdateService(updateService);

        Assertions.assertEquals("redirect:/flights/list?updated=true", controller.doDeletePost(null, UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), null, null, null));

        Mockito.verify(updateService).deleteFlight(Mockito.eq(flightBean));

    }

    @Test
    public void doEditGetNotFound() {

        FlightsQueryService queryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(queryService.loadFlights(Mockito.any())).thenReturn(new PaginatedList<>(null, null));

        FlightsEditController controller = new FlightsEditController();
        controller.setFlightsQueryService(queryService);

        Assertions.assertThrows(FlightNotFoundException.class, () -> controller.doEditGet(null, UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), true, null, null, null));

    }

    @Test
    public void doEditGetUpdatedTrue() {

        FlightBean flightBean = new FlightBean();
        FlightsQueryService queryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(queryService.loadFlights(Mockito.any())).thenReturn(new PaginatedList<>(Arrays.asList(flightBean), null));

        Messages messages = new Messages();

        Model model = Mockito.mock(Model.class);

        FlightsEditController controller = new FlightsEditController();
        controller.setMessageSource(Mockito.mock(MessageSource.class));
        controller.setFlightsQueryService(queryService);

        Assertions.assertEquals("/flights/edit", controller.doEditGet(null, UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), true, messages, null, model));

        Mockito.verify(model).addAttribute(Mockito.eq("flightEditor"), Mockito.any(FlightEditor.class));
        Mockito.verifyNoMoreInteractions(model);
        Assertions.assertEquals(1, messages.getMessagesBySeverity().size());
        Assertions.assertEquals(1, messages.getMessagesBySeverity().get(MessageSeverity.INFO).size());

    }

    @Test
    public void doEditGetUpdatedFalse() {

        FlightBean flightBean = new FlightBean();
        FlightsQueryService queryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(queryService.loadFlights(Mockito.any())).thenReturn(new PaginatedList<>(Arrays.asList(flightBean), null));

        Messages messages = new Messages();

        Model model = Mockito.mock(Model.class);

        FlightsEditController controller = new FlightsEditController();
        controller.setMessageSource(Mockito.mock(MessageSource.class));
        controller.setFlightsQueryService(queryService);

        Assertions.assertEquals("/flights/edit", controller.doEditGet(null, UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), false, messages, null, model));

        Mockito.verify(model).addAttribute(Mockito.eq("flightEditor"), Mockito.any(FlightEditor.class));
        Mockito.verifyNoMoreInteractions(model);
        Assertions.assertEquals(0, messages.getMessagesBySeverity().size());

    }

    @Test
    public void doEditPostBindingResultHasErrors() {

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);

        FlightBean flightBean = new FlightBean();
        FlightsQueryService queryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(queryService.loadFlights(Mockito.any())).thenReturn(new PaginatedList<>(Arrays.asList(flightBean), null));

        Messages messages = new Messages();

        Model model = Mockito.mock(Model.class);

        FlightsEditController controller = new FlightsEditController();
        controller.setFlightsQueryService(queryService);
        controller.setMessageSource(Mockito.mock(MessageSource.class));

        Assertions.assertEquals("/flights/edit", controller.doEditPost(null, UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), null, bindingResult, messages, null, model));
        Mockito.verify(model).addAttribute(Mockito.eq("flightEditor"), Mockito.any(FlightEditor.class));
        Mockito.verifyNoMoreInteractions(model);

    }

    @Test
    public void doEditPostBindingResultHasNoErrorsFound() {

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);

        FlightBean flightBean = new FlightBean();
        flightBean.setEntityId(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cff"));
        FlightsQueryService queryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(queryService.loadFlights(Mockito.any())).thenReturn(new PaginatedList<>(Arrays.asList(flightBean), null));

        FlightsUpdateService updateService = Mockito.mock(FlightsUpdateService.class);

        FlightEditor flightEditor = Mockito.mock(FlightEditor.class);
        Mockito.when(flightEditor.getEntityId()).thenReturn(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"));

        FlightsEditController controller = new FlightsEditController();
        controller.setFlightsQueryService(queryService);
        controller.setFlightsUpdateService(updateService);

        Assertions.assertEquals("redirect:/flights/edit/c2bb2c43-e029-4cc2-a80c-7445cdea0cff?updated=true", controller.doEditPost(null, UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), flightEditor, bindingResult, null, null, null));

        Mockito.verify(updateService).saveFlight(Mockito.eq(flightBean), Mockito.any());

    }

    @Test
    public void doEditPostBindingResultHasNoErrorsNotFound() {

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);

        FlightsQueryService queryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(queryService.loadFlights(Mockito.any())).thenReturn(new PaginatedList<>(null, null));

        FlightsUpdateService updateService = Mockito.mock(FlightsUpdateService.class);

        FlightEditor flightEditor = Mockito.mock(FlightEditor.class);
        Mockito.when(flightEditor.getEntityId()).thenReturn(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"));

        FlightsEditController controller = new FlightsEditController();
        controller.setFlightsQueryService(queryService);
        controller.setFlightsUpdateService(updateService);

        Assertions.assertThrows(FlightNotFoundException.class, () -> controller.doEditPost(null, UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), flightEditor, bindingResult, null, null, null));

    }

}
