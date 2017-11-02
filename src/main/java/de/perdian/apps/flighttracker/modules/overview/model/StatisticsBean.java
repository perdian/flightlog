package de.perdian.apps.flighttracker.modules.overview.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.support.types.FlightDistance;

public class StatisticsBean implements Serializable {

    static final long serialVersionUID = 1L;

    private Integer distanceInKilometers = null;
    private Integer distanceInMiles = null;
    private Double earthOrbits = null;
    private Double earthToMoon = null;
    private Double earthToSun = null;
    private Double durationInHours = null;
    private Double durationInDays = null;
    private Double durationInWeeks = null;
    private Double durationInMonths = null;
    private Double durationInYears = null;
    private Integer numberOfFlights = null;
    private Map<FlightDistance, Integer> numberOfFlightsByDistance = null;
    private FlightBean longestFlightByDistance = null;
    private FlightBean shortestFlightByDistance = null;
    private FlightBean longestFlightByDuration = null;
    private FlightBean shortestFlightByDuration = null;
    private FlightBean fastestFlight = null;
    private FlightBean slowestFlight = null;
    private List<StatisticsTopItem> topAirports = null;
    private List<StatisticsTopItem> topAirlines = null;
    private List<StatisticsTopItem> topAircraftTypes = null;
    private List<StatisticsTopItem> topRoutes = null;
    private Integer numberOfDifferentAirports = null;
    private Integer numberOfDifferentAirlines = null;
    private Integer numberOfDifferentAircraftTypes = null;
    private Integer numberOfDifferentRoutes = null;
    private Integer numberOfDifferentCountries = null;
    private List<StatisticsTopItem> distributionOfCabinClasses = null;
    private List<StatisticsTopItem> distributionOfSeatTypes = null;
    private List<StatisticsTopItem> distributionOfFlightReasons = null;

    public Integer getDistanceInKilometers() {
        return this.distanceInKilometers;
    }
    public void setDistanceInKilometers(Integer distanceInKilometers) {
        this.distanceInKilometers = distanceInKilometers;
    }

    public Integer getDistanceInMiles() {
        return this.distanceInMiles;
    }
    public void setDistanceInMiles(Integer distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
    }

    public Double getEarthOrbits() {
        return this.earthOrbits;
    }
    public void setEarthOrbits(Double earthOrbits) {
        this.earthOrbits = earthOrbits;
    }

    public Double getEarthToMoon() {
        return this.earthToMoon;
    }
    public void setEarthToMoon(Double earthToMoon) {
        this.earthToMoon = earthToMoon;
    }

    public Double getEarthToSun() {
        return this.earthToSun;
    }
    public void setEarthToSun(Double earthToSun) {
        this.earthToSun = earthToSun;
    }

    public Double getDurationInHours() {
        return this.durationInHours;
    }
    public void setDurationInHours(Double durationInHours) {
        this.durationInHours = durationInHours;
    }

    public Double getDurationInDays() {
        return this.durationInDays;
    }
    public void setDurationInDays(Double durationInDays) {
        this.durationInDays = durationInDays;
    }

    public Double getDurationInWeeks() {
        return this.durationInWeeks;
    }
    public void setDurationInWeeks(Double durationInWeeks) {
        this.durationInWeeks = durationInWeeks;
    }

