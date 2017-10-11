package de.perdian.apps.flighttracker.business.modules.wizard;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class WizardData implements Serializable {

    static final long serialVersionUID = 1L;

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
        StringBuilder result = new StringBuilder();
        result.append("[departureAirportCode=").append(this.getDepartureAirportCode());
        result.append(",departureDateLocal=").append(this.getDepartureDateLocal());
        result.append(",departureTimeLocal=").append(this.getDepartureTimeLocal());
        result.append(",arrivalAirportCode=").append(this.getArrivalAirportCode());
        result.append(",arrivalDateLocal=").append(this.getArrivalDateLocal());
        result.append(",arrivalTimeLocal=").append(this.getArrivalTimeLocal());
        result.append(",aircraftType=").append(this.getAircraftType());
        result.append(",aircraftRegistration=").append(this.getAircraftRegistration());
        result.append(",aircraftName=").append(this.getAircraftName());
        return result.append("]").toString();
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
