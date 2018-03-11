package de.perdian.apps.flighttracker.modules.flights.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.flights.services.FlightsQuery;
import de.perdian.apps.flighttracker.modules.flights.services.FlightsQueryService;
import de.perdian.apps.flighttracker.support.persistence.PaginatedList;

public class FlightsListControllerTest {

    @Test
    public void doList() {

        Model model = Mockito.mock(Model.class);

        PaginatedList<FlightBean> resultList = new PaginatedList<>(null, null);
        FlightsQueryService queryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(queryService.loadFlights(Mockito.any())).thenReturn(resultList);

        FlightsListController controller = new FlightsListController();
        controller.setFlightsQueryService(queryService);

        Assertions.assertEquals("/flights/list", controller.doList(null, model));

        Mockito.verify(model).addAttribute(Mockito.eq("flightsQuery"), Mockito.any(FlightsQuery.class));
        Mockito.verify(model).addAttribute(Mockito.eq("flights"), Mockito.eq(resultList));
        Mockito.verifyNoMoreInteractions(model);
        Mockito.verify(queryService).loadFlights(Mockito.any(FlightsQuery.class));

    }

}