    public Double getDurationInMonths() {
        return this.durationInMonths;
    }
    public void setDurationInMonths(Double durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public Double getDurationInYears() {
        return this.durationInYears;
    }
    public void setDurationInYears(Double durationInYears) {
        this.durationInYears = durationInYears;
    }

    public Integer getNumberOfFlights() {
        return this.numberOfFlights;
    }
    public void setNumberOfFlights(Integer numberOfFlights) {
        this.numberOfFlights = numberOfFlights;
    }

    public Map<FlightDistance, Integer> getNumberOfFlightsByDistance() {
        return this.numberOfFlightsByDistance;
    }
    public void setNumberOfFlightsByDistance(Map<FlightDistance, Integer> numberOfFlightsByDistance) {
        this.numberOfFlightsByDistance = numberOfFlightsByDistance;
    }

    public FlightBean getLongestFlightByDistance() {
        return this.longestFlightByDistance;
    }
    public void setLongestFlightByDistance(FlightBean longestFlightByDistance) {
        this.longestFlightByDistance = longestFlightByDistance;
    }

    public FlightBean getShortestFlightByDistance() {
        return this.shortestFlightByDistance;
    }
    public void setShortestFlightByDistance(FlightBean shortestFlightByDistance) {
        this.shortestFlightByDistance = shortestFlightByDistance;
    }

    public FlightBean getLongestFlightByDuration() {
        return this.longestFlightByDuration;
    }
    public void setLongestFlightByDuration(FlightBean longestFlightByDuration) {
        this.longestFlightByDuration = longestFlightByDuration;
    }

    public FlightBean getShortestFlightByDuration() {
        return this.shortestFlightByDuration;
    }
    public void setShortestFlightByDuration(FlightBean shortestFlightByDuration) {
        this.shortestFlightByDuration = shortestFlightByDuration;
    }

    public FlightBean getFastestFlight() {
        return this.fastestFlight;
    }
    public void setFastestFlight(FlightBean fastestFlight) {
        this.fastestFlight = fastestFlight;
    }

    public FlightBean getSlowestFlight() {
        return this.slowestFlight;
    }
    public void setSlowestFlight(FlightBean slowestFlight) {
        this.slowestFlight = slowestFlight;
    }

    public List<StatisticsTopItem> getTopAirports() {
        return this.topAirports;
    }
    public void setTopAirports(List<StatisticsTopItem> topAirports) {
        this.topAirports = topAirports;
    }

    public List<StatisticsTopItem> getTopAirlines() {
        return this.topAirlines;
    }
    public void setTopAirlines(List<StatisticsTopItem> topAirlines) {
        this.topAirlines = topAirlines;
    }

    public List<StatisticsTopItem> getTopAircraftTypes() {
        return this.topAircraftTypes;
    }
    public void setTopAircraftTypes(List<StatisticsTopItem> topAircraftTypes) {
        this.topAircraftTypes = topAircraftTypes;
    }

    public List<StatisticsTopItem> getTopRoutes() {
        return this.topRoutes;
    }
    public void setTopRoutes(List<StatisticsTopItem> topRoutes) {
        this.topRoutes = topRoutes;
    }

    public Integer getNumberOfDifferentAirports() {
        return this.numberOfDifferentAirports;
    }
    public void setNumberOfDifferentAirports(Integer numberOfDifferentAirports) {
        this.numberOfDifferentAirports = numberOfDifferentAirports;
    }

    public Integer getNumberOfDifferentAirlines() {
        return this.numberOfDifferentAirlines;
    }
    public void setNumberOfDifferentAirlines(Integer numberOfDifferentAirlines) {
        this.numberOfDifferentAirlines = numberOfDifferentAirlines;
    }

    public Integer getNumberOfDifferentAircraftTypes() {
        return this.numberOfDifferentAircraftTypes;
    }
    public void setNumberOfDifferentAircraftTypes(Integer numberOfDifferentAircraftTypes) {
        this.numberOfDifferentAircraftTypes = numberOfDifferentAircraftTypes;
    }

    public Integer getNumberOfDifferentRoutes() {
        return this.numberOfDifferentRoutes;
    }
    public void setNumberOfDifferentRoutes(Integer numberOfDifferentRoutes) {
        this.numberOfDifferentRoutes = numberOfDifferentRoutes;
    }

    public Integer getNumberOfDifferentCountries() {
        return this.numberOfDifferentCountries;
    }
    public void setNumberOfDifferentCountries(Integer numberOfDifferentCountries) {
        this.numberOfDifferentCountries = numberOfDifferentCountries;
    }

    public List<StatisticsTopItem> getDistributionOfCabinClasses() {
        return this.distributionOfCabinClasses;
    }
    public void setDistributionOfCabinClasses(List<StatisticsTopItem> distributionOfCabinClasses) {
        this.distributionOfCabinClasses = distributionOfCabinClasses;
    }

    public List<StatisticsTopItem> getDistributionOfSeatTypes() {
        return this.distributionOfSeatTypes;
    }
    public void setDistributionOfSeatTypes(List<StatisticsTopItem> distributionOfSeatTypes) {
        this.distributionOfSeatTypes = distributionOfSeatTypes;
    }

    public List<StatisticsTopItem> getDistributionOfFlightReasons() {
        return this.distributionOfFlightReasons;
    }
    public void setDistributionOfFlightReasons(List<StatisticsTopItem> distributionOfFlightReasons) {
        this.distributionOfFlightReasons = distributionOfFlightReasons;
    }

}
