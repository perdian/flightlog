package de.perdian.apps.flighttracker.modules.flights.web;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.flights.model.AircraftBean;
import de.perdian.apps.flighttracker.modules.flights.model.AirportBean;
import de.perdian.apps.flighttracker.modules.flights.model.AirportContactBean;
import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

public class FlightEditor implements Serializable {

    static final long serialVersionUID = 1L;

    private Long entityId = null;
    private String departureAirportCode = null;
    private String departureAirportCountryCode = null;
    private String departureAirportName = null;
    private String departureDateLocal = null;
    private String departureTimeLocal = null;
    private String arrivalAirportCode = null;
    private String arrivalAirportCountryCode = null;
    private String arrivalAirportName = null;
    private String arrivalDateLocal = null;
    private String arrivalTimeLocal = null;
    private String airlineCode = null;
    private String airlineName = null;
    private String flightNumber = null;
    private String flightDuration = null;
    private String flightDistance = null;
    private String aircraftType = null;
    private String aircraftRegistration = null;
    private String aircraftName = null;
    private String seatNumber = null;
    private String seatType = null;
    private String cabinClass = null;
    private String flightReason = null;
    private String comment = null;

    public FlightEditor() {
    }

    public FlightEditor(FlightBean flight) {
        this.setEntityId(flight.getEntityId());
        this.setAircraftName(flight.getAircraft() == null ? null : flight.getAircraft().getName());
        this.setAircraftRegistration(flight.getAircraft() == null ? null : flight.getAircraft().getRegistration());
        this.setAircraftType(flight.getAircraft() == null ? null : flight.getAircraft().getType());
        this.setAirlineCode(flight.getAirline() == null ? null : flight.getAirline().getIataCode());
        this.setAirlineName(flight.getAirline() == null ? null : flight.getAirline().getName());
        this.setArrivalAirportCode(flight.getArrivalContact() == null || flight.getArrivalContact().getAirport() == null ? null : flight.getArrivalContact().getAirport().getCode());
        this.setArrivalAirportCountryCode(flight.getArrivalContact() == null || flight.getArrivalContact().getAirport() == null ? null : flight.getArrivalContact().getAirport().getCountryCode());
        this.setArrivalAirportName(flight.getArrivalContact() == null || flight.getArrivalContact().getAirport() == null ? null : flight.getArrivalContact().getAirport().getName());
        this.setArrivalDateLocal(flight.getArrivalContact() == null || flight.getArrivalContact().getDateLocal() == null ? null : FlighttrackerHelper.formatDate(flight.getArrivalContact().getDateLocal()));
        this.setArrivalTimeLocal(flight.getArrivalContact() == null || flight.getArrivalContact().getTimeLocal() == null ? null : FlighttrackerHelper.formatTime(flight.getArrivalContact().getTimeLocal()));
        this.setCabinClass(flight.getCabinClass() == null ? null : flight.getCabinClass().name());
        this.setComment(flight.getComment());
        this.setDepartureAirportCode(flight.getDepartureContact() == null || flight.getDepartureContact().getAirport() == null ? null : flight.getDepartureContact().getAirport().getCode());
        this.setDepartureAirportCountryCode(flight.getDepartureContact() == null || flight.getDepartureContact().getAirport() == null ? null : flight.getDepartureContact().getAirport().getCountryCode());
        this.setDepartureAirportName(flight.getDepartureContact() == null || flight.getDepartureContact().getAirport() == null ? null : flight.getDepartureContact().getAirport().getName());
        this.setDepartureDateLocal(flight.getDepartureContact() == null || flight.getDepartureContact().getDateLocal() == null ? null : FlighttrackerHelper.formatDate(flight.getDepartureContact().getDateLocal()));
        this.setDepartureTimeLocal(flight.getDepartureContact() == null || flight.getDepartureContact().getTimeLocal() == null ? null : FlighttrackerHelper.formatTime(flight.getDepartureContact().getTimeLocal()));
        this.setFlightDistance(flight.getFlightDistance() == null ? null : flight.getFlightDistance().toString());
        this.setFlightDuration(FlighttrackerHelper.formatDuration(flight.getFlightDuration()));
        this.setFlightNumber(flight.getFlightNumber());
        this.setFlightReason(flight.getFlightReason() == null ? null : flight.getFlightReason().name());
        this.setSeatNumber(flight.getSeatNumber());
        this.setSeatType(flight.getSeatType() == null ? null : flight.getSeatType().name());
    }

