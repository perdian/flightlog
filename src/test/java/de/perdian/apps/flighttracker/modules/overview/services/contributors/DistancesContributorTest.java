package de.perdian.apps.flighttracker.modules.overview.services.contributors;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewBean;
import de.perdian.apps.flighttracker.support.types.FlightDistance;

public class DistancesContributorTest {

    @Test
    public void contributeTo() {

        FlightBean flight1a = new FlightBean();
        flight1a.setFlightDistance(200);
        FlightBean flight1b = new FlightBean();
        flight1b.setFlightDistance(200);
        FlightBean flight2 = new FlightBean();
        flight2.setFlightDistance(3000);
        FlightBean flight3 = new FlightBean();
        flight3.setFlightDistance(4000);
        FlightBean flight4 = new FlightBean();
        flight4.setFlightDistance(12000);

        OverviewBean overviewBean = new OverviewBean();

        DistancesContributor contributor = new DistancesContributor();
        contributor.contributeTo(overviewBean, Arrays.asList(flight1a, flight1b, flight2, flight3, flight4), null);

        Assertions.assertEquals(2, overviewBean.getDistances().get(0).getValue()); // SHORT
        Assertions.assertEquals(40d, overviewBean.getDistances().get(0).getPercentage());
        Assertions.assertEquals(FlightDistance.SHORT.getMinValue(), overviewBean.getDistances().get(0).getContext().get("minValue"));
        Assertions.assertEquals(FlightDistance.SHORT.getMaxValue(), overviewBean.getDistances().get(0).getContext().get("maxValue"));
        Assertions.assertEquals(1, overviewBean.getDistances().get(1).getValue()); // MEDIUM
        Assertions.assertEquals(20d, overviewBean.getDistances().get(1).getPercentage());
        Assertions.assertEquals(FlightDistance.MEDIUM.getMinValue(), overviewBean.getDistances().get(1).getContext().get("minValue"));
        Assertions.assertEquals(FlightDistance.MEDIUM.getMaxValue(), overviewBean.getDistances().get(1).getContext().get("maxValue"));
        Assertions.assertEquals(1, overviewBean.getDistances().get(2).getValue()); // LONG
        Assertions.assertEquals(20d, overviewBean.getDistances().get(2).getPercentage());
        Assertions.assertEquals(FlightDistance.LONG.getMinValue(), overviewBean.getDistances().get(2).getContext().get("minValue"));
        Assertions.assertEquals(FlightDistance.LONG.getMaxValue(), overviewBean.getDistances().get(2).getContext().get("maxValue"));
        Assertions.assertEquals(1, overviewBean.getDistances().get(3).getValue()); // ULTRALONG
        Assertions.assertEquals(20d, overviewBean.getDistances().get(3).getPercentage());
        Assertions.assertEquals(FlightDistance.ULTRALONG.getMinValue(), overviewBean.getDistances().get(3).getContext().get("minValue"));
        Assertions.assertEquals(FlightDistance.ULTRALONG.getMaxValue(), overviewBean.getDistances().get(3).getContext().get("maxValue"));

    }

}
