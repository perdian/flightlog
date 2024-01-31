package de.perdian.flightlog.modules.flights.shared.service;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.flights.shared.model.Flight;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class FlightQuery implements Predicate<Flight> {

    private User user = null;
    private Comparator<Flight> comparator = (f1, f2) -> -1 * Flight.compareByDepartureDateAndTime(f1, f2);
    private Collection<UUID> restrictEntityIdentifiers = null;
    private Collection<UUID> excludeEntityIdentifiers = null;

    public FlightQuery(User user) {
        this.setUser(user);
    }

    @Override
    public boolean test(Flight flight) {
        if (!this.testUser(flight)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean testUser(Flight flight) {
        UUID flightUserId = flight.getUser() == null ? null : flight.getUser().getUserId();
        UUID queryUserId = this.getUser() == null || this.getUser().getEntity() == null ? null : this.getUser().getEntity().getUserId();
        return Objects.equals(flightUserId, queryUserId);
    }

    public User getUser() {
        return this.user;
    }
    public void setUser(User user) {
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

}