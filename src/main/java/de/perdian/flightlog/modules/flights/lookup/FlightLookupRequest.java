package de.perdian.flightlog.modules.flights.lookup;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class FlightLookupRequest {

    private String airlineCode = null;
    private String flightNumber = null;
    private LocalDate departureDate = LocalDate.now();
    private String departureAirportCode = null;

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public boolean isPopulated() {
        return StringUtils.isNotEmpty(this.getAirlineCode()) && StringUtils.isNotEmpty(this.getFlightNumber());
    }

    public String getAirlineCode() {
        return this.airlineCode;
    }
    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode == null ? null : airlineCode.toUpperCase();
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
        return this.departureAirportCode;
    }
    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode == null ? null : departureAirportCode.toUpperCase();
    }

}
