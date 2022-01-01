package de.perdian.flightlog.modules.overview.services.contributors;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.flightlog.FlightlogTestHelper;
import de.perdian.flightlog.modules.flights.model.FlightBean;
import de.perdian.flightlog.modules.overview.model.OverviewBean;

public class TopFlightsContributorTest {

    @Test
    public void contributeTo() {

        FlightBean flight1 = new FlightBean();
        flight1.setAirline(FlightlogTestHelper.createAirlineBean("LH", "DE", "Lufthansa"));
        flight1.setDepartureContact(FlightlogTestHelper.createAirportContactBean("DUS", "DE", null, null));
        flight1.setArrivalContact(FlightlogTestHelper.createAirportContactBean("MCO", "US", null, null));
        flight1.setAircraft(FlightlogTestHelper.createAircraftBean("A380", null, null));
        FlightBean flight2 = new FlightBean();
        flight2.setAirline(FlightlogTestHelper.createAirlineBean("LH", "DE", "Lufthansa"));
        flight2.setDepartureContact(FlightlogTestHelper.createAirportContactBean("CGN", "DE", null, null));
        flight2.setArrivalContact(FlightlogTestHelper.createAirportContactBean("MCO", "US", null, null));
        flight2.setAircraft(FlightlogTestHelper.createAircraftBean("A380", null, null));
        FlightBean flight3 = new FlightBean();
        flight3.setAirline(FlightlogTestHelper.createAirlineBean("UA", "US", "United"));
        flight3.setDepartureContact(FlightlogTestHelper.createAirportContactBean("CGN", "DE", null, null));
        flight3.setArrivalContact(FlightlogTestHelper.createAirportContactBean("MCO", "US", null, null));
        flight3.setAircraft(FlightlogTestHelper.createAircraftBean("A320", null, null));
        FlightBean flight4 = new FlightBean();
        flight4.setAirline(FlightlogTestHelper.createAirlineBean("DL", "US", "Delta"));
        flight4.setDepartureContact(FlightlogTestHelper.createAirportContactBean("MCO", "US", null, null));
        flight4.setArrivalContact(FlightlogTestHelper.createAirportContactBean("JFK", "US", null, null));
        flight4.setAircraft(FlightlogTestHelper.createAircraftBean("B747", null, null));

        OverviewBean overviewBean = new OverviewBean();

        TopFlightsContributor contributor = new TopFlightsContributor();
        contributor.setAirlinesRepository(FlightlogTestHelper.createDefaultAirlinesRepository());
        contributor.setAirportsRepository(FlightlogTestHelper.createDefaultAirportsRepository());
        contributor.contributeTo(overviewBean, Arrays.asList(flight1, flight2, flight3, flight4));

        Assertions.assertEquals(4, overviewBean.getTopFlights().size());
        Assertions.assertEquals(4, overviewBean.getTopFlights().get("topTenAirports").size());
        Assertions.assertEquals("MCO", overviewBean.getTopFlights().get("topTenAirports").get(0).getTitle().getText());
        Assertions.assertEquals(4, overviewBean.getTopFlights().get("topTenAirports").get(0).getValue());
        Assertions.assertEquals("CGN", overviewBean.getTopFlights().get("topTenAirports").get(1).getTitle().getText());
        Assertions.assertEquals(2, overviewBean.getTopFlights().get("topTenAirports").get(1).getValue());
        Assertions.assertEquals("DUS", overviewBean.getTopFlights().get("topTenAirports").get(2).getTitle().getText());
        Assertions.assertEquals(1, overviewBean.getTopFlights().get("topTenAirports").get(2).getValue());
        Assertions.assertEquals("JFK", overviewBean.getTopFlights().get("topTenAirports").get(3).getTitle().getText());
        Assertions.assertEquals(1, overviewBean.getTopFlights().get("topTenAirports").get(3).getValue());
        Assertions.assertEquals(3, overviewBean.getTopFlights().get("topTenAirlines").size());
        Assertions.assertEquals("LH", overviewBean.getTopFlights().get("topTenAirlines").get(0).getTitle().getText());
        Assertions.assertEquals("Lufthansa", overviewBean.getTopFlights().get("topTenAirlines").get(0).getDescription().getText());
        Assertions.assertEquals(2, overviewBean.getTopFlights().get("topTenAirlines").get(0).getValue());
        Assertions.assertEquals("DL", overviewBean.getTopFlights().get("topTenAirlines").get(1).getTitle().getText());
        Assertions.assertNull(overviewBean.getTopFlights().get("topTenAirlines").get(1).getDescription().getText());
        Assertions.assertEquals(1, overviewBean.getTopFlights().get("topTenAirlines").get(1).getValue());
        Assertions.assertEquals("UA", overviewBean.getTopFlights().get("topTenAirlines").get(2).getTitle().getText());
        Assertions.assertEquals("United", overviewBean.getTopFlights().get("topTenAirlines").get(2).getDescription().getText());
        Assertions.assertEquals(1, overviewBean.getTopFlights().get("topTenAirlines").get(2).getValue());
        Assertions.assertEquals(3, overviewBean.getTopFlights().get("topTenRoutes").size());
        Assertions.assertEquals("CGN - MCO", overviewBean.getTopFlights().get("topTenRoutes").get(0).getTitle().getText());
        Assertions.assertEquals(2, overviewBean.getTopFlights().get("topTenRoutes").get(0).getValue());
        Assertions.assertEquals("DUS - MCO", overviewBean.getTopFlights().get("topTenRoutes").get(1).getTitle().getText());
        Assertions.assertEquals(1, overviewBean.getTopFlights().get("topTenRoutes").get(1).getValue());
        Assertions.assertEquals("MCO - JFK", overviewBean.getTopFlights().get("topTenRoutes").get(2).getTitle().getText());
        Assertions.assertEquals(1, overviewBean.getTopFlights().get("topTenRoutes").get(2).getValue());
        Assertions.assertEquals(3, overviewBean.getTopFlights().get("topTenAircraftTypes").size());
        Assertions.assertEquals("A380", overviewBean.getTopFlights().get("topTenAircraftTypes").get(0).getTitle().getText());
        Assertions.assertEquals(2, overviewBean.getTopFlights().get("topTenAircraftTypes").get(0).getValue());
        Assertions.assertEquals("A320", overviewBean.getTopFlights().get("topTenAircraftTypes").get(1).getTitle().getText());
        Assertions.assertEquals(1, overviewBean.getTopFlights().get("topTenAircraftTypes").get(1).getValue());
        Assertions.assertEquals("B747", overviewBean.getTopFlights().get("topTenAircraftTypes").get(2).getTitle().getText());
        Assertions.assertEquals(1, overviewBean.getTopFlights().get("topTenAircraftTypes").get(2).getValue());

    }

}
