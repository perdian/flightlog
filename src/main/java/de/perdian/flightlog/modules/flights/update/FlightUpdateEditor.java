package de.perdian.flightlog.modules.flights.update;

import de.perdian.flightlog.modules.aircrafts.model.Aircraft;
import de.perdian.flightlog.modules.airlines.model.Airline;
import de.perdian.flightlog.modules.airports.model.Airport;
import de.perdian.flightlog.modules.airports.model.AirportContact;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class FlightUpdateEditor {

    private UUID entityId = null;
    private String departureAirportCode = null;
    private String departureAirportCountryCode = null;
    private String departureAirportName = null;
    private boolean departureAirportKnown = false;
    private LocalDate departureDateLocal = null;
    private LocalTime departureTimeLocal = null;
    private String arrivalAirportCode = null;
    private String arrivalAirportCountryCode = null;
    private String arrivalAirportName = null;
    private boolean arrivalAirportKnown = false;
    private LocalDate arrivalDateLocal = null;
    private LocalTime arrivalTimeLocal = null;
    private String airlineCode = null;
    private String airlineName = null;
    private String flightNumber = null;
    private Duration flightDuration = null;
    private Integer flightDistance = null;
    private String aircraftType = null;
    private String aircraftRegistration = null;
    private String aircraftName = null;
    private String seatNumber = null;
    private String seatType = null;
    private String cabinClass = null;
    private String flightReason = null;
    private String comment = null;

    public FlightUpdateEditor() {
    }

    public FlightUpdateEditor(Flight flight) {
        this.applyValuesFrom(flight);
    }

    public void applyValuesFrom(Flight flight) {
        this.setEntityId(flight.getEntityId());
        this.setAircraftName(flight.getAircraft() == null ? null : flight.getAircraft().getName());
        this.setAircraftRegistration(flight.getAircraft() == null ? null : flight.getAircraft().getRegistration());
        this.setAircraftType(flight.getAircraft() == null ? null : flight.getAircraft().getType());
        this.setAirlineCode(flight.getAirline() == null ? null : flight.getAirline().getCode());
        this.setAirlineName(flight.getAirline() == null ? null : flight.getAirline().getName());
        this.setArrivalAirportCode(flight.getArrivalContact() == null || flight.getArrivalContact().getAirport() == null ? null : flight.getArrivalContact().getAirport().getCode());
        this.setArrivalAirportCountryCode(flight.getArrivalContact() == null || flight.getArrivalContact().getAirport() == null ? null : flight.getArrivalContact().getAirport().getCountryCode());
        this.setArrivalAirportName(flight.getArrivalContact() == null || flight.getArrivalContact().getAirport() == null ? null : flight.getArrivalContact().getAirport().getName());
        this.setArrivalDateLocal(flight.getArrivalContact() == null ? null : flight.getArrivalContact().getDateLocal());
        this.setArrivalTimeLocal(flight.getArrivalContact() == null ? null : flight.getArrivalContact().getTimeLocal());
        this.setCabinClass(flight.getCabinClass() == null ? null : flight.getCabinClass().name());
        this.setComment(flight.getComment());
        this.setDepartureAirportCode(flight.getDepartureContact() == null || flight.getDepartureContact().getAirport() == null ? null : flight.getDepartureContact().getAirport().getCode());
        this.setDepartureAirportCountryCode(flight.getDepartureContact() == null || flight.getDepartureContact().getAirport() == null ? null : flight.getDepartureContact().getAirport().getCountryCode());
        this.setDepartureAirportName(flight.getDepartureContact() == null || flight.getDepartureContact().getAirport() == null ? null : flight.getDepartureContact().getAirport().getName());
        this.setDepartureDateLocal(flight.getDepartureContact() == null ? null : flight.getDepartureContact().getDateLocal());
        this.setDepartureTimeLocal(flight.getDepartureContact() == null ? null : flight.getDepartureContact().getTimeLocal());
        this.setFlightDistance(flight.getFlightDistance());
        this.setFlightDuration(flight.getFlightDuration());
        this.setFlightNumber(flight.getFlightNumber());
        this.setFlightReason(flight.getFlightReason() == null ? null : flight.getFlightReason().name());
        this.setSeatNumber(flight.getSeatNumber());
        this.setSeatType(flight.getSeatType() == null ? null : flight.getSeatType().name());
    }

    public void copyValuesInto(Flight flight) {

        Aircraft aircraft = new Aircraft();
        aircraft.setName(this.getAircraftName());
        aircraft.setRegistration(this.getAircraftRegistration());
        aircraft.setType(this.getAircraftType());
        flight.setAircraft(aircraft);

        Airline airline = new Airline();
        airline.setCode(this.getAirlineCode());
        airline.setName(this.getAirlineName());
        flight.setAirline(airline);

        Airport arrivalAirport = new Airport();
        arrivalAirport.setCode(this.getArrivalAirportCode());
        AirportContact arrivalAirportContactBean = new AirportContact();
        arrivalAirportContactBean.setAirport(arrivalAirport);
        arrivalAirportContactBean.setDateLocal(this.getArrivalDateLocal());
        arrivalAirportContactBean.setTimeLocal(this.getArrivalTimeLocal());
        flight.setArrivalContact(arrivalAirportContactBean);

        flight.setCabinClass(StringUtils.isEmpty(this.getCabinClass()) ? null : CabinClass.valueOf(this.getCabinClass()));
        flight.setComment(this.getComment());

        Airport departureAirport = new Airport();
        departureAirport.setCode(this.getDepartureAirportCode());
        AirportContact departureAirportContactBean = new AirportContact();
        departureAirportContactBean.setAirport(departureAirport);
        departureAirportContactBean.setDateLocal(this.getDepartureDateLocal());
        departureAirportContactBean.setTimeLocal(this.getDepartureTimeLocal());
        flight.setDepartureContact(departureAirportContactBean);

        flight.setFlightDistance(this.getFlightDistance());
        flight.setFlightDuration(this.getFlightDuration());
        flight.setFlightNumber(this.getFlightNumber());
        flight.setFlightReason(StringUtils.isEmpty(this.getFlightReason()) ? null : FlightReason.valueOf(this.getFlightReason()));
        flight.setSeatNumber(this.getSeatNumber());
        flight.setSeatType(StringUtils.isEmpty(this.getSeatType()) ? null : SeatType.valueOf(this.getSeatType()));

    }

    public UUID getEntityId() {
        return this.entityId;
    }
    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    @NotNull
    @Length(min = 3, max = 3)
    public String getDepartureAirportCode() {
        return this.departureAirportCode;
    }
    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode == null ? null : departureAirportCode.toUpperCase();
    }

    public String getDepartureAirportCountryCode() {
        return this.departureAirportCountryCode;
    }
    public void setDepartureAirportCountryCode(String departureAirportCountryCode) {
        this.departureAirportCountryCode = departureAirportCountryCode == null ? null : departureAirportCountryCode.toUpperCase();
    }

    public String getDepartureAirportName() {
        return this.departureAirportName;
    }
    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public boolean isDepartureAirportKnown() {
        return StringUtils.isNotEmpty(this.departureAirportName);
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
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

    @NotNull
    @Length(min = 3, max = 3)
    public String getArrivalAirportCode() {
        return this.arrivalAirportCode;
    }
    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode == null ? null : arrivalAirportCode.toUpperCase();
    }

    public String getArrivalAirportCountryCode() {
        return this.arrivalAirportCountryCode;
    }
    public void setArrivalAirportCountryCode(String arrivalAirportCountryCode) {
        this.arrivalAirportCountryCode = arrivalAirportCountryCode == null ? null : arrivalAirportCountryCode.toUpperCase();
    }

    public String getArrivalAirportName() {
        return this.arrivalAirportName;
    }
    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public boolean isArrivalAirportKnown() {
        return StringUtils.isNotEmpty(this.getArrivalAirportName());
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
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

    public String getFlightNumber() {
        return this.flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber == null ? null : flightNumber.toUpperCase();
    }

    @NotNull
    @NotEmpty
    public String getAirlineCode() {
        return this.airlineCode;
    }
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode == null ? null : airlineCode.toUpperCase();
    }

    public String getAirlineName() {
        return this.airlineName;
    }
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public Duration getFlightDuration() {
        return this.flightDuration;
    }
    public void setFlightDuration(Duration flightDuration) {
        this.flightDuration = flightDuration;
    }

    @Positive
    public Integer getFlightDistance() {
        return this.flightDistance;
    }
    public void setFlightDistance(Integer flightDistance) {
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
        this.seatNumber = seatNumber == null ? null : seatNumber.toUpperCase();
    }

    public String getSeatType() {
        return this.seatType;
    }
    public void setSeatType(String seatType) {
        this.seatType = seatType == null ? null : seatType.toUpperCase();
    }

    @NotNull
    public String getCabinClass() {
        return this.cabinClass;
    }
    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass == null ? null : cabinClass.toUpperCase();
    }

    @NotNull
    public String getFlightReason() {
        return this.flightReason;
    }
    public void setFlightReason(String flightReason) {
        this.flightReason = flightReason == null ? null : flightReason.toUpperCase();
    }

    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

}
