package de.perdian.apps.flighttracker.modules.flights.services;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import de.perdian.apps.flighttracker.modules.flights.persistence.FlightEntity;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightDistance;
import de.perdian.apps.flighttracker.support.types.FlightReason;

public class FlightsQuery implements Serializable {

    static final long serialVersionUID = 1L;

    private Integer page = null;
    private Integer pageSize = null;
    private Collection<UUID> restrictIdentifiers = null;
    private Collection<String> restrictFlightNumbers = null;
    private Collection<String> restrictDepartureAirportCodes = null;
    private Collection<String> restrictArrivalAirportCodes = null;
    private Collection<TimePeriod> restrictTimePeriods = null;
    private UserEntity restrictUser = null;
    private Collection<String> restrictAirportCodes = null;
    private Collection<String> restrictAirlineCodes = null;
    private Collection<String> restrictAircraftTypes = null;
    private Collection<CabinClass> restrictCabinClasses = null;
    private Collection<FlightReason> restrictFlightReasons = null;
    private Collection<FlightDistance> restrictFlightDistances = null;

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        toStringBuilder.append("restrictIdentifiers", this.getRestrictIdentifiers());
        toStringBuilder.append("restrictFlightNumbers", this.getRestrictDepartureAirportCodes());
        toStringBuilder.append("restrictDepartureAirportCodes", this.getRestrictDepartureAirportCodes());
        toStringBuilder.append("restrictArrivalAirportCodes", this.getRestrictArrivalAirportCodes());
        toStringBuilder.append("restrictTimePeriods", this.getRestrictTimePeriods());
        toStringBuilder.append("restrictUser", this.getRestrictUser());
        toStringBuilder.append("restrictAirportCodes", this.getRestrictAirportCodes());
        toStringBuilder.append("restrictAirlineCodes", this.getRestrictAirlineCodes());
        toStringBuilder.append("restrictCabinClasses", this.getRestrictCabinClasses());
        toStringBuilder.append("restrictFlightReasons", this.getRestrictFlightReasons());
        toStringBuilder.append("restrictFlightDistances", this.getRestrictFlightDistances());
        return toStringBuilder.toString();
    }

    Predicate toPredicate(Root<FlightEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicateList = new ArrayList<>();
        if (this.getRestrictArrivalAirportCodes() != null && !this.getRestrictArrivalAirportCodes().isEmpty()) {
            predicateList.add(root.get("arrivalAirportCode").in(this.getRestrictArrivalAirportCodes()));
        }
        if (this.getRestrictDepartureAirportCodes() != null && !this.getRestrictDepartureAirportCodes().isEmpty()) {
            predicateList.add(root.get("departureAirportCode").in(this.getRestrictDepartureAirportCodes()));
        }
        if (this.getRestrictTimePeriods() != null && !this.getRestrictTimePeriods().isEmpty()) {
            List<Predicate> timePeriodsPredicateList = new ArrayList<>();
            for (TimePeriod timePeriod : this.getRestrictTimePeriods()) {
                timePeriodsPredicateList.add(timePeriod.toPredicate(root, query, cb));
            }
            predicateList.add(cb.or(timePeriodsPredicateList.toArray(new Predicate[0])));
        }
        if (this.getRestrictFlightNumbers() != null && !this.getRestrictFlightNumbers().isEmpty()) {
            predicateList.add(root.get("flightNumber").in(this.getRestrictFlightNumbers()));
        }
        if (this.getRestrictIdentifiers() != null && !this.getRestrictIdentifiers().isEmpty()) {
            predicateList.add(root.get("id").in(this.getRestrictIdentifiers()));
        }
        if (this.getRestrictUser() != null) {
            predicateList.add(root.get("user").in(Collections.singleton(this.getRestrictUser())));
        } else {
            predicateList.add(cb.isNull(root.get("user")));
        }
        if (this.getRestrictIdentifiers() != null && !this.getRestrictIdentifiers().isEmpty()) {
            predicateList.add(root.get("id").in(this.getRestrictIdentifiers()));
        }
        if (this.getRestrictAirlineCodes() != null && !this.getRestrictAirlineCodes().isEmpty()) {
            predicateList.add(root.get("airlineCode").in(this.getRestrictAirlineCodes()));
        }
        if (this.getRestrictAircraftTypes() != null && !this.getRestrictAircraftTypes().isEmpty()) {
            predicateList.add(root.get("aircraftType").in(this.getRestrictAircraftTypes()));
        }
        if (this.getRestrictAirportCodes() != null && !this.getRestrictAirportCodes().isEmpty()) {
            predicateList.add(cb.or(root.get("departureAirportCode").in(this.getRestrictAirportCodes()), root.get("arrivalAirportCode").in(this.getRestrictAirportCodes())));
        }
        if (this.getRestrictCabinClasses() != null && !this.getRestrictCabinClasses().isEmpty()) {
            predicateList.add(root.get("cabinClass").in(this.getRestrictCabinClasses()));
        }
        if (this.getRestrictFlightReasons() != null && !this.getRestrictFlightReasons().isEmpty()) {
            predicateList.add(root.get("flightReason").in(this.getRestrictFlightReasons()));
        }
        if (this.getRestrictFlightDistances() != null && !this.getRestrictFlightDistances().isEmpty()) {
            List<Predicate> flightDistancesPredicateList = new ArrayList<>();
            for (FlightDistance flightDistance : this.getRestrictFlightDistances()) {
                int minValue = Optional.ofNullable(flightDistance.getMinValue()).orElse(0);
                int maxValue = Optional.ofNullable(flightDistance.getMaxValue()).orElse(Integer.MAX_VALUE);
                flightDistancesPredicateList.add(cb.between(root.get("flightDistance"), minValue, maxValue));
            }
            predicateList.add(cb.or(flightDistancesPredicateList.toArray(new Predicate[0])));
        }
        return cb.and(predicateList.toArray(new Predicate[0]));
    }

    public static class TimePeriod implements Serializable {

        static final long serialVersionUID = 1L;

        private LocalDate minimumDepartureDateLocal = null;
        private LocalDate maximumDepartureDateLocal = null;
        private LocalDate minimumArrivalDateLocal = null;
        private LocalDate maximumArrivalDateLocal = null;

        @Override
        public String toString() {
            ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
            toStringBuilder.append("minimumDepartureDateLocal", this.getMinimumDepartureDateLocal());
            toStringBuilder.append("maximumDepartureDateLocal", this.getMaximumDepartureDateLocal());
            toStringBuilder.append("minimumArrivalDateLocal", this.getMinimumArrivalDateLocal());
            toStringBuilder.append("maximumArrivalDateLocal", this.getMaximumArrivalDateLocal());
            return toStringBuilder.toString();
        }

        public Predicate toPredicate(Root<FlightEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            List<Predicate> predicateList = new ArrayList<>();
            if (this.getMaximumArrivalDateLocal() != null) {
                predicateList.add(cb.lessThanOrEqualTo(root.get("arrivalDateLocal"), this.getMaximumArrivalDateLocal()));
            }
            if (this.getMaximumDepartureDateLocal() != null) {
                predicateList.add(cb.lessThanOrEqualTo(root.get("departureDateLocal"), this.getMaximumDepartureDateLocal()));
            }
            if (this.getMinimumArrivalDateLocal() != null) {
                predicateList.add(cb.greaterThanOrEqualTo(root.get("arrivalDateLocal"), this.getMinimumArrivalDateLocal()));
            }
            if (this.getMinimumDepartureDateLocal() != null) {
                predicateList.add(cb.greaterThanOrEqualTo(root.get("departureDateLocal"), this.getMinimumDepartureDateLocal()));
            }
            return cb.and(predicateList.toArray(new Predicate[0]));
        }

        public LocalDate getMinimumDepartureDateLocal() {
            return this.minimumDepartureDateLocal;
        }
        public void setMinimumDepartureDateLocal(LocalDate minimumDepartureDateLocal) {
            this.minimumDepartureDateLocal = minimumDepartureDateLocal;
        }

        public LocalDate getMaximumDepartureDateLocal() {
            return this.maximumDepartureDateLocal;
        }
        public void setMaximumDepartureDateLocal(LocalDate maximumDepartureDateLocal) {
            this.maximumDepartureDateLocal = maximumDepartureDateLocal;
        }

        public LocalDate getMinimumArrivalDateLocal() {
            return this.minimumArrivalDateLocal;
        }
        public void setMinimumArrivalDateLocal(LocalDate minimumArrivalDateLocal) {
            this.minimumArrivalDateLocal = minimumArrivalDateLocal;
        }

        public LocalDate getMaximumArrivalDateLocal() {
            return this.maximumArrivalDateLocal;
        }
        public void setMaximumArrivalDateLocal(LocalDate maximumArrivalDateLocal) {
            this.maximumArrivalDateLocal = maximumArrivalDateLocal;
        }

    }

    public Integer getPage() {
        return this.page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Collection<UUID> getRestrictIdentifiers() {
        return this.restrictIdentifiers;
    }
    public void setRestrictIdentifiers(Collection<UUID> restrictIdentifiers) {
        this.restrictIdentifiers = restrictIdentifiers;
    }

    public Collection<String> getRestrictFlightNumbers() {
        return this.restrictFlightNumbers;
    }
    public void setRestrictFlightNumbers(Collection<String> restrictFlightNumbers) {
        this.restrictFlightNumbers = restrictFlightNumbers;
    }

    public Collection<String> getRestrictDepartureAirportCodes() {
        return this.restrictDepartureAirportCodes;
    }
    public void setRestrictDepartureAirportCodes(Collection<String> restrictDepartureAirportCodes) {
        this.restrictDepartureAirportCodes = restrictDepartureAirportCodes;
    }

    public Collection<String> getRestrictArrivalAirportCodes() {
        return this.restrictArrivalAirportCodes;
    }
    public void setRestrictArrivalAirportCodes(Collection<String> restrictArrivalAirportCodes) {
        this.restrictArrivalAirportCodes = restrictArrivalAirportCodes;
    }

    public Collection<TimePeriod> getRestrictTimePeriods() {
        return this.restrictTimePeriods;
    }
    public void setRestrictTimePeriods(Collection<TimePeriod> restrictTimePeriods) {
        this.restrictTimePeriods = restrictTimePeriods;
    }

    public UserEntity getRestrictUser() {
        return this.restrictUser;
    }
    public void setRestrictUser(UserEntity restrictUser) {
        this.restrictUser = restrictUser;
    }

    public Collection<String> getRestrictAirportCodes() {
        return this.restrictAirportCodes;
    }
    public void setRestrictAirportCodes(Collection<String> restrictAirportCodes) {
        this.restrictAirportCodes = restrictAirportCodes;
    }

    public Collection<String> getRestrictAirlineCodes() {
        return this.restrictAirlineCodes;
    }
    public void setRestrictAirlineCodes(Collection<String> restrictAirlineCodes) {
        this.restrictAirlineCodes = restrictAirlineCodes;
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

}
