package de.perdian.flightlog.modules.flights.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.perdian.flightlog.modules.aircrafts.persistence.AircraftTypeEntity;
import de.perdian.flightlog.modules.aircrafts.persistence.AircraftTypesRepository;
import de.perdian.flightlog.modules.airlines.persistence.AirlineEntity;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;
import de.perdian.flightlog.modules.airports.persistence.AirportEntity;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.modules.flights.service.FlightLookupSource;
import de.perdian.flightlog.modules.flights.service.model.*;
import de.perdian.flightlog.support.FlightlogHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Flightradar24LookupSource implements FlightLookupSource {

    private static final Logger log = LoggerFactory.getLogger(Flightradar24LookupSource.class);
    private static final Pattern AIRPORT_CODE_PATTERN = Pattern.compile("\\(([A-Z]+)\\)");
    private static final Pattern AIRCRAFT_REGISTRATION_PATTERN = Pattern.compile("\\((.+?)\\)");

    private AircraftTypesRepository aircraftTypesRepository = null;
    private AirportsRepository airportsRepository = null;
    private AirlinesRepository airlinesRepository = null;
    private Clock clock = Clock.systemUTC();
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
                AirlineEntity airlineEntity = this.getAirlinesRepository().loadAirlineByCode(actualAirlineCode);
                return this.createFlightFromDataTable(dataTableElement, airlineEntity, actualFlightNumber, flightLookupRequest);
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

    private List<Flight> createFlightFromDataTable(Element dataTableElement, AirlineEntity airlineEntity, String flightNumber, FlightLookupRequest flightLookupRequest) {
        return dataTableElement.select("tbody tr").stream()
            .map(row -> this.createDataRowFromDataTableRow(row))
            .filter(dataRow -> flightLookupRequest.getDepartureDate() != null && dataRow.matchesDepartureLocalDate(flightLookupRequest.getDepartureDate()))
            .filter(dataRow -> StringUtils.isEmpty(flightLookupRequest.getDepartureAirportCode()) || dataRow.matchesDepartureAirportCode(flightLookupRequest.getDepartureAirportCode()))
            .map(dataRow -> this.createFlightFromDataRow(dataRow, airlineEntity, flightNumber))
            .toList();
    }

    private Flightradar24DataRow createDataRowFromDataTableRow(Element sourceRow) {

        Elements cells = sourceRow.select("td");
        Element departureCate = cells.get(2);
        LocalDate departureDateUtc = LocalDate.parse(departureCate.text().strip(), DateTimeFormatter.ofPattern("dd MMM yyyy").withLocale(Locale.ENGLISH));

        Element departureAirportCell = cells.get(3);
        String departureAirportCodePlusBrackets = departureAirportCell.selectFirst("a").text().strip();
        Matcher departureAirportMatcher = AIRPORT_CODE_PATTERN.matcher(departureAirportCodePlusBrackets);
        String departureAirportCode = departureAirportMatcher.matches() ? departureAirportMatcher.group(1) : null;
        AirportEntity departureAirportEntity = this.getAirportsRepository().loadAirportByIataCode(departureAirportCode);

        Element arrivalAirportCell = cells.get(4);
        String arrivalAirportCodePlusBrackets = arrivalAirportCell.selectFirst("a").text().strip();
        Matcher arrivalAirportMatcher = AIRPORT_CODE_PATTERN.matcher(arrivalAirportCodePlusBrackets);
        String arrivalAirportCode = arrivalAirportMatcher.matches() ? arrivalAirportMatcher.group(1) : null;
        AirportEntity arrivalAirportEntity = this.getAirportsRepository().loadAirportByIataCode(arrivalAirportCode);

        Element aircraftCell = cells.get(5);
        String aircraftTypeValue = aircraftCell.text().strip();
        int aircraftTypeCodeEndIndex = aircraftTypeValue == null ? null : aircraftTypeValue.indexOf(" ");
        String aircraftTypeCode = aircraftTypeValue == null ? null : (aircraftTypeCodeEndIndex < 0 ? aircraftTypeValue : aircraftTypeValue.substring(0, aircraftTypeCodeEndIndex));
        AircraftTypeEntity aircraftTypeEntity = StringUtils.isEmpty(aircraftTypeCode) ? null : this.getAircraftTypesRepository().loadAircraftTypeByCode(aircraftTypeCode);
        String aircraftType = aircraftTypeEntity == null ? aircraftTypeCode : aircraftTypeEntity.getTitle();

        Element aircraftRegistrationElement = aircraftCell.selectFirst("a");
        String aircaftRegistrationValue = aircraftRegistrationElement == null ? null : aircraftRegistrationElement.text().strip();
        Matcher aircraftRegistrationMatcher = StringUtils.isEmpty(aircaftRegistrationValue) ? null : AIRCRAFT_REGISTRATION_PATTERN.matcher(aircaftRegistrationValue);
        String aircraftRegistratopm = aircraftRegistrationMatcher != null && aircraftRegistrationMatcher.matches() ? aircraftRegistrationMatcher.group(1) : null;

        Element flightTimeElement = cells.get(6);
        String flightTimeValue = flightTimeElement.text().strip();
        Duration flightTime = flightTimeValue != null && flightTimeValue.length() > 1 ? FlightlogHelper.parseDuration(flightTimeValue) : null;

        Element actualDepartureTimeCell = cells.get(8);
        String actualDepartureTimeUtcValue = actualDepartureTimeCell.text().strip();
        LocalTime actualDepartureTimeUtc = actualDepartureTimeUtcValue.length() > 1 ? LocalTime.parse(actualDepartureTimeUtcValue) : null;

        Element statusCell = cells.get(11);
        String status = statusCell.text().strip();

        Flightradar24DataRow dataRow = new Flightradar24DataRow();
        dataRow.setDepartureAirportEntity(departureAirportEntity);
        dataRow.setDepartureAirportCode(departureAirportEntity == null ? departureAirportCode : departureAirportEntity.getIataCode());
        dataRow.setDepartureDateUtc(departureDateUtc);
        dataRow.setDepartureTimeUtc(actualDepartureTimeUtc);
        dataRow.setArrivalAirportEntity(arrivalAirportEntity);
        dataRow.setArrivalAirportCode(arrivalAirportEntity == null ? arrivalAirportCode : arrivalAirportEntity.getIataCode());
        dataRow.setDuration(flightTime);
        dataRow.setAircraftType(aircraftType);
        dataRow.setAircraftRegistration(aircraftRegistratopm);
        dataRow.setStatus(status);
        return dataRow;

    }

    private Flight createFlightFromDataRow(Flightradar24DataRow dataRow, AirlineEntity airlineEntity, String flightNumber) {

        ZoneId departureZoneId = dataRow.getDepartureAirportEntity() == null ? null : dataRow.getDepartureAirportEntity().getTimezoneId();
        ZonedDateTime departureDateTimeUtc = dataRow.getDepartureDateUtc() == null  || dataRow.getDepartureTimeUtc() == null ? null : dataRow.getDepartureTimeUtc().atDate(dataRow.getDepartureDateUtc()).atZone(ZoneId.of("UTC"));
        ZonedDateTime departureDateTimeLocal = departureDateTimeUtc == null || departureZoneId == null ? null : departureDateTimeUtc.withZoneSameInstant(departureZoneId);
        AirportEntity departureAirportEntity = dataRow.getDepartureAirportEntity();
        AirportContact departureContact = new AirportContact();
        departureContact.setAirport(departureAirportEntity == null ? null : new Airport(departureAirportEntity));
        departureContact.setDateLocal(departureDateTimeLocal == null ? null : departureDateTimeLocal.toLocalDate());
        departureContact.setTimeLocal(departureDateTimeLocal == null ? null : departureDateTimeLocal.toLocalTime());

        ZoneId arrivalZoneId = dataRow.getArrivalAirportEntity() == null ? null : dataRow.getArrivalAirportEntity().getTimezoneId();
        ZonedDateTime arrivalDateTimeUtc = dataRow.getDuration() == null || departureDateTimeUtc == null ? null : departureDateTimeUtc.plus(dataRow.getDuration());
        ZonedDateTime arrivalDateTimeLocal = arrivalDateTimeUtc == null || arrivalZoneId == null ? null : arrivalDateTimeUtc.withZoneSameInstant(arrivalZoneId);
        AirportEntity arrivalAirportEntity = dataRow.getArrivalAirportEntity();
        AirportContact arrivalContact = new AirportContact();
        arrivalContact.setAirport(arrivalAirportEntity == null ? null : new Airport(arrivalAirportEntity));
        arrivalContact.setDateLocal(arrivalDateTimeLocal == null ? null : arrivalDateTimeLocal.toLocalDate());
        arrivalContact.setTimeLocal(arrivalDateTimeLocal == null ? null : arrivalDateTimeLocal.toLocalTime());

        Duration flightDuration = departureDateTimeUtc == null || arrivalDateTimeUtc == null ? null : Duration.between(departureDateTimeUtc, arrivalDateTimeUtc);
        Integer flightDistance = departureAirportEntity == null || arrivalAirportEntity == null ? null : FlightlogHelper.computeDistanceInKilometers(departureAirportEntity.getLongitude(), departureAirportEntity.getLatitude(), arrivalAirportEntity.getLongitude(), arrivalAirportEntity.getLatitude());

        Aircraft aircraft = new Aircraft();
        aircraft.setType(dataRow.getAircraftType());
        aircraft.setRegistration(dataRow.getAircraftRegistration());

        Flight flight = new Flight();
        flight.setDepartureContact(departureContact);
        flight.setArrivalContact(arrivalContact);
        flight.setAircraft(aircraft);
        flight.setAirline(airlineEntity);
        flight.setFlightNumber(flightNumber);
        flight.setFlightDuration(flightDuration);
        flight.setFlightDistance(flightDistance);
        return flight;

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

    Clock getClock() {
        return this.clock;
    }
    void setClock(Clock clock) {
        this.clock = clock;
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
