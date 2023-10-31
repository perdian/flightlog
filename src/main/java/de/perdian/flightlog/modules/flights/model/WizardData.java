package de.perdian.flightlog.modules.flights.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

public class WizardData implements Serializable {

    static final long serialVersionUID = 1L;

    private String airlineCode = null;
    private String flightNumber = null;
    private LocalDate departureDate = LocalDate.now();
    private String departureAirportCode = null;

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public String getAirlineCode() {
        return this.airlineCode == null ? null : this.airlineCode.toUpperCase();
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

    @DateTimeFormat(pattern="yyyy-MM-dd")
    public LocalDate getDepartureDate() {
        return this.departureDate;
    }
    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureAirportCode() {
        return this.departureAirportCode == null ? null : this.departureAirportCode.toUpperCase();
    }
    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

}
