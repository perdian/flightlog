package de.perdian.flightlog.modules.flights.service.impl;

import de.perdian.flightlog.modules.airports.persistence.AirportEntity;

import java.time.*;
import java.util.Objects;

class Flightradar24DataRow {

    private LocalDate departureDateUtc = null;
    private LocalTime departureTimeUtc = null;
    private AirportEntity departureAirportEntity = null;
    private String departureAirportCode = null;
    private AirportEntity arrivalAirportEntity = null;
    private String arrivalAirportCode = null;
    private String aircraftType = null;
    private String aircraftRegistration = null;
    private Duration duration = null;
    private String status = null;

    static int compareByDepartureDateAndTime(Flightradar24DataRow r1, Flightradar24DataRow r2) {
        LocalDate d1 = r1 == null ? null : r1.getDepartureDateUtc();
        LocalDate d2 = r2 == null ? null : r2.getDepartureDateUtc();
        if (d1 == null && d2 == null) {
            return 0;
        } else if (d1 == null && d2 != null) {
            return -1;
        } else if (d1 != null && d2 == null) {
            return 1;
        } else {
            int dateResult = d1.compareTo(d2);
            if (dateResult != 0) {
                return dateResult;
            } else {
                LocalTime t1 = r1 == null ? null : r1.getDepartureTimeUtc();
                LocalTime t2 = r2 == null ? null : r2.getDepartureTimeUtc();
                if (t1 == null && t2 == null) {
                    return 0;
                } else if (t1 == null && t2 != null) {
                    return -1;
                } else if (t1 != null && t2 == null) {
                    return 1;
                } else {
                    return t1.compareTo(t2);
                }
            }
        }
    }


    boolean matchesDepartureLocalDate(LocalDate requestedDepartureDateLocal) {
        ZoneId departureZoneId = this.getDepartureAirportEntity() == null ? null : this.getDepartureAirportEntity().getTimezoneId();
        ZonedDateTime actualDepartureDateTimeUtc = this.getDepartureDateUtc() == null  || this.getDepartureTimeUtc() == null ? null : this.getDepartureTimeUtc().atDate(this.getDepartureDateUtc()).atZone(ZoneId.of("UTC"));
        ZonedDateTime actualDepartureDateTimeLocal = actualDepartureDateTimeUtc == null || departureZoneId == null ? null : actualDepartureDateTimeUtc.withZoneSameInstant(departureZoneId);
        LocalDate actualDepartureDate = actualDepartureDateTimeLocal == null ? null : actualDepartureDateTimeLocal.toLocalDate();
        return actualDepartureDate != null && actualDepartureDate.equals(requestedDepartureDateLocal);
    }

    boolean matchesDepartureAirportCode(String requestedDepartureAirportCode) {
        return Objects.equals(this.getDepartureAirportCode(), requestedDepartureAirportCode);
    }

    LocalDate getDepartureDateUtc() {
        return this.departureDateUtc;
    }
    void setDepartureDateUtc(LocalDate departureDateUtc) {
        this.departureDateUtc = departureDateUtc;
    }

    LocalTime getDepartureTimeUtc() {
        return this.departureTimeUtc;
    }
    void setDepartureTimeUtc(LocalTime departureTimeUtc) {
        this.departureTimeUtc = departureTimeUtc;
    }

    AirportEntity getDepartureAirportEntity() {
        return this.departureAirportEntity;
    }
    void setDepartureAirportEntity(AirportEntity departureAirportEntity) {
        this.departureAirportEntity = departureAirportEntity;
    }

    String getDepartureAirportCode() {
        return this.departureAirportCode;
    }
    void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    AirportEntity getArrivalAirportEntity() {
        return this.arrivalAirportEntity;
    }
    void setArrivalAirportEntity(AirportEntity arrivalAirportEntity) {
        this.arrivalAirportEntity = arrivalAirportEntity;
    }

    String getArrivalAirportCode() {
        return this.arrivalAirportCode;
    }
    void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    String getAircraftType() {
        return this.aircraftType;
    }
    void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    String getAircraftRegistration() {
        return this.aircraftRegistration;
    }
    void setAircraftRegistration(String aircraftRegistration) {
        this.aircraftRegistration = aircraftRegistration;
    }

    Duration getDuration() {
        return this.duration;
    }
    void setDuration(Duration duration) {
        this.duration = duration;
    }

    String getStatus() {
        return this.status;
    }
    void setStatus(String status) {
        this.status = status;
    }

}
