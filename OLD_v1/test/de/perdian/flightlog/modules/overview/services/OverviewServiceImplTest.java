package de.perdian.flightlog.modules.overview.services;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.perdian.flightlog.modules.flights.editor.FlightBean;
import de.perdian.flightlog.modules.flights.services.FlightsQuery;
import de.perdian.flightlog.modules.flights.services.FlightsQueryService;
import de.perdian.flightlog.modules.overview.model.OverviewBean;
import de.perdian.flightlog.support.persistence.PaginatedList;

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
        Assertions.assertEquals(2, overviewBean.getNumberOfFlights());

        Mockito.verify(flightsQueryService).loadFlights(Mockito.eq(flightsQuery));
        Mockito.verify(contributor1).contributeTo(Mockito.any(), Mockito.eq(flights));
        Mockito.verify(contributor2).contributeTo(Mockito.any(), Mockito.eq(flights));

    }

}
