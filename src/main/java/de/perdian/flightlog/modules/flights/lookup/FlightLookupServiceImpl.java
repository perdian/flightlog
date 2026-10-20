package de.perdian.flightlog.modules.flights.lookup;

import de.perdian.flightlog.modules.flights.shared.model.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class FlightLookupServiceImpl implements FlightLookupService {

    private static final Logger log = LoggerFactory.getLogger(FlightLookupServiceImpl.class);

    private List<FlightLookupRequestSanitizer> requestSanitizers = null;
    private List<FlightLookupSource> sources = null;

    public List<Flight> lookupFlights(FlightLookupRequest flightLookupRequest) {
        FlightLookupRequest sanitizedRequest = this.sanitizeRequest(flightLookupRequest);
        return Optional.ofNullable(this.getSources()).orElseGet(Collections::emptyList).stream()
            .map(source -> this.lookupFlightsFromSource(source, sanitizedRequest))
            .filter(list -> list != null && !list.isEmpty())
            .flatMap(list -> list.stream())
            .sorted(Flight::compareByDepartureDateAndTime)
            .collect(Collectors.toList());
    }

    private FlightLookupRequest sanitizeRequest(FlightLookupRequest flightLookupRequest) {
        if (this.getRequestSanitizers() != null) {
            for (FlightLookupRequestSanitizer requestSanitizer : this.getRequestSanitizers()) {
                try {
                    FlightLookupRequest sanitizedRequest = requestSanitizer.sanitize(flightLookupRequest);
                    if (sanitizedRequest != null) {
                        return sanitizedRequest;
                    }
                } catch (Exception e) {
                    log.warn("Cannot sanitize flights using '{}' [{}}]", requestSanitizer, flightLookupRequest, e);
                }
            }
        }
        return flightLookupRequest;
    }

    private List<Flight> lookupFlightsFromSource(FlightLookupSource source, FlightLookupRequest flightLookupRequest) {
        try {
            return source.lookupFlights(flightLookupRequest);
        } catch (Exception e) {
            log.warn("Cannot load flights from source '{}' [{}}]", source, flightLookupRequest, e);
            return null;
        }
    }

    List<FlightLookupRequestSanitizer> getRequestSanitizers() {
        return this.requestSanitizers;
    }
    @Autowired(required = false)
    void setRequestSanitizers(List<FlightLookupRequestSanitizer> requestSanitizers) {
        this.requestSanitizers = requestSanitizers;
    }

    List<FlightLookupSource> getSources() {
        return this.sources;
    }
    @Autowired(required = false)
    void setSources(List<FlightLookupSource> sources) {
        this.sources = sources;
    }

}
