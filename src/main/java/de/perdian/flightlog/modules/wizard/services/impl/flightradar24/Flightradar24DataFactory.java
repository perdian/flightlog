package de.perdian.flightlog.modules.wizard.services.impl.flightradar24;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.perdian.flightlog.modules.aircrafts.persistence.AircraftTypeEntity;
import de.perdian.flightlog.modules.aircrafts.persistence.AircraftTypesRepository;
import de.perdian.flightlog.modules.airports.persistence.AirportEntity;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.modules.wizard.services.WizardData;
import de.perdian.flightlog.modules.wizard.services.WizardDataFactory;
import de.perdian.flightlog.support.FlightlogHelper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@Component
public class Flightradar24DataFactory implements WizardDataFactory {

    private static final Logger log = LoggerFactory.getLogger(Flightradar24DataFactory.class);
    private static final Pattern AIRPORT_CODE_PATTERN = Pattern.compile("\\(([A-Z]+)\\)");
    private static final Pattern AIRCRAFT_REGISTRATION_PATTERN = Pattern.compile("\\((.+?)\\)");

    private AircraftTypesRepository aircraftTypesRepository = null;
    private AirportsRepository airportsRepository = null;
    private Clock clock = Clock.systemUTC();

    @Override
    public List<WizardData> createData(String airlineCode, String flightNumber, LocalDate departureDate, String departureAirportCode) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            String resolvedFlightNumber = this.resolveActualFlightNumber(airlineCode, flightNumber, departureDate, httpClient);
            Matcher resolvedFlightNumberMatcher = Pattern.compile("([a-zA-Z]+)(\\d+)").matcher(resolvedFlightNumber);
            String actualAirlineCode = resolvedFlightNumberMatcher.matches() ? resolvedFlightNumberMatcher.group(1) : airlineCode;
            String actualFlightNumber = resolvedFlightNumberMatcher.matches() ? resolvedFlightNumberMatcher.group(2) : flightNumber;

            String httpGetUrl = "https://www.flightradar24.com/data/flights/" + actualAirlineCode + actualFlightNumber;
            log.debug("Querying flightradar24 for flight {}{} on {} using URL: {}", actualAirlineCode, actualFlightNumber, departureDate, httpGetUrl);

            HttpGet httpRequest = new HttpGet(httpGetUrl);
            httpRequest.addHeader("User-Agent", "curl");
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpRequest)) {
                 String httpResponseContent = EntityUtils.toString(httpResponse.getEntity());
                 Document htmlDocument = Jsoup.parse(httpResponseContent);
                 Element dataTableElement = htmlDocument.selectFirst("table#tbl-datatable");
                 if (dataTableElement != null) {
                     return this.createDataFromDataTable(dataTableElement, actualAirlineCode, actualFlightNumber, departureDate, departureAirportCode);
                 }
            }

        } catch (Exception e) {
            log.debug("Cannot retreive data from flightradar24 for flight {}{} on {}", airlineCode, flightNumber, departureDate, e);
        }
        return null;
    }

    private String resolveActualFlightNumber(String airlineCode, String flightNumber, LocalDate departureDate, CloseableHttpClient httpClient) {
        try {
            String httpUrl = "https://www.flightradar24.com/v1/search/web/find?query=" + airlineCode + flightNumber;
            log.debug("Querying flightradar24 for potential codeshare information of flight {}{} on {} using URL: {}", airlineCode, flightNumber, departureDate, httpUrl);
            try (CloseableHttpResponse httpResponse = httpClient.execute(new HttpGet(httpUrl))) {
                String httpResponseContent = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonResponse = (JSONObject)new JSONParser(JSONParser.MODE_PERMISSIVE).parse(httpResponseContent);
                JSONArray jsonResultsArray = (JSONArray)jsonResponse.get("results");
                JSONObject jsonResult = (JSONObject)jsonResultsArray.get(0);
                if ("codeshare".equalsIgnoreCase(jsonResult.getAsString("match"))) {
                    JSONObject jsonDetailObject = (JSONObject)jsonResult.get("detail");
                    String actualFlightNumber = jsonDetailObject.getAsString("flight");
                    if (StringUtils.isNotEmpty(actualFlightNumber)) {
                        return actualFlightNumber;
                    }
                }
            }
        } catch (Exception e) {
            // Ignore any error here
        }
        return airlineCode + flightNumber;
    }

    private List<WizardData> createDataFromDataTable(Element dataTableElement, String airlineCode, String flightNumber, LocalDate departureDate, String departureAirportCode) {
        return dataTableElement.select("tbody tr").stream()
            .map(row -> this.createDataRowFromDataTableRow(row))
            .filter(dataRow -> departureDate != null && dataRow.matchesDepartureLocalDate(departureDate))
            .filter(dataRow -> StringUtils.isEmpty(departureAirportCode) || dataRow.matchesDepartureAirportCode(departureAirportCode))
            .map(dataRow -> dataRow.toWizardData(airlineCode, flightNumber))
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

    Clock getClock() {
        return this.clock;
    }
    void setClock(Clock clock) {
        this.clock = clock;
    }

}
