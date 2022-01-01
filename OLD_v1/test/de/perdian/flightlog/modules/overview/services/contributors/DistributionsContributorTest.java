package de.perdian.flightlog.modules.overview.services.contributors;

import java.util.Arrays;

import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.flightlog.modules.flights.model.FlightBean;
import de.perdian.flightlog.modules.overview.model.OverviewBean;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;

public class DistributionsContributorTest {

    @Test
    public void contributeTo() {

        FlightBean flight1 = new FlightBean();
        flight1.setCabinClass(CabinClass.ECONOMY);
        flight1.setFlightReason(FlightReason.BUSINESS);
        flight1.setSeatType(SeatType.WINDOW);
        FlightBean flight2 = new FlightBean();
        flight2.setCabinClass(CabinClass.ECONOMY);
        flight2.setFlightReason(FlightReason.PRIVATE);
        flight2.setSeatType(SeatType.WINDOW);
        FlightBean flight3 = new FlightBean();
        flight3.setCabinClass(CabinClass.PREMIUM_ECONOMY);
        flight3.setFlightReason(FlightReason.PRIVATE);
        flight3.setSeatType(SeatType.MIDDLE);
        FlightBean flight4 = new FlightBean();
        flight4.setCabinClass(CabinClass.BUSINESS);
        flight4.setFlightReason(FlightReason.CREW);

        OverviewBean overviewBean = new OverviewBean();

        DistributionsContributor contributor = new DistributionsContributor();
        contributor.contributeTo(overviewBean, Arrays.asList(flight1, flight2, flight3, flight4));

        MatcherAssert.assertThat(overviewBean.getDistributions().get("cabinClasses"), IsCollectionWithSize.hasSize(CabinClass.values().length));
        Assertions.assertEquals(2, overviewBean.getDistributions().get("cabinClasses").get(0).getValue().intValue()); // ECONOMY
        Assertions.assertEquals(50d, overviewBean.getDistributions().get("cabinClasses").get(0).getPercentage());
        Assertions.assertEquals(1, overviewBean.getDistributions().get("cabinClasses").get(1).getValue().intValue()); // PREMIUM_ECONOMY
        Assertions.assertEquals(25d, overviewBean.getDistributions().get("cabinClasses").get(1).getPercentage());
        Assertions.assertEquals(1, overviewBean.getDistributions().get("cabinClasses").get(2).getValue().intValue()); // BUSINESS
        Assertions.assertEquals(25d, overviewBean.getDistributions().get("cabinClasses").get(2).getPercentage());
        Assertions.assertEquals(0, overviewBean.getDistributions().get("cabinClasses").get(3).getValue().intValue()); // FIRST
        Assertions.assertNull(overviewBean.getDistributions().get("cabinClasses").get(3).getPercentage());

        MatcherAssert.assertThat(overviewBean.getDistributions().get("flightReasons"), IsCollectionWithSize.hasSize(FlightReason.values().length));
        Assertions.assertEquals(2, overviewBean.getDistributions().get("flightReasons").get(0).getValue().intValue()); // PRIVATE
        Assertions.assertEquals(50d, overviewBean.getDistributions().get("flightReasons").get(0).getPercentage());
        Assertions.assertEquals(1, overviewBean.getDistributions().get("flightReasons").get(1).getValue().intValue()); // BUSINESS
        Assertions.assertEquals(25d, overviewBean.getDistributions().get("flightReasons").get(1).getPercentage());
        Assertions.assertEquals(1, overviewBean.getDistributions().get("flightReasons").get(2).getValue().intValue()); // CREW
        Assertions.assertEquals(25d, overviewBean.getDistributions().get("flightReasons").get(2).getPercentage());
        Assertions.assertEquals(0, overviewBean.getDistributions().get("flightReasons").get(3).getValue().intValue()); // VIRTUAL
        Assertions.assertNull(overviewBean.getDistributions().get("flightReasons").get(3).getPercentage());

        MatcherAssert.assertThat(overviewBean.getDistributions().get("seatTypes"), IsCollectionWithSize.hasSize(SeatType.values().length));
        Assertions.assertEquals(2, overviewBean.getDistributions().get("seatTypes").get(0).getValue().intValue()); // WINDOW
        Assertions.assertEquals(50d, overviewBean.getDistributions().get("seatTypes").get(0).getPercentage());
        Assertions.assertEquals(1, overviewBean.getDistributions().get("seatTypes").get(1).getValue().intValue()); // MIDDLE
        Assertions.assertEquals(25d, overviewBean.getDistributions().get("seatTypes").get(1).getPercentage());
        Assertions.assertEquals(0, overviewBean.getDistributions().get("seatTypes").get(2).getValue().intValue()); // AISLE
        Assertions.assertNull(overviewBean.getDistributions().get("seatTypes").get(2).getPercentage());

    }

}
