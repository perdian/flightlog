package de.perdian.flightlog.modules.overview.services.contributors;

import java.time.Duration;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.flightlog.modules.flights.model.FlightBean;
import de.perdian.flightlog.modules.overview.model.OverviewBean;
import de.perdian.flightlog.modules.overview.services.contributors.GeneralContributor;

public class GeneralContributorTest {

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
        FlightBean flight4 = new FlightBean();
        flight4.setFlightDistance(50);
        flight4.setFlightDuration(Duration.ofHours(10));

        OverviewBean overviewBean = new OverviewBean();

        GeneralContributor contributor = new GeneralContributor();
        contributor.contributeTo(overviewBean, Arrays.asList(flight1, flight2, flight3, flight4), null);

        Assertions.assertEquals(5, overviewBean.getGeneral().size());
        Assertions.assertEquals(4, overviewBean.getGeneral().get(0).getValue()); // totalNumber
        Assertions.assertEquals(2150, overviewBean.getGeneral().get(1).getValue()); // distanceInKm
        Assertions.assertEquals(1335, overviewBean.getGeneral().get(2).getValue()); // distanceInMiles
        Assertions.assertEquals(22.75d, overviewBean.getGeneral().get(3).getValue().doubleValue(), 0.01d); // durationInHours
        Assertions.assertEquals(0.95d, overviewBean.getGeneral().get(4).getValue().doubleValue(), 0.1d); // durationInDays

    }

}
