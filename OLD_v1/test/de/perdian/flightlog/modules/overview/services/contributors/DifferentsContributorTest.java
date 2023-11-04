package de.perdian.flightlog.modules.overview.services.contributors;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.flightlog.FlightlogTestHelper;
import de.perdian.flightlog.modules.flights.editor.FlightBean;
import de.perdian.flightlog.modules.overview.model.OverviewBean;

public class DifferentsContributorTest {

    @Test
    public void contributeTo() {

        OverviewBean overviewBean = new OverviewBean();
        FlightBean flight1 = new FlightBean();
        flight1.setAircraft(FlightlogTestHelper.createAircraftBean("A380", null, null));
        flight1.setDepartureContact(FlightlogTestHelper.createAirportContactBean("CGN", "DE", null, null));
        flight1.setArrivalContact(FlightlogTestHelper.createAirportContactBean("MCO", "US", null, null));
        flight1.setAirline(FlightlogTestHelper.createAirlineBean("LH", "DE", "Lufthansa"));
        FlightBean flight2 = new FlightBean();
        flight2.setAircraft(FlightlogTestHelper.createAircraftBean("A380", null, null));
        flight2.setDepartureContact(FlightlogTestHelper.createAirportContactBean("CGN", "DE", null, null));
        flight2.setArrivalContact(FlightlogTestHelper.createAirportContactBean("JFK", "US", null, null));
        flight2.setAirline(FlightlogTestHelper.createAirlineBean("LH", "DE", "Lufthansa"));
        FlightBean flight3 = new FlightBean();
        flight3.setAircraft(FlightlogTestHelper.createAircraftBean("B747", null, null));
        flight3.setDepartureContact(FlightlogTestHelper.createAirportContactBean("CGN", "DE", null, null));
        flight3.setArrivalContact(FlightlogTestHelper.createAirportContactBean("MCO", "US", null, null));
        flight3.setAirline(FlightlogTestHelper.createAirlineBean("UA", "US", "United"));

        DifferentsContributor contributor = new DifferentsContributor();
        contributor.contributeTo(overviewBean, Arrays.asList(flight1, flight2, flight3));

        Assertions.assertEquals(Integer.valueOf(2), overviewBean.getDifferents().get(0).getValue()); // numberOfDifferentAircraftTypes
        Assertions.assertEquals(Integer.valueOf(3), overviewBean.getDifferents().get(1).getValue()); // numberOfDifferentAirports
        Assertions.assertEquals(Integer.valueOf(2), overviewBean.getDifferents().get(2).getValue()); // numberOfDifferentCountries
        Assertions.assertEquals(Integer.valueOf(2), overviewBean.getDifferents().get(3).getValue()); // numberOfDifferentRoutes
        Assertions.assertEquals(Integer.valueOf(2), overviewBean.getDifferents().get(4).getValue()); // numberOfDifferentAirlines

    }

}
