package de.perdian.flightlog.modules.flights.web;

import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;

import de.perdian.flightlog.modules.flights.web.FlightEditorHelper;
import de.perdian.flightlog.modules.flights.web.FlightEditorHelperAdvice;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;

public class FlightEditorHelperAdviceTest {

    @Test
    public void flightEditorHelper() {

        FlightEditorHelperAdvice advice = new FlightEditorHelperAdvice();
        FlightEditorHelper helper = advice.flightEditorHelper();
        MatcherAssert.assertThat(helper.getCabinClassValues(), IsCollectionWithSize.hasSize(CabinClass.values().length));
        MatcherAssert.assertThat(helper.getFlightReasonValues(), IsCollectionWithSize.hasSize(FlightReason.values().length));
        MatcherAssert.assertThat(helper.getSeatTypeValues(), IsCollectionWithSize.hasSize(SeatType.values().length));

    }

}
