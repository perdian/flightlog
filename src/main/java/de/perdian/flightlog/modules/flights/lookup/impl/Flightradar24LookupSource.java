package de.perdian.flightlog.modules.flights.lookup.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.perdian.flightlog.modules.aircrafts.model.Aircraft;
import de.perdian.flightlog.modules.aircrafts.model.AircraftType;
import de.perdian.flightlog.modules.aircrafts.persistence.AircraftTypesRepository;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;
import de.perdian.flightlog.modules.airports.model.Airport;
import de.perdian.flightlog.modules.airports.model.AirportContact;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.modules.flights.lookup.FlightLookupRequest;
import de.perdian.flightlog.modules.flights.lookup.FlightLookupSource;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.support.FlightlogHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Flightradar24LookupSource implements FlightLookupSource {

    private static final Logger log = LoggerFactory.getLogger(Flightradar24LookupSource.class);

    private AircraftTypesRepository aircraftTypesRepository = null;
    private AirportsRepository airportsRepository = null;
    private AirlinesRepository airlinesRepository = null;
    private OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Flight> lookupFlights(FlightLookupRequest flightLookupRequest) {

        String resolvedFlightNumber = this.resolveActualFlightNumber(flightLookupRequest);
        Matcher resolvedFlightNumberMatcher = Pattern.compile("([a-zA-Z]+)(\\d+)").matcher(resolvedFlightNumber);
        String actualAirlineCode = resolvedFlightNumberMatcher.matches() ? resolvedFlightNumberMatcher.group(1) : flightLookupRequest.getAirlineCode();
        String actualFlightNumber = resolvedFlightNumberMatcher.matches() ? resolvedFlightNumberMatcher.group(2) : flightLookupRequest.getFlightNumber();

        String httpUrl = "https://www.flightradar24.com/data/flights/" + actualAirlineCode + actualFlightNumber;
        log.debug("Querying flightradar24 for flight {}{} on {} using URL: {}", actualAirlineCode, actualFlightNumber, flightLookupRequest.getDepartureDate(), httpUrl);
        Request httpRequest = new Request.Builder().get().url(httpUrl).addHeader("User-Agent", "curl").build();
        try (Response httpResponse = this.getHttpClient().newCall(httpRequest).execute()) {
            String httpResponseBody = httpResponse.body().string();
            Document htmlDocument = Jsoup.parse(httpResponseBody);
            Element dataTableElement = htmlDocument.selectFirst("table#tbl-datatable");
            if (dataTableElement != null) {
                return this.createFlightFromDataTable(dataTableElement, actualAirlineCode, actualFlightNumber, flightLookupRequest);
            }
        } catch (Exception e) {
            log.debug("Cannot retreive data from flightradar24 for: {}", flightLookupRequest, e);
        }

        return Collections.emptyList();

    }

    private String resolveActualFlightNumber(FlightLookupRequest flightLookupRequest) {
        try {

            String completeFlightNumber = flightLookupRequest.getAirlineCode() + flightLookupRequest.getFlightNumber();
            String httpUrl = "https://www.flightradar24.com/v1/search/web/find?query=" + completeFlightNumber;
            log.debug("Querying flightradar24 for potential codeshare information of flight on {} using URL: {}", flightLookupRequest, httpUrl);
            Request httpRequest = new Request.Builder().get().url(httpUrl).addHeader("User-Agent", "curl").build();

            try (Response httpResponse = this.getHttpClient().newCall(httpRequest).execute()) {
                String httpResponseContent = httpResponse.body().string();
                JsonNode jsonResponse = this.getObjectMapper().readTree(httpResponseContent);
                JsonNode resultsArray = jsonResponse.get("results");
                for (int i=0; i < resultsArray.size(); i++) {
                    JsonNode result = resultsArray.get(i);
                    String labelvalue = result.get("label").asText();
                    String matchValue = result.get("match").asText();
                    if (labelvalue.startsWith(completeFlightNumber + " ") && "codeshare".equalsIgnoreCase(matchValue)) {
                        JsonNode resultDetail = result.get("detail");
                        String actualFlightNumber = resultDetail.get("flight").asText();
                        if (StringUtils.isNotEmpty(actualFlightNumber)) {
                            return actualFlightNumber;
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.debug("Cannot evaluate flightradar24 codeshare result", e);
        }
        return flightLookupRequest.getAirlineCode() + flightLookupRequest.getFlightNumber();
    }

    private List<Flight> createFlightFromDataTable(Element dataTableElement, String airlineCode, String flightNumber, FlightLookupRequest flightLookupRequest) {
        return new Flightradar24DataTable(dataTableElement).extractFlights().stream()
            .map(flight -> this.createFlight(airlineCode, flightNumber, flight))
            .filter(flightLookupRequest::test)
            .toList();
    }

    private Flight createFlight(String airlineCode, String flightNumber, Flightradar24DataFlight internalFlight) {

        Airport departureAirport = this.getAirportsRepository().loadAirportByCode(internalFlight.getDepartureAirportCode());
        LocalDateTime departureLocalTime = departureAirport.computeLocalDateTime(internalFlight.getDepartureTime());
        AirportContact departureContact = new AirportContact();
        departureContact.setAirport(departureAirport);
        departureContact.setDateLocal(departureLocalTime.toLocalDate());
        departureContact.setTimeLocal(departureLocalTime.toLocalTime());
        departureContact.setDateTimeUtc(internalFlight.getDepartureTime());

        Airport arrivalAirport = this.getAirportsRepository().loadAirportByCode(internalFlight.getArrivalAirportCode());
        LocalDateTime arrivalLocalTime = arrivalAirport.computeLocalDateTime(internalFlight.getArrivalTime());
        AirportContact arrivalContact = new AirportContact();
        arrivalContact.setAirport(arrivalAirport);
        arrivalContact.setDateLocal(arrivalLocalTime.toLocalDate());
        arrivalContact.setTimeLocal(arrivalLocalTime.toLocalTime());
        arrivalContact.setDateTimeUtc(internalFlight.getArrivalTime());

        AircraftType aircraftType = this.getAircraftTypesRepository().loadAircraftTypeByCode(internalFlight.getAircraftTypeCode());
        Aircraft aircraft = new Aircraft();
        aircraft.setType(aircraftType.getTitle());
        aircraft.setRegistration(internalFlight.getAircraftRegistration());

        Flight externalFlight = new Flight();
        externalFlight.setDepartureContact(departureContact);
        externalFlight.setArrivalContact(arrivalContact);
        externalFlight.setFlightDistance(FlightlogHelper.computeDistanceInKilometers(departureAirport.getLongitude(), departureAirport.getLatitude(), arrivalAirport.getLongitude(), arrivalAirport.getLatitude()));
        externalFlight.setFlightDuration(Duration.between(internalFlight.getDepartureTime(), internalFlight.getArrivalTime().plus(1, ChronoUnit.MINUTES)));
        externalFlight.setAircraft(aircraft);
        externalFlight.setAirline(this.getAirlinesRepository().loadAirlineByCode(airlineCode));
        externalFlight.setFlightNumber(flightNumber);
        return externalFlight;

    }

    AircraftTypesRepository getAircraftTypesRepository() {
        return this.aircraftTypesRepository;
    }
    @Autowired
    void setAircraftTypesRepository(AircraftTypesRepository aircraftTypesRepository) {
        this.aircraftTypesRepository = aircraftTypesRepository;
    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

    AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

    OkHttpClient getHttpClient() {
        return this.httpClient;
    }
    void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }
    void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}
