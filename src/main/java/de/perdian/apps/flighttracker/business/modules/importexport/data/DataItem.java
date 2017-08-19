package de.perdian.apps.flighttracker.business.modules.importexport.data;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

/**
 * Intermediate item that contains all information related to a flights
 * that is used during imoprt or export process.
 *
 * @author Christian Robert
 */

public class DataItem implements Serializable {

    static final long serialVersionUID = 1L;

    private String departureAirportCode = null;
    private LocalDate departureDateLocal = null;
    private LocalTime departureTimeLocal = null;
    private String arrivalAirportCode = null;
    private LocalDate arrivalDateLocal = null;
    private LocalTime arrivalTimeLocal = null;
    private String airlineName = null;
    private String airlineCode = null;
    private String aircraftType = null;
    private String aircraftRegistration = null;
    private String aircraftName = null;
    private String seatNumber = null;
    private SeatType seatType = null;
    private Duration flightDuration = null;
    private Integer flightDistance = null;
    private String flightNumber = null;
    private CabinClass cabinClass = null;
    private FlightReason flightReason = null;
    private String comment = null;

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        toStringBuilder.append("departureAirport", this.getDepartureAirportCode());
        toStringBuilder.append("departureDateLocal", this.getDepartureDateLocal());
        toStringBuilder.append("departureTimeLocal", this.getDepartureTimeLocal());
        toStringBuilder.append("arrivalAirport", this.getArrivalAirportCode());
        toStringBuilder.append("arrivalDateLocal", this.getArrivalDateLocal());
        toStringBuilder.append("arrivalTimeLocal", this.getArrivalTimeLocal());
        toStringBuilder.append("airlineCode", this.getAirlineCode());
        toStringBuilder.append("flightNumber", this.getFlightNumber());
        return toStringBuilder.toString();
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

    public String getAirlineName() {
        return this.airlineName;
    }
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getAirlineCode() {
        return this.airlineCode;
    }
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getAircraftType() {
        return this.aircraftType;
    }
    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getAircraftRegistration() {
        return this.aircraftRegistration;
    }
    public void setAircraftRegistration(String aircraftRegistration) {
        this.aircraftRegistration = aircraftRegistration;
    }

    public String getAircraftName() {
        return this.aircraftName;
    }
    public void setAircraftName(String aircraftName) {
        this.aircraftName = aircraftName;
    }

    public String getSeatNumber() {
        return this.seatNumber;
    }
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatType getSeatType() {
        return this.seatType;
    }
    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public Duration getFlightDuration() {
        return this.flightDuration;
    }
    public void setFlightDuration(Duration flightDuration) {
        this.flightDuration = flightDuration;
    }

    public Integer getFlightDistance() {
        return this.flightDistance;
    }
    public void setFlightDistance(Integer flightDistance) {
        this.flightDistance = flightDistance;
    }

    public String getFlightNumber() {
        return this.flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public CabinClass getCabinClass() {
        return this.cabinClass;
    }
    public void setCabinClass(CabinClass cabinClass) {
        this.cabinClass = cabinClass;
    }

    public FlightReason getFlightReason() {
        return this.flightReason;
    }
    public void setFlightReason(FlightReason flightReason) {
        this.flightReason = flightReason;
    }

    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

}
