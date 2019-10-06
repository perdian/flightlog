package de.perdian.flightlog.modules.flights.model;

import java.io.Serializable;
import java.time.Duration;
import java.util.UUID;

import de.perdian.flightlog.modules.airlines.persistence.AirlineEntity;
import de.perdian.flightlog.modules.users.persistence.UserEntity;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.FlightType;
import de.perdian.flightlog.support.types.SeatType;

public class FlightBean implements Serializable {

    static final long serialVersionUID = 1L;

    private UUID entityId = null;
    private UserEntity user = null;
    private AirportContactBean departureContact = null;
    private AirportContactBean arrivalContact = null;
    private AircraftBean aircraft = null;
    private AirlineEntity airline = null;
    private String flightNumber = null;
    private FlightReason flightReason = null;
    private FlightType flightType = null;
    private Duration flightDuration = null;
    private String flightDurationString = null;
    private Integer flightDistance = null; // Kilometers
    private String seatNumber = null;
    private SeatType seatType = null;
    private CabinClass cabinClass = null;
    private String comment = null;
    private Double averageSpeed = null;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[entityId=").append(this.getEntityId());
        result.append(",user=").append(this.getUser());
        result.append(",flightNumber=").append(this.getFlightNumber());
        result.append(",departureContact=").append(this.getDepartureContact());
        result.append(",arrivalContact=").append(this.getArrivalContact());
        return result.append("]").toString();
    }

    public UUID getEntityId() {
        return this.entityId;
    }
    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public UserEntity getUser() {
        return this.user;
    }
    public void setUser(UserEntity user) {
        this.user = user;
    }

    public AirportContactBean getDepartureContact() {
        return this.departureContact;
    }
    public void setDepartureContact(AirportContactBean departureContact) {
        this.departureContact = departureContact;
    }

    public AirportContactBean getArrivalContact() {
        return this.arrivalContact;
    }
    public void setArrivalContact(AirportContactBean arrivalContact) {
        this.arrivalContact = arrivalContact;
    }

    public AircraftBean getAircraft() {
        return this.aircraft;
    }
    public void setAircraft(AircraftBean aircraft) {
        this.aircraft = aircraft;
    }

    public AirlineEntity getAirline() {
        return this.airline;
    }
    public void setAirline(AirlineEntity airline) {
        this.airline = airline;
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

    public FlightType getFlightType() {
        return this.flightType;
    }
    public void setFlightType(FlightType flightType) {
        this.flightType = flightType;
    }

    public Duration getFlightDuration() {
        return this.flightDuration;
    }
    public void setFlightDuration(Duration flightDuration) {
        this.flightDuration = flightDuration;
    }

    public String getFlightDurationString() {
        return this.flightDurationString;
    }
    public void setFlightDurationString(String flightDurationString) {
        this.flightDurationString = flightDurationString;
    }

    public Integer getFlightDistance() {
        return this.flightDistance;
    }
    public void setFlightDistance(Integer flightDistance) {
        this.flightDistance = flightDistance;
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

    public Double getAverageSpeed() {
        return this.averageSpeed;
    }
    public void setAverageSpeed(Double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

}
