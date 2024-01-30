package de.perdian.flightlog.modules.flights.exchange;

import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class FlightsExchangePackageFlight implements Serializable {

    static final long serialVersionUID = 1L;

    private String aircraftName = null;
    private String aircraftRegistration = null;
    private String aircraftType = null;
    private String airlineCode = null;
    private String airlineName = null;
    private String arrivalAirportCode = null;
    private LocalDate arrivalDateLocal = null;
    private LocalTime arrivalTimeLocal = null;
    private CabinClass cabinClass = null;
    private String comment = null;
    private String departureAirportCode = null;
    private LocalDate departureDateLocal = null;
    private LocalTime departureTimeLocal = null;
    private Integer flightDistance = null;
    private String flightDuration = null;
    private String flightNumber = null;
    private FlightReason flightReason = null;
    private String seatNumber = null;
    private SeatType seatType = null;
    private Boolean include = Boolean.TRUE;

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[");
        result.append(this.getDepartureAirportCode()).append("@").append(this.getDepartureDateLocal());
        if (this.getDepartureTimeLocal() != null) {
            result.append("_").append(this.getDepartureTimeLocal());
        }
        result.append(" » ");
        result.append(this.getArrivalAirportCode()).append("@").append(this.getArrivalDateLocal());
        if (this.getArrivalTimeLocal() != null) {
            result.append("_").append(this.getArrivalTimeLocal());
        }
        if (StringUtils.isNotEmpty(this.getAirlineCode())) {
            result.append(" (").append(this.getAirlineCode());
            if (StringUtils.isNotEmpty(this.getFlightNumber())) {
                result.append(" ").append(this.getFlightNumber());
            }
            result.append(")");
        }
        result.append("]");
        return result.toString();
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

    public String getAircraftType() {
        return this.aircraftType;
    }
    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getAirlineCode() {
        return this.airlineCode;
    }
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getAirlineName() {
        return this.airlineName;
    }
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getArrivalAirportCode() {
        return this.arrivalAirportCode;
    }
    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate getArrivalDateLocal() {
        return this.arrivalDateLocal;
    }
    public void setArrivalDateLocal(LocalDate arrivalDateLocal) {
        this.arrivalDateLocal = arrivalDateLocal;
    }

    @DateTimeFormat(pattern = "HH:mm")
    public LocalTime getArrivalTimeLocal() {
        return this.arrivalTimeLocal;
    }
    public void setArrivalTimeLocal(LocalTime arrivalTimeLocal) {
        this.arrivalTimeLocal = arrivalTimeLocal;
    }

    public CabinClass getCabinClass() {
        return this.cabinClass;
    }
    public void setCabinClass(CabinClass cabinClass) {
        this.cabinClass = cabinClass;
    }

    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDepartureAirportCode() {
        return this.departureAirportCode;
    }
    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate getDepartureDateLocal() {
        return this.departureDateLocal;
    }
    public void setDepartureDateLocal(LocalDate departureDateLocal) {
        this.departureDateLocal = departureDateLocal;
    }

    @DateTimeFormat(pattern = "HH:mm")
    public LocalTime getDepartureTimeLocal() {
        return this.departureTimeLocal;
    }
    public void setDepartureTimeLocal(LocalTime departureTimeLocal) {
        this.departureTimeLocal = departureTimeLocal;
    }

    public Integer getFlightDistance() {
        return this.flightDistance;
    }
    public void setFlightDistance(Integer flightDistance) {
        this.flightDistance = flightDistance;
    }

    public String getFlightDuration() {
        return this.flightDuration;
    }
    public void setFlightDuration(String flightDuration) {
        this.flightDuration = flightDuration;
    }

    public String getFlightNumber() {
        return this.flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public FlightReason getFlightReason() {
        return this.flightReason;
    }
    public void setFlightReason(FlightReason flightReason) {
        this.flightReason = flightReason;
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

    public Boolean getInclude() {
        return this.include;
    }
    public void setInclude(Boolean include) {
        this.include = include;
    }

}
