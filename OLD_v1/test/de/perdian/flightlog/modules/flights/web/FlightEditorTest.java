package de.perdian.flightlog.modules.flights.web;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.flightlog.FlightlogTestHelper;
import de.perdian.flightlog.modules.flights.editor.FlightBean;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;

public class FlightEditorTest {

    @Test
    public void constructor() {
        FlightEditor flightEditor = new FlightEditor(FlightlogTestHelper.createDefaultFlightBean());
        Assertions.assertEquals(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), flightEditor.getEntityId());
        Assertions.assertEquals("Frankfurt am Main", flightEditor.getAircraftName());
        Assertions.assertEquals("D-AIMA", flightEditor.getAircraftRegistration());
        Assertions.assertEquals("Airbus A380-800", flightEditor.getAircraftType());
        Assertions.assertEquals("MCO", flightEditor.getArrivalAirportCode());
        Assertions.assertEquals("2017-12-08", flightEditor.getArrivalDateLocal());
        Assertions.assertEquals("13:12", flightEditor.getArrivalTimeLocal());
        Assertions.assertEquals("ECONOMY", flightEditor.getCabinClass());
        Assertions.assertEquals("this is a comment", flightEditor.getComment());
        Assertions.assertEquals("CGN", flightEditor.getDepartureAirportCode());
        Assertions.assertEquals("2017-12-08", flightEditor.getDepartureDateLocal());
        Assertions.assertEquals("08:10", flightEditor.getDepartureTimeLocal());
        Assertions.assertEquals("1234", flightEditor.getFlightDistance());
        Assertions.assertEquals("", flightEditor.getFlightDuration());
        Assertions.assertEquals("1234", flightEditor.getFlightNumber());
        Assertions.assertEquals("PRIVATE", flightEditor.getFlightReason());
        Assertions.assertEquals("42F", flightEditor.getSeatNumber());
        Assertions.assertEquals("WINDOW", flightEditor.getSeatType());
    }

    @Test
    public void copyValuesInto() {

        FlightEditor flightEditor = new FlightEditor(FlightlogTestHelper.createDefaultFlightBean());

        FlightBean newFlightBean = new FlightBean();
        flightEditor.copyValuesInto(newFlightBean);

        Assertions.assertEquals("Frankfurt am Main", newFlightBean.getAircraft().getName());
        Assertions.assertEquals("D-AIMA", newFlightBean.getAircraft().getRegistration());
        Assertions.assertEquals("Airbus A380-800", newFlightBean.getAircraft().getType());
        Assertions.assertEquals("MCO", newFlightBean.getArrivalContact().getAirport().getCode());
        Assertions.assertEquals(LocalDate.of(2017, 12, 8), newFlightBean.getArrivalContact().getDateLocal());
        Assertions.assertNull(newFlightBean.getArrivalContact().getDateOffset());
        Assertions.assertEquals(LocalTime.of(13, 12), newFlightBean.getArrivalContact().getTimeLocal());
        Assertions.assertNull(newFlightBean.getAverageSpeed());
        Assertions.assertEquals(CabinClass.ECONOMY, newFlightBean.getCabinClass());
        Assertions.assertEquals("this is a comment", newFlightBean.getComment());
        Assertions.assertEquals("CGN", newFlightBean.getDepartureContact().getAirport().getCode());
        Assertions.assertEquals(LocalDate.of(2017, 12, 8), newFlightBean.getDepartureContact().getDateLocal());
        Assertions.assertNull(newFlightBean.getDepartureContact().getDateOffset());
        Assertions.assertEquals(LocalTime.of(8, 10), newFlightBean.getDepartureContact().getTimeLocal());
        Assertions.assertEquals(Integer.valueOf(1234), newFlightBean.getFlightDistance());
        Assertions.assertEquals("1234", newFlightBean.getFlightNumber());
        Assertions.assertEquals(FlightReason.PRIVATE, newFlightBean.getFlightReason());
        Assertions.assertEquals("42F", newFlightBean.getSeatNumber());
        Assertions.assertEquals(SeatType.WINDOW, newFlightBean.getSeatType());

    }

}
