package de.perdian.flightlog.modules.flights.service.model;

import de.perdian.flightlog.modules.flights.web.editor.FlightEditor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FlightLookup implements Serializable {

    static final long serialVersionUID = 1L;

    private String airlineCode = null;
    private String flightNumber = null;
    private String departureAirportCode = null;
    private LocalDate departureDateLocal = null;
    private LocalTime departureTimeLocal = null;
    private String arrivalAirportCode = null;
    private LocalDate arrivalDateLocal = null;
    private LocalTime arrivalTimeLocal = null;
    private String aircraftType = null;
    private String aircraftName = null;
    private String aircraftRegistration = null;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public static int sortByDepartureDateAndTime(FlightLookup flightLookup1, FlightLookup flightLookup2) {
        if (flightLookup1.getDepartureDateLocal() == null) {
            return flightLookup2.getDepartureDateLocal() == null ? 0 : 1;
        } else if (flightLookup2.getDepartureDateLocal() == null) {
            return -1;
        } else {
            LocalDateTime ldt1 = flightLookup1.getDepartureDateLocal().atTime(flightLookup1.getDepartureTimeLocal() == null ? LocalTime.of(0, 0) : flightLookup1.getDepartureTimeLocal());
            LocalDateTime ldt2 = flightLookup2.getDepartureDateLocal().atTime(flightLookup2.getDepartureTimeLocal() == null ? LocalTime.of(0, 0) : flightLookup2.getDepartureTimeLocal());
            return ldt1.compareTo(ldt2);
        }
    }

    public void writeInto(FlightEditor flightEditor) {
    }

    public String getAirlineCode() {
        return this.airlineCode;
    }
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getFlightNumber() {
        return this.flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureAirportCode() {
        return this.departureAirportCode;
    }
    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }
    public LocalDate getDepartureDateLocal() {
        return this.departureDateLocal;
    }

    public void setDepartureDateLocal(LocalDate departureDateLocal) {
        this.departureDateLocal = departureDateLocal;
    }

    public LocalTime getDepartureTimeLocal() {
        return this.departureTimeLocal;
    }
    public void setDepartureTimeLocal(LocalTime departureTimeLocal) {
        this.departureTimeLocal = departureTimeLocal;
    }

    public String getArrivalAirportCode() {
        return this.arrivalAirportCode;
    }
    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public LocalDate getArrivalDateLocal() {
        return this.arrivalDateLocal;
    }
    public void setArrivalDateLocal(LocalDate arrivalDateLocal) {
        this.arrivalDateLocal = arrivalDateLocal;
    }

    public LocalTime getArrivalTimeLocal() {
        return this.arrivalTimeLocal;
    }
    public void setArrivalTimeLocal(LocalTime arrivalTimeLocal) {
        this.arrivalTimeLocal = arrivalTimeLocal;
    }

    public String getAircraftType() {
        return this.aircraftType;
    }
    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getAircraftName() {
        return this.aircraftName;
    }
    public void setAircraftName(String aircraftName) {
        this.aircraftName = aircraftName;
    }

    public String getAircraftRegistration() {
        return this.aircraftRegistration;
    }
    public void setAircraftRegistration(String aircraftRegistration) {
        this.aircraftRegistration = aircraftRegistration;
    }

}
