package de.perdian.apps.flighttracker.modules.overview.services;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.flights.services.FlightsQuery;
import de.perdian.apps.flighttracker.modules.flights.services.FlightsQueryService;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewBean;
import de.perdian.apps.flighttracker.support.persistence.PaginatedList;

public class OverviewServiceImplTest {

    @Test
    public void loadFlights() {

        FlightBean flight1 = new FlightBean();
        FlightBean flight2 = new FlightBean();
        List<FlightBean> flights = Arrays.asList(flight1, flight2);
        FlightsQueryService flightsQueryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(flightsQueryService.loadFlights(Mockito.any())).thenReturn(new PaginatedList<>(flights, null));

        OverviewContributor contributor1 = Mockito.mock(OverviewContributor.class);
        OverviewContributor contributor2 = Mockito.mock(OverviewContributor.class);

        FlightsQuery flightsQuery = new FlightsQuery();

        OverviewServiceImpl serviceImpl = new OverviewServiceImpl();
        serviceImpl.setContributors(Arrays.asList(contributor1, contributor2));
        serviceImpl.setFlightsQueryService(flightsQueryService);

        OverviewBean overviewBean = serviceImpl.loadOverview(flightsQuery);
        Assertions.assertNotNull(overviewBean);

        Mockito.verify(flightsQueryService).loadFlights(Mockito.eq(flightsQuery));
        Mockito.verify(contributor1).contributeTo(Mockito.any(), Mockito.eq(flights), Mockito.any());
        Mockito.verify(contributor2).contributeTo(Mockito.any(), Mockito.eq(flights), Mockito.any());

    }

}
