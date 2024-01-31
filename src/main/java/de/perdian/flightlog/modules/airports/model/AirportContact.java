package de.perdian.flightlog.modules.airports.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public class AirportContact {

    private Airport airport = null;
    private LocalDate dateLocal = null;
    private String dateOffset = null;
    private LocalTime timeLocal = null;
    private Instant dateTimeUtc = null;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[").append(this.getAirport() == null ? "null" : this.getAirport().getCode());
        if (this.getDateLocal() != null) {
            result.append(" @ ").append(this.getDateLocal());
            if (this.getTimeLocal() != null) {
                result.append(" ").append(this.getTimeLocal());
            }
        }
        return result.append("]").toString();
    }

    public Airport getAirport() {
        return this.airport;
    }
    public void setAirport(Airport airport) {
        this.airport = airport;
    }

    public LocalDate getDateLocal() {
        return this.dateLocal;
    }
    public void setDateLocal(LocalDate dateLocal) {
        this.dateLocal = dateLocal;
    }

    public String getDateOffset() {
        return this.dateOffset;
    }
    public void setDateOffset(String dateOffset) {
        this.dateOffset = dateOffset;
    }

    public LocalTime getTimeLocal() {
        return this.timeLocal;
    }
    public void setTimeLocal(LocalTime timeLocal) {
        this.timeLocal = timeLocal;
    }

    public Instant getDateTimeUtc() {
        return this.dateTimeUtc;
    }
    public void setDateTimeUtc(Instant dateTimeUtc) {
        this.dateTimeUtc = dateTimeUtc;
    }

}
