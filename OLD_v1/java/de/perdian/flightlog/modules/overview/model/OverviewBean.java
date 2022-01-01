package de.perdian.flightlog.modules.overview.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import de.perdian.flightlog.modules.flights.model.FlightBean;

public class OverviewBean implements Serializable {

    static final long serialVersionUID = 1L;

    private int numberOfFlights = 0;
    private List<OverviewItem> general = null;
    private List<OverviewItem> distances = null;
    private List<OverviewItem> differents = null;
    private Map<String, FlightBean> maxFlights = null;
    private Map<String, List<OverviewItem>> topFlights = null;
    private Map<String, List<OverviewItem>> distributions = null;

    public int getNumberOfFlights() {
        return this.numberOfFlights;
    }
    public void setNumberOfFlights(int numberOfFlights) {
        this.numberOfFlights = numberOfFlights;
    }

    public List<OverviewItem> getGeneral() {
        return this.general;
    }
    public void setGeneral(List<OverviewItem> general) {
        this.general = general;
    }

    public List<OverviewItem> getDistances() {
        return this.distances;
    }
    public void setDistances(List<OverviewItem> distances) {
        this.distances = distances;
    }

    public List<OverviewItem> getDifferents() {
        return this.differents;
    }
    public void setDifferents(List<OverviewItem> differents) {
        this.differents = differents;
    }

    public Map<String, FlightBean> getMaxFlights() {
        return this.maxFlights;
    }
    public void setMaxFlights(Map<String, FlightBean> maxFlights) {
        this.maxFlights = maxFlights;
    }

    public Map<String, List<OverviewItem>> getTopFlights() {
        return this.topFlights;
    }
    public void setTopFlights(Map<String, List<OverviewItem>> topFlights) {
        this.topFlights = topFlights;
    }

    public Map<String, List<OverviewItem>> getDistributions() {
        return this.distributions;
    }
    public void setDistributions(Map<String, List<OverviewItem>> distributions) {
        this.distributions = distributions;
    }

}
