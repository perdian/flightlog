package de.perdian.flightlog.modules.flights.lookup.impl;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;

class Flightradar24DataFlight {

    private Instant departureTime = null;
    private String departureAirportCode = null;
    private Instant arrivalTime = null;
    private String arrivalAirportCode = null;
    private String aircraftTypeCode = null;
    private String aircraftRegistration = null;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    Instant getDepartureTime() {
        return this.departureTime;
    }
    void setDepartureTime(Instant departureTime) {
        this.departureTime = departureTime;
    }

    String getDepartureAirportCode() {
        return this.departureAirportCode;
    }
    void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    Instant getArrivalTime() {
        return this.arrivalTime;
    }
    void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    String getArrivalAirportCode() {
        return this.arrivalAirportCode;
    }
    void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    String getAircraftTypeCode() {
        return this.aircraftTypeCode;
    }
    void setAircraftTypeCode(String aircraftTypeCode) {
        this.aircraftTypeCode = aircraftTypeCode;
    }

    String getAircraftRegistration() {
        return this.aircraftRegistration;
    }
    void setAircraftRegistration(String aircraftRegistration) {
        this.aircraftRegistration = aircraftRegistration;
    }

}