    public void copyValuesInto(FlightBean flightBean) {

        AircraftBean aircraftBean = new AircraftBean();
        aircraftBean.setName(this.getAircraftName());
        aircraftBean.setRegistration(this.getAircraftRegistration());
        aircraftBean.setType(this.getAircraftType());
        flightBean.setAircraft(aircraftBean);

        AirlineBean airlineBean = new AirlineBean();
        airlineBean.setIataCode(this.getAirlineCode());
        airlineBean.setName(this.getAirlineName());
        flightBean.setAirline(airlineBean);

        AirportBean arrivalAirportBean = new AirportBean();
        arrivalAirportBean.setCode(this.getArrivalAirportCode());
        AirportContactBean arrivalAirportContactBean = new AirportContactBean();
        arrivalAirportContactBean.setAirport(arrivalAirportBean);
        arrivalAirportContactBean.setDateLocal(FlighttrackerHelper.parseLocalDate(this.getArrivalDateLocal()));
        arrivalAirportContactBean.setTimeLocal(FlighttrackerHelper.parseLocalTime(this.getArrivalTimeLocal()));
        flightBean.setArrivalContact(arrivalAirportContactBean);

        flightBean.setCabinClass(StringUtils.isEmpty(this.getCabinClass()) ? null : CabinClass.valueOf(this.getCabinClass()));
        flightBean.setComment(this.getComment());

        AirportBean departureAirportBean = new AirportBean();
        departureAirportBean.setCode(this.getDepartureAirportCode());
        AirportContactBean departureAirportContactBean = new AirportContactBean();
        departureAirportContactBean.setAirport(departureAirportBean);
        departureAirportContactBean.setDateLocal(FlighttrackerHelper.parseLocalDate(this.getDepartureDateLocal()));
        departureAirportContactBean.setTimeLocal(FlighttrackerHelper.parseLocalTime(this.getDepartureTimeLocal()));
        flightBean.setDepartureContact(departureAirportContactBean);

        flightBean.setFlightDistance(StringUtils.isEmpty(this.getFlightDistance()) ? null : Integer.parseInt(this.getFlightDistance()));
        flightBean.setFlightDuration(FlighttrackerHelper.parseDuration(this.getFlightDuration()));
        flightBean.setFlightNumber(this.getFlightNumber());
        flightBean.setFlightReason(StringUtils.isEmpty(this.getFlightReason()) ? null : FlightReason.valueOf(this.getFlightReason()));
        flightBean.setSeatNumber(this.getSeatNumber());
        flightBean.setSeatType(StringUtils.isEmpty(this.getSeatType()) ? null : SeatType.valueOf(this.getSeatType()));

    }

    public Long getEntityId() {
        return this.entityId;
    }
    private void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @NotNull
    @NotEmpty
    public String getDepartureAirportCode() {
        return this.departureAirportCode;
    }
    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getDepartureAirportCountryCode() {
        return this.departureAirportCountryCode;
    }
    public void setDepartureAirportCountryCode(String departureAirportCountryCode) {
        this.departureAirportCountryCode = departureAirportCountryCode;
    }

    public String getDepartureAirportName() {
        return this.departureAirportName;
    }
    void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    @NotNull
    @NotEmpty
    public String getDepartureDateLocal() {
        return this.departureDateLocal;
    }
    public void setDepartureDateLocal(String departureDateLocal) {
        this.departureDateLocal = departureDateLocal;
    }

    public String getDepartureTimeLocal() {
        return this.departureTimeLocal;
    }
    public void setDepartureTimeLocal(String departureTimeLocal) {
        this.departureTimeLocal = departureTimeLocal;
    }

    @NotNull
    @NotEmpty
    public String getArrivalAirportCode() {
        return this.arrivalAirportCode;
    }
    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public String getArrivalAirportCountryCode() {
        return this.arrivalAirportCountryCode;
    }
    public void setArrivalAirportCountryCode(String arrivalAirportCountryCode) {
        this.arrivalAirportCountryCode = arrivalAirportCountryCode;
    }

    public String getArrivalAirportName() {
        return this.arrivalAirportName;
    }
    void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    @NotNull
    @NotEmpty
    public String getArrivalDateLocal() {
        return this.arrivalDateLocal;
    }
    public void setArrivalDateLocal(String arrivalDateLocal) {
        this.arrivalDateLocal = arrivalDateLocal;
    }

    public String getArrivalTimeLocal() {
        return this.arrivalTimeLocal;
    }
    public void setArrivalTimeLocal(String arrivalTimeLocal) {
        this.arrivalTimeLocal = arrivalTimeLocal;
    }

    public String getFlightNumber() {
        return this.flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
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

    public String getFlightDuration() {
        return this.flightDuration;
    }
    public void setFlightDuration(String flightDuration) {
        this.flightDuration = flightDuration;
    }

    public String getFlightDistance() {
        return this.flightDistance;
    }
    public void setFlightDistance(String flightDistance) {
        this.flightDistance = flightDistance;
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

    public String getSeatType() {
        return this.seatType;
    }
    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getCabinClass() {
        return this.cabinClass;
    }
    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    public String getFlightReason() {
        return this.flightReason;
    }
    public void setFlightReason(String flightReason) {
        this.flightReason = flightReason;
    }

    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

}
