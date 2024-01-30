package de.perdian.flightlog.modules.flights.exchange;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class FlightsExchangePackage implements Serializable {

    static final long serialVersionUID = 1L;

    private List<FlightsExchangePackageFlight> flights = null;
    private Instant creationTime = Instant.now();

    @JacksonXmlElementWrapper(localName = "flights")
    @JacksonXmlProperty(localName = "flight")
    public List<FlightsExchangePackageFlight> getFlights() {
        return this.flights;
    }
    public void setFlights(List<FlightsExchangePackageFlight> flights) {
        this.flights = flights;
    }

    public Instant getCreationTime() {
        return this.creationTime;
    }
    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

}
