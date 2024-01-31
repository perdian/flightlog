package de.perdian.flightlog.modules.flights.shared.persistence;

import de.perdian.flightlog.modules.authentication.persistence.UserEntity;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "flightlog_flight")
public class FlightEntity implements Serializable {

    static final long serialVersionUID = 1L;

    private UUID id = null;
    private UserEntity user = null;
    private String departureAirportCode = null;
    private LocalDate departureDateLocal = null;
    private LocalTime departureTimeLocal = null;
    private String arrivalAirportCode = null;
    private LocalDate arrivalDateLocal = null;
    private LocalTime arrivalTimeLocal = null;
    private String aircraftType = null;
    private String aircraftRegistration = null;
    private String aircraftName = null;
    private String airlineCode = null;
    private String airlineName = null;
    private String flightNumber = null;
    private FlightReason flightReason = null;
    private Integer flightDuration = null; // Minutes
    private Integer flightDistance = null; // Kilometers
    private String seatNumber = null;
    private SeatType seatType = null;
    private CabinClass cabinClass = null;
    private String comment = null;
    private Instant createdAt = null;
    private Instant lastUpdatedAt = null;

    @Override
    public int hashCode() {
        return this.getId() == null ? 0 : this.getId().hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else {
            return (that instanceof FlightEntity) && this.getId() != null && this.getId().equals(((FlightEntity)that).getId());
        }
    }

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

    @PrePersist
    @PreUpdate
    void prePersistOrUpdate() {
        Instant now = Instant.now();
        if (this.getCreatedAt() == null) {
            this.setCreatedAt(now);
        }
        this.setLastUpdatedAt(now);
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    public UUID getId() {
        return this.id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    @ManyToOne(optional = true)
    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Column(length = 3)
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

    @Column(length = 3)
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

    @Column(length = 50)
    public String getAircraftType() {
        return this.aircraftType;
    }
    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    @Column(length = 10)
    public String getAircraftRegistration() {
        return this.aircraftRegistration;
    }
    public void setAircraftRegistration(String aircraftRegistration) {
        this.aircraftRegistration = aircraftRegistration;
    }

    @Column(length = 100)
    public String getAircraftName() {
        return this.aircraftName;
    }
    public void setAircraftName(String aircraftName) {
        this.aircraftName = aircraftName;
    }

    @Column(length = 2)
    public String getAirlineCode() {
        return this.airlineCode;
    }
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    @Column(length = 100)
    public String getAirlineName() {
        return this.airlineName;
    }
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    @Column(length = 10)
    public String getFlightNumber() {
        return this.flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Enumerated(EnumType.STRING)
    public FlightReason getFlightReason() {
        return this.flightReason;
    }
    public void setFlightReason(FlightReason flightReason) {
        this.flightReason = flightReason;
    }

    @Column(length = 10)
    public String getSeatNumber() {
        return this.seatNumber;
    }
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Enumerated(EnumType.STRING)
    public SeatType getSeatType() {
        return this.seatType;
    }
    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    @Enumerated(EnumType.STRING)
    public CabinClass getCabinClass() {
        return this.cabinClass;
    }
    public void setCabinClass(CabinClass cabinClass) {
        this.cabinClass = cabinClass;
    }

    public Integer getFlightDuration() {
        return this.flightDuration;
    }
    public void setFlightDuration(Integer flightDuration) {
        this.flightDuration = flightDuration;
    }

    public Integer getFlightDistance() {
        return this.flightDistance;
    }
    public void setFlightDistance(Integer flightDistance) {
        this.flightDistance = flightDistance;
    }

    @Column(length = 1000)
    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastUpdatedAt() {
        return this.lastUpdatedAt;
    }
    public void setLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

}
