package de.perdian.flightlog.support.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.flightlog.support.types.FlightDistance;

public class FlightDistanceTest {

    @Test
    public void matchesOnlyUpperBoundary() {
        Assertions.assertTrue(FlightDistance.SHORT.matches(Integer.MIN_VALUE));
        Assertions.assertTrue(FlightDistance.SHORT.matches(0));
        Assertions.assertTrue(FlightDistance.SHORT.matches(1500));
        Assertions.assertFalse(FlightDistance.SHORT.matches(1501));
    }

    @Test
    public void matchesTwoBoundaries() {
        Assertions.assertFalse(FlightDistance.MEDIUM.matches(1500));
        Assertions.assertTrue(FlightDistance.MEDIUM.matches(1501));
        Assertions.assertTrue(FlightDistance.MEDIUM.matches(2000));
        Assertions.assertTrue(FlightDistance.MEDIUM.matches(3500));
        Assertions.assertFalse(FlightDistance.MEDIUM.matches(3501));
    }

    @Test
    public void matchesOnlyLowerBoundary() {
        Assertions.assertFalse(FlightDistance.ULTRALONG.matches(10000));
        Assertions.assertTrue(FlightDistance.ULTRALONG.matches(10001));
        Assertions.assertTrue(FlightDistance.ULTRALONG.matches(Integer.MAX_VALUE));
    }

}
