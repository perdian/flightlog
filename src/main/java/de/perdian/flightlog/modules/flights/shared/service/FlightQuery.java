package de.perdian.flightlog.modules.flights.shared.service;

import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetails;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightDistance;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.FlightType;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class FlightQuery implements Cloneable, Predicate<Flight> {

    private FlightlogUserDetails user = null;
    private Comparator<Flight> comparator = (f1, f2) -> -1 * Flight.compareByDepartureDateAndTime(f1, f2);
    private Collection<UUID> restrictEntityIdentifiers = null;
    private Collection<UUID> excludeEntityIdentifiers = null;
    private Collection<Integer> restrictYears = null;
    private Collection<String> restrictAirlineCodes = null;
    private Collection<String> restrictAirportCodes = null;
    private Collection<String> restrictAircraftTypes = null;
    private Collection<CabinClass> restrictCabinClasses = null;
    private Collection<FlightReason> restrictFlightReasons = null;
    private Collection<FlightDistance> restrictFlightDistances = null;
    private Collection<FlightType> restrictFlightTypes = null;

    public FlightQuery clone() {
        try {
            return (FlightQuery)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cannot clone class: " + this.getClass().getName(), e);
        }
    }

    @Override
    public boolean test(Flight flight) {
        if (!this.testUser(flight)) {
            return false;
        } else if (!this.testRestrictToCollection(this.getRestrictYears(), flight.getDepartureContact().getDateLocal().getYear())) {
            return false;
        } else if (!this.testRestrictToCollection(this.getRestrictAirlineCodes(), flight.getAirline().getCode())) {
            return false;
        } else if (!this.testRestrictToCollection(this.getRestrictAirportCodes(), flight.getDepartureContact().getAirport().getCode(), flight.getArrivalContact().getAirport().getCode())) {
            return false;
        } else if (!this.testRestrictToCollection(this.getRestrictAircraftTypes(), flight.getAircraft().getType())) {
            return false;
        } else if (!this.testRestrictToCollection(this.getRestrictCabinClasses(), flight.getCabinClass())) {
            return false;
        } else if (!this.testRestrictToCollection(this.getRestrictFlightReasons(), flight.getFlightReason())) {
            return false;
        } else if (!this.testRestrictToCollection(this.getRestrictFlightDistances(), flight.getFlightDistanceType())) {
            return false;
        } else if (!this.testRestrictToCollection(this.getRestrictFlightTypes(), flight.getFlightType())) {
            return false;
        } else {
            return true;
        }
    }

    private boolean testUser(Flight flight) {
        UUID flightUserId = flight.getUser() == null ? null : flight.getUser().getUserId();
        UUID queryUserId = this.getUser() == null || this.getUser().getUserEntity() == null ? null : this.getUser().getUserEntity().getUserId();
        return Objects.equals(flightUserId, queryUserId);
    }

    private <T> boolean testRestrictToCollection(Collection<T> compareValues, T... values) {
        if (compareValues == null || compareValues.isEmpty()) {
            return true;
        } else {
            for (T value : values) {
                if (compareValues.contains(value)) {
                    return true;
                }
            }
            return false;
        }
    }

    public FlightQuery withUser(FlightlogUserDetails user) {
        this.setUser(user);
        return this;
    }
    public FlightlogUserDetails getUser() {
        return this.user;
    }
    public void setUser(FlightlogUserDetails user) {
        this.user = user;
    }

    public Comparator<Flight> getComparator() {
        return this.comparator;
    }
    public void setComparator(Comparator<Flight> comparator) {
        this.comparator = comparator;
    }

    public Collection<UUID> getRestrictEntityIdentifiers() {
        return this.restrictEntityIdentifiers;
    }
    public void setRestrictEntityIdentifiers(Collection<UUID> restrictEntityIdentifiers) {
        this.restrictEntityIdentifiers = restrictEntityIdentifiers;
    }

    public Collection<UUID> getExcludeEntityIdentifiers() {
        return this.excludeEntityIdentifiers;
    }
    public void setExcludeEntityIdentifiers(Collection<UUID> excludeEntityIdentifiers) {
        this.excludeEntityIdentifiers = excludeEntityIdentifiers;
    }

    public Collection<Integer> getRestrictYears() {
        return this.restrictYears;
    }
    public void setRestrictYears(Collection<Integer> restrictYears) {
        this.restrictYears = restrictYears;
    }

    public Collection<String> getRestrictAirlineCodes() {
        return this.restrictAirlineCodes;
    }
    public void setRestrictAirlineCodes(Collection<String> restrictAirlineCodes) {
        this.restrictAirlineCodes = restrictAirlineCodes;
    }

    public Collection<String> getRestrictAirportCodes() {
        return this.restrictAirportCodes;
    }
    public void setRestrictAirportCodes(Collection<String> restrictAirportCodes) {
        this.restrictAirportCodes = restrictAirportCodes;
    }

    public Collection<String> getRestrictAircraftTypes() {
        return this.restrictAircraftTypes;
    }
    public void setRestrictAircraftTypes(Collection<String> restrictAircraftTypes) {
        this.restrictAircraftTypes = restrictAircraftTypes;
    }

    public Collection<CabinClass> getRestrictCabinClasses() {
        return this.restrictCabinClasses;
    }
    public void setRestrictCabinClasses(Collection<CabinClass> restrictCabinClasses) {
        this.restrictCabinClasses = restrictCabinClasses;
    }

    public Collection<FlightReason> getRestrictFlightReasons() {
        return this.restrictFlightReasons;
    }
    public void setRestrictFlightReasons(Collection<FlightReason> restrictFlightReasons) {
        this.restrictFlightReasons = restrictFlightReasons;
    }

    public Collection<FlightDistance> getRestrictFlightDistances() {
        return this.restrictFlightDistances;
    }
    public void setRestrictFlightDistances(Collection<FlightDistance> restrictFlightDistances) {
        this.restrictFlightDistances = restrictFlightDistances;
    }

    public Collection<FlightType> getRestrictFlightTypes() {
        return this.restrictFlightTypes;
    }
    public void setRestrictFlightTypes(Collection<FlightType> restrictFlightTypes) {
        this.restrictFlightTypes = restrictFlightTypes;
    }

}
