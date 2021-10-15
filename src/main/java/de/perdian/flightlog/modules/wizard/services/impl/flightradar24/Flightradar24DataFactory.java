package de.perdian.flightlog.modules.wizard.services.impl.flightradar24;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        LocalDate currentDate = LocalDate.now(this.getClock());
        Elements trElements = dataTableElement.getElementsByTag("tr");
        List<Element> trElementsForSpecificDate = currentDate.isBefore(departureDate) ? null : this.lookupTableRowsForDate(trElements, departureDate, departureAirportCode);
        if (trElementsForSpecificDate != null && !trElementsForSpecificDate.isEmpty()) {
            return trElementsForSpecificDate.stream()
                .map(tableRow -> this.createDataFromTableRow(tableRow, airlineCode, flightNumber, departureDate))
                .sorted(WizardData::sortByDepartureDateAndTime)
                .collect(Collectors.toList());
        } else {
            List<Element> trElementsForCurrentDate = this.lookupTableRowsForDate(trElements, LocalDate.now().minusDays(1), departureAirportCode);
            if (trElementsForCurrentDate.size() == 1) {
                WizardData currentDateFlightData = this.createDataFromTableRow(trElementsForCurrentDate.get(0), airlineCode, flightNumber, departureDate);
                if (currentDateFlightData != null) {

                    // As the departure and arrival times as well as the aircraft registry is not actually the
                    // right one for the departure date passed as parameter to the method but instead the values
                    // from yesterday, we'll only use the values which we assume are the same (which currently
                    // are the airport codes plus the originally queried departure date)
                    WizardData resultFlightData = new WizardData();
                    resultFlightData.setAirlineCode(currentDateFlightData.getAirlineCode());
                    resultFlightData.setFlightNumber(currentDateFlightData.getFlightNumber());
                    resultFlightData.setArrivalAirportCode(currentDateFlightData.getArrivalAirportCode());
                    resultFlightData.setDepartureAirportCode(currentDateFlightData.getDepartureAirportCode());
                    resultFlightData.setDepartureDateLocal(departureDate);
                    return Collections.singletonList(resultFlightData);

                }
            }
        }
        return null;
    }

    private List<Element> lookupTableRowsForDate(Elements trElements, LocalDate targetDate, String targetDepartureAirport) {
        List<Element> tableRowsForDate = new ArrayList<>();
        for (int i=0; i < trElements.size(); i++) {

            Element trElement = trElements.get(i);
            Elements tdElements = trElement.getElementsByTag("td");

            Element dateElement = tdElements.size() <= 2 ? null : tdElements.get(2);
            String dateString = dateElement == null ? null : dateElement.text().trim();
            LocalDate date = StringUtils.isEmpty(dateString) ? null : LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd MMM yyyy").withLocale(Locale.ENGLISH));
            boolean dateMatches = date != null && date.equals(targetDate);

            Element departureAirportElement = tdElements.size() <= 2 ? null : tdElements.get(2);
            List<Element> departureAirportCodeElements = departureAirportElement == null ? null : departureAirportElement.getElementsByTag("a");
            String departureAirportValue = departureAirportCodeElements == null || departureAirportCodeElements.isEmpty() ? null : departureAirportCodeElements.get(0).text();
            boolean departureAirportMatches = StringUtils.isBlank(targetDepartureAirport) || targetDepartureAirport.equalsIgnoreCase(departureAirportValue);

            Element statusElement = tdElements.size() <= 11 ? null : tdElements.get(11);
            String status = statusElement == null ? null : statusElement.text().trim();
            boolean statusMatches = !"Scheduled".equalsIgnoreCase(status) && !"Unknown".equalsIgnoreCase(status);

            if (dateMatches && departureAirportMatches && statusMatches) {
                tableRowsForDate.add(trElement);
            }

        }
        return tableRowsForDate;
    }

    private WizardData createDataFromTableRow(Element trElement, String airlineCode, String flightNumber, LocalDate departureDate) {

        WizardData wizardData = new WizardData();
        wizardData.setDepartureDateLocal(departureDate);
        wizardData.setAirlineCode(airlineCode);
        wizardData.setFlightNumber(flightNumber);

        Elements tdElements = trElement.getElementsByTag("td");
        Element aircraftElement = tdElements.size() <= 5 ? null : tdElements.get(5);
        String aircraftType = aircraftElement == null ? null : aircraftElement.text().trim();
        int aircraftCodeEndIndex = aircraftType == null ? null : aircraftType.indexOf(" ");
        String aircraftTypeCode = aircraftType == null ? null : (aircraftCodeEndIndex < 0 ? aircraftType : aircraftType.substring(0, aircraftCodeEndIndex));
        if (StringUtils.isNotEmpty(aircraftTypeCode)) {
            AircraftTypeEntity aircraftTypeEntity = this.getAircraftTypesRepository().loadAircraftTypeByCode(aircraftTypeCode);
            if (aircraftTypeEntity != null && !StringUtils.isEmpty(aircraftTypeEntity.getTitle())) {
                wizardData.setAircraftType(aircraftTypeEntity.getTitle());
            } else {
                wizardData.setAircraftType(aircraftTypeCode);
            }
        }
        Elements aircraftRegistryElement = aircraftElement == null ? null : aircraftElement.getElementsByTag("a");
        if (aircraftRegistryElement != null && !aircraftRegistryElement.isEmpty()) {
            wizardData.setAircraftRegistration(extractValue(aircraftRegistryElement.text().trim()));
        }

        Element departureAirportElement = tdElements.size() <= 3 ? null : tdElements.get(3);
        Elements departureAirportCodeElements = departureAirportElement == null ? null : departureAirportElement.getElementsByTag("a");
        Element departureAirportCodeElement = departureAirportCodeElements == null || departureAirportCodeElements.isEmpty() ? null : departureAirportCodeElements.get(0);
        String departureAirportCode = departureAirportCodeElement == null ? null : this.extractValue(departureAirportCodeElement.text().trim());
        wizardData.setDepartureAirportCode(departureAirportCode);

        Element arrivalAirportElement = tdElements.size() <= 4 ? null : tdElements.get(4);
        Elements arrivalAirportCodeElements = arrivalAirportElement == null ? null : arrivalAirportElement.getElementsByTag("a");
        Element arrivalAirportCodeElement = arrivalAirportCodeElements == null || arrivalAirportCodeElements.isEmpty() ? null : arrivalAirportCodeElements.get(0);
        String arrivalAirportCode = arrivalAirportCodeElement == null ? null : this.extractValue(arrivalAirportCodeElement.text().trim());
        wizardData.setArrivalAirportCode(arrivalAirportCode);

        AirportEntity departureAirportEntity = this.getAirportsRepository().loadAirportByIataCode(departureAirportCode);
        AirportEntity arrivalAirportEntity = this.getAirportsRepository().loadAirportByIataCode(arrivalAirportCode);
        if (departureAirportEntity != null && arrivalAirportEntity != null) {

            Element actualDepartureTimeElement = tdElements.size() <= 8 ? null : tdElements.get(8);
            String actualDepartureTimeValue = actualDepartureTimeElement == null ? null : actualDepartureTimeElement.text().trim();
            if (!StringUtils.isEmpty(actualDepartureTimeValue) && !"-".equalsIgnoreCase(actualDepartureTimeValue) && !"—".equalsIgnoreCase(actualDepartureTimeValue)) {
                if (departureAirportEntity.getTimezoneId() != null && arrivalAirportEntity.getTimezoneId() != null) {

                    ZonedDateTime actualDeparturetimeUtc = LocalTime.parse(actualDepartureTimeValue).atDate(departureDate).atZone(ZoneId.of("UTC"));
                    LocalTime actualDepartureTimeLocal = actualDeparturetimeUtc.withZoneSameInstant(departureAirportEntity.getTimezoneId()).toLocalTime();
                    wizardData.setDepartureTimeLocal(actualDepartureTimeLocal);

                    Element flightDurationElement = tdElements.size() <= 6 ? null : tdElements.get(6);
                    String flightDurationValue = flightDurationElement == null ? null : flightDurationElement.text().trim();
                    Duration flightDuration = FlightlogHelper.parseDuration(flightDurationValue);
                    if (flightDuration != null) {
                        ZonedDateTime actualDepartureTimeZoned = actualDepartureTimeLocal.atDate(departureDate).atZone(departureAirportEntity.getTimezoneId());
                        ZonedDateTime actualArrivalTimeZonedAtDeparture = actualDepartureTimeZoned.plus(flightDuration);
                        ZonedDateTime actualArrivalTimeZonedAtArrival = actualArrivalTimeZonedAtDeparture.withZoneSameInstant(arrivalAirportEntity.getTimezoneId());
                        wizardData.setArrivalDateLocal(actualArrivalTimeZonedAtArrival.toLocalDate());
                        wizardData.setArrivalTimeLocal(actualArrivalTimeZonedAtArrival.toLocalTime());
                    }

                }
            }

        }

        return wizardData;

    }

    private String extractValue(String input) {
        if (StringUtils.isEmpty(input)) {
            return null;
        } else {
            int startIndex = input.startsWith("(") ? 1 : 0;
            int endIndex = input.endsWith(")") ? input.length() - 1 : input.length();
            return input.substring(startIndex, endIndex);
        }
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
