package de.perdian.apps.flighttracker.modules.flights.web;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Test;

import de.perdian.apps.flighttracker.modules.flights.FlightsTestHelper;
import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

public class FlightEditorTest {

    @Test
    public void constructor() {
        FlightEditor flightEditor = new FlightEditor(FlightsTestHelper.createDefaultFlightBean());
        Assert.assertEquals(Long.valueOf(42), flightEditor.getEntityId());
        Assert.assertEquals("Frankfurt am Main", flightEditor.getAircraftName());
        Assert.assertEquals("D-AIMA", flightEditor.getAircraftRegistration());
        Assert.assertEquals("Airbus A380-800", flightEditor.getAircraftType());
        Assert.assertEquals("MCO", flightEditor.getArrivalAirportCode());
        Assert.assertEquals("2017-12-08", flightEditor.getArrivalDateLocal());
        Assert.assertEquals("13:12", flightEditor.getArrivalTimeLocal());
        Assert.assertEquals("ECONOMY", flightEditor.getCabinClass());
        Assert.assertEquals("this is a comment", flightEditor.getComment());
        Assert.assertEquals("CGN", flightEditor.getDepartureAirportCode());
        Assert.assertEquals("2017-12-08", flightEditor.getDepartureDateLocal());
        Assert.assertEquals("08:10", flightEditor.getDepartureTimeLocal());
        Assert.assertEquals("1234", flightEditor.getFlightDistance());
        Assert.assertEquals("", flightEditor.getFlightDuration());
        Assert.assertEquals("1234", flightEditor.getFlightNumber());
        Assert.assertEquals("PRIVATE", flightEditor.getFlightReason());
        Assert.assertEquals("42F", flightEditor.getSeatNumber());
        Assert.assertEquals("WINDOW", flightEditor.getSeatType());
    }

    @Test
    public void copyValuesInto() {

        FlightEditor flightEditor = new FlightEditor(FlightsTestHelper.createDefaultFlightBean());

        FlightBean newFlightBean = new FlightBean();
        flightEditor.copyValuesInto(newFlightBean);

        Assert.assertEquals("Frankfurt am Main", newFlightBean.getAircraft().getName());
        Assert.assertEquals("D-AIMA", newFlightBean.getAircraft().getRegistration());
        Assert.assertEquals("Airbus A380-800", newFlightBean.getAircraft().getType());
        Assert.assertEquals("MCO", newFlightBean.getArrivalContact().getAirport().getCode());
        Assert.assertEquals(LocalDate.of(2017, 12, 8), newFlightBean.getArrivalContact().getDateLocal());
        Assert.assertNull(newFlightBean.getArrivalContact().getDateOffset());
        Assert.assertEquals(LocalTime.of(13, 12), newFlightBean.getArrivalContact().getTimeLocal());
        Assert.assertNull(newFlightBean.getAverageSpeed());
        Assert.assertEquals(CabinClass.ECONOMY, newFlightBean.getCabinClass());
        Assert.assertEquals("this is a comment", newFlightBean.getComment());
        Assert.assertEquals("CGN", newFlightBean.getDepartureContact().getAirport().getCode());
        Assert.assertEquals(LocalDate.of(2017, 12, 8), newFlightBean.getDepartureContact().getDateLocal());
        Assert.assertNull(newFlightBean.getDepartureContact().getDateOffset());
        Assert.assertEquals(LocalTime.of(8, 10), newFlightBean.getDepartureContact().getTimeLocal());
        Assert.assertEquals(Integer.valueOf(1234), newFlightBean.getFlightDistance());
        Assert.assertEquals("1234", newFlightBean.getFlightNumber());
        Assert.assertEquals(FlightReason.PRIVATE, newFlightBean.getFlightReason());
        Assert.assertEquals("42F", newFlightBean.getSeatNumber());
        Assert.assertEquals(SeatType.WINDOW, newFlightBean.getSeatType());

    }

}
