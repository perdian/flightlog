package de.perdian.flightlog.modules.flights.web.editor;

import de.perdian.flightlog.modules.flights.service.model.Airport;
import de.perdian.flightlog.support.FlightlogHelper;

import java.time.Duration;

public class FlightRoute {

    private Airport departureAirport = null;
    private Airport arrivalAirport = null;
    private Duration duration = null;
    private Integer distance = null;

    public Airport getDepartureAirport() {
        return this.departureAirport;
    }
    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return this.arrivalAirport;
    }
    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getDurationString() {
        return this.getDuration() == null ? null : FlightlogHelper.formatDuration(this.getDuration());
    }

    public Duration getDuration() {
        return this.duration;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Integer getDistance() {
        return this.distance;
    }
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

}
