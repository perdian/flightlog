package de.perdian.flightlog.modules.flights.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.flightlog.FlightlogTestHelper;
import de.perdian.flightlog.modules.flights.web.FlightsSupportController;

public class FlightsSupportControllerTest {

    @Test
    public void computeDistance() {

        FlightsSupportController controller = new FlightsSupportController();
        controller.setAirportsRepository(FlightlogTestHelper.createDefaultAirportsRepository());

        Assertions.assertEquals(Integer.valueOf(7494), controller.computeDistance("CGN", "MCO"));

    }

    @Test
    public void computeDistanceDepartureNotFound() {

        FlightsSupportController controller = new FlightsSupportController();
        controller.setAirportsRepository(FlightlogTestHelper.createDefaultAirportsRepository());

        Assertions.assertNull(controller.computeDistance("INVALID", "MCO"));

    }

    @Test
    public void computeDistanceArrivalNotFound() {

        FlightsSupportController controller = new FlightsSupportController();
        controller.setAirportsRepository(FlightlogTestHelper.createDefaultAirportsRepository());

        Assertions.assertNull(controller.computeDistance("CGN", "INVALID"));

    }

    @Test
    public void computeDuration() {

        FlightsSupportController controller = new FlightsSupportController();
        controller.setAirportsRepository(FlightlogTestHelper.createDefaultAirportsRepository());

        Assertions.assertEquals("09:00", controller.computeDuration("CGN", "2017-12-11", "12:00", "MCO", "2017-12-11", "15:00"));
        Assertions.assertEquals("08:00", controller.computeDuration("MCO", "2017-12-11", "08:00", "CGN", "2017-12-11", "22:00"));

    }

    @Test
    public void computeDurationDepartureNotFound() {

        FlightsSupportController controller = new FlightsSupportController();
        controller.setAirportsRepository(FlightlogTestHelper.createDefaultAirportsRepository());

        Assertions.assertNull(controller.computeDuration("INVALID", "2017-12-11", "12:00", "MCO", "2017-12-11", "15:00"));

    }

    @Test
    public void computeDurationArrivalNotFound() {

        FlightsSupportController controller = new FlightsSupportController();
        controller.setAirportsRepository(FlightlogTestHelper.createDefaultAirportsRepository());

        Assertions.assertNull(controller.computeDuration("CGN", "2017-12-11", "12:00", "INVALID", "2017-12-11", "15:00"));

    }

}
