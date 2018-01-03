package de.perdian.apps.flighttracker.modules.overview.services.contributors;

import java.time.Duration;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewBean;

public class MaxFlightsContributorTest {

    @Test
    public void contributeTo() {

        FlightBean flight1 = new FlightBean();
        flight1.setFlightDistance(100);
        flight1.setFlightDuration(Duration.ofHours(12).plusMinutes(30));
        FlightBean flight2 = new FlightBean();
        flight2.setFlightDistance(null);
        flight2.setFlightDuration(Duration.ofMinutes(15));
        FlightBean flight3 = new FlightBean();
        flight3.setFlightDistance(2000);
        flight3.setFlightDuration(null);
        flight3.setAverageSpeed(50d);
        FlightBean flight4 = new FlightBean();
        flight4.setFlightDistance(50);
        flight4.setFlightDuration(Duration.ofHours(10));
        flight4.setAverageSpeed(100d);

        OverviewBean overviewBean = new OverviewBean();

        MaxFlightsContributor contributor = new MaxFlightsContributor();
        contributor.contributeTo(overviewBean, Arrays.asList(flight1, flight2, flight3, flight4), null);

        Assertions.assertEquals(6, overviewBean.getMaxFlights().size());
        Assertions.assertEquals(flight1, overviewBean.getMaxFlights().get("longestFlightByDuration"));
        Assertions.assertEquals(flight3, overviewBean.getMaxFlights().get("longestFlightByDistance"));
        Assertions.assertEquals(flight2, overviewBean.getMaxFlights().get("shortestFlightByDuration"));
        Assertions.assertEquals(flight4, overviewBean.getMaxFlights().get("shortestFlightByDistance"));
        Assertions.assertEquals(flight4, overviewBean.getMaxFlights().get("fastestFlight"));
        Assertions.assertEquals(flight3, overviewBean.getMaxFlights().get("slowestFlight"));

    }

}
