package de.perdian.apps.flighttracker.modules.overview.services.contributors;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.apps.flighttracker.FlighttrackerTestHelper;
import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewBean;

public class DifferentsContributorTest {

    @Test
    public void contributeTo() {

        OverviewBean overviewBean = new OverviewBean();
        FlightBean flight1 = new FlightBean();
        flight1.setAircraft(FlighttrackerTestHelper.createAircraftBean("A380", null, null));
        flight1.setDepartureContact(FlighttrackerTestHelper.createAirportContactBean("CGN", "DE", null, null));
        flight1.setArrivalContact(FlighttrackerTestHelper.createAirportContactBean("MCO", "US", null, null));
        flight1.setAirline(FlighttrackerTestHelper.createAirlineBean("LH", "Lufthansa"));
        FlightBean flight2 = new FlightBean();
        flight2.setAircraft(FlighttrackerTestHelper.createAircraftBean("A380", null, null));
        flight2.setDepartureContact(FlighttrackerTestHelper.createAirportContactBean("CGN", "DE", null, null));
        flight2.setArrivalContact(FlighttrackerTestHelper.createAirportContactBean("JFK", "US", null, null));
        flight2.setAirline(FlighttrackerTestHelper.createAirlineBean("LH", "Lufthansa"));
        FlightBean flight3 = new FlightBean();
        flight3.setAircraft(FlighttrackerTestHelper.createAircraftBean("B747", null, null));
        flight3.setDepartureContact(FlighttrackerTestHelper.createAirportContactBean("CGN", "DE", null, null));
        flight3.setArrivalContact(FlighttrackerTestHelper.createAirportContactBean("MCO", "US", null, null));
        flight3.setAirline(FlighttrackerTestHelper.createAirlineBean("UA", "United"));

        DifferentsContributor contributor = new DifferentsContributor();
        contributor.contributeTo(overviewBean, Arrays.asList(flight1, flight2, flight3));

        Assertions.assertEquals(Integer.valueOf(2), overviewBean.getDifferents().get(0).getValue()); // numberOfDifferentAircraftTypes
        Assertions.assertEquals(Integer.valueOf(3), overviewBean.getDifferents().get(1).getValue()); // numberOfDifferentAirports
        Assertions.assertEquals(Integer.valueOf(2), overviewBean.getDifferents().get(2).getValue()); // numberOfDifferentCountries
        Assertions.assertEquals(Integer.valueOf(2), overviewBean.getDifferents().get(3).getValue()); // numberOfDifferentRoutes
        Assertions.assertEquals(Integer.valueOf(2), overviewBean.getDifferents().get(4).getValue()); // numberOfDifferentAirlines

    }

}
