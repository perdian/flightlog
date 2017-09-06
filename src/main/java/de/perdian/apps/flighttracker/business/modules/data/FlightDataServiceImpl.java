package de.perdian.apps.flighttracker.business.modules.data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class FlightDataServiceImpl implements FlightDataService {

    private List<FlightDataSource> sources = null;

    @Override
    public FlightData lookupFlightData(String airlineCode, String flightNumber, LocalDate departureDate) {

        // To get a valid FlightData object we simply loop through all
        // the available sources until the first one returns a valid
        // response
        return Optional.ofNullable(this.getSources()).orElseGet(Collections::emptyList).stream()
            .map(source -> source.lookupFlightData(airlineCode, flightNumber, departureDate))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);

    }

    List<FlightDataSource> getSources() {
        return this.sources;
    }
    @Autowired(required = false)
    void setSources(List<FlightDataSource> sources) {
        this.sources = sources;
    }

}
