package de.perdian.flightlog.modules.flights.shared.model;

import de.perdian.flightlog.modules.airports.model.Airport;
import de.perdian.flightlog.support.FlightlogHelper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class Route {

    private Airport departureAirport = null;
    private Airport arrivalAirport = null;

    public static List<Route> computeDistinctRoutes(Collection<Flight> flights) {
        Map<String, Route> routesByKey = new LinkedHashMap<>();
        for (Flight flight : flights) {
            Airport departureAirport = flight.getDepartureContact().getAirport();
            Airport arrivalAirport = flight.getArrivalContact().getAirport();
            String routeKey = departureAirport.getCode() + "-" + arrivalAirport.getCode();
            routesByKey.computeIfAbsent(routeKey, _ -> new Route(departureAirport, arrivalAirport));
        }
        return routesByKey.entrySet().stream().map(Map.Entry::getValue).toList();
    }

    public Route(Airport departureAirport, Airport arrivalAirport) {
        this.setDepartureAirport(departureAirport);
        this.setArrivalAirport(arrivalAirport);
    }

    public Integer computeDistance() {
        if (this.getDepartureAirport().getLongitude() != null && this.getDepartureAirport().getLatitude() != null && this.getArrivalAirport().getLongitude() != null && this.getArrivalAirport().getLatitude() != null) {
            return FlightlogHelper.computeDistanceInKilometers(this.getDepartureAirport().getLongitude(), this.getDepartureAirport().getLatitude(), this.getArrivalAirport().getLongitude(), this.getArrivalAirport().getLatitude());
        } else {
            return null;
        }
    }

    public String computeDistanceString() {
        Integer distance = this.computeDistance();
        if (distance == null) {
            return null;
        } else {
            return new DecimalFormat("#,##0", new DecimalFormatSymbols(Locale.GERMANY)).format(distance) + " km";
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(this.getDepartureAirport());
        hashCodeBuilder.append(this.getArrivalAirport());
        return hashCodeBuilder.toHashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof Route thatRoute) {
            EqualsBuilder equalsBuilder = new EqualsBuilder();
            equalsBuilder.append(this.getDepartureAirport(), thatRoute.getDepartureAirport());
            equalsBuilder.append(this.getArrivalAirport(), thatRoute.getArrivalAirport());
            return equalsBuilder.isEquals();
        } else {
            return false;
        }
    }

    public Airport getDepartureAirport() {
        return this.departureAirport;
    }
    private void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return this.arrivalAirport;
    }
    private void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

}
