package de.perdian.apps.flighttracker.modules.flights.web;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Assert;
import org.junit.Test;

import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

public class FlightEditorHelperAdviceTest {

    @Test
    public void flightEditorHelper() {

        FlightEditorHelperAdvice advice = new FlightEditorHelperAdvice();
        FlightEditorHelper helper = advice.flightEditorHelper();
        Assert.assertThat(helper.getCabinClassValues(), IsCollectionWithSize.hasSize(CabinClass.values().length));
        Assert.assertThat(helper.getFlightReasonValues(), IsCollectionWithSize.hasSize(FlightReason.values().length));
        Assert.assertThat(helper.getSeatTypeValues(), IsCollectionWithSize.hasSize(SeatType.values().length));

    }

}
