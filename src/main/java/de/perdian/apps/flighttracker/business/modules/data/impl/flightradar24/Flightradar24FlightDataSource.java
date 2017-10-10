package de.perdian.apps.flighttracker.business.modules.data.impl.flightradar24;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import de.perdian.apps.flighttracker.business.modules.data.FlightData;
import de.perdian.apps.flighttracker.business.modules.data.FlightDataSource;
import de.perdian.apps.flighttracker.persistence.entities.AircraftTypeEntity;
import de.perdian.apps.flighttracker.persistence.entities.AirportEntity;
import de.perdian.apps.flighttracker.persistence.repositories.AircraftTypesRepository;
import de.perdian.apps.flighttracker.persistence.repositories.AirportsRepository;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;
import us.codecraft.xsoup.Xsoup;

@Component
public class Flightradar24FlightDataSource implements FlightDataSource {

    private static final Logger log = LoggerFactory.getLogger(Flightradar24FlightDataSource.class);

    private AircraftTypesRepository aircraftTypesRepository = null;
    private AirportsRepository airportsRepository = null;
    private Clock clock = Clock.systemUTC();

    public static void main(String[] args) throws Exception {

        Constructor<?> aircraftTypesRepositoryConstructor = Class.forName("de.perdian.apps.flighttracker.persistence.repositories.AircraftTypesRepositoryImpl").getDeclaredConstructors()[0];
        aircraftTypesRepositoryConstructor.setAccessible(true);
        AircraftTypesRepository aircraftTypesRepository = (AircraftTypesRepository)aircraftTypesRepositoryConstructor.newInstance();
        Method asetResourceLoaderMethod = aircraftTypesRepository.getClass().getDeclaredMethod("setResourceLoader", ResourceLoader.class);
        asetResourceLoaderMethod.setAccessible(true);
        asetResourceLoaderMethod.invoke(aircraftTypesRepository, new DefaultResourceLoader());
        Method ainitMethod = aircraftTypesRepository.getClass().getDeclaredMethod("initialize");
        ainitMethod.setAccessible(true);
        ainitMethod.invoke(aircraftTypesRepository);

        Constructor<?> airportsRepositoryConstructor = Class.forName("de.perdian.apps.flighttracker.persistence.repositories.AirportsRepositoryImpl").getDeclaredConstructors()[0];
        airportsRepositoryConstructor.setAccessible(true);
        AirportsRepository airportsRepository = (AirportsRepository)airportsRepositoryConstructor.newInstance();
        Method bsetResourceLoaderMethod = airportsRepository.getClass().getDeclaredMethod("setResourceLoader", ResourceLoader.class);
        bsetResourceLoaderMethod.setAccessible(true);
        bsetResourceLoaderMethod.invoke(airportsRepository, new DefaultResourceLoader());
        Method binitMethod = airportsRepository.getClass().getDeclaredMethod("initialize");
        binitMethod.setAccessible(true);
        binitMethod.invoke(airportsRepository);

        Flightradar24FlightDataSource dataSource = new Flightradar24FlightDataSource();
        dataSource.setAircraftTypesRepository(aircraftTypesRepository);
        dataSource.setAirportsRepository(airportsRepository);

        System.err.println(dataSource.lookupFlightData("AY", "69", LocalDate.now().minusDays(2)));

    }

    @Override
    public FlightData lookupFlightData(String airlineCode, String flightNumber, LocalDate departureDate) {

        // TODO: Check if we are enabled

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("https://www.flightradar24.com/data/flights/" + airlineCode + flightNumber);
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                 String httpResponseContent = EntityUtils.toString(httpResponse.getEntity());
                 Document htmlDocument = Jsoup.parse(httpResponseContent);
                 Elements dataTableElements = Xsoup.select(htmlDocument, "//table[@id='tbl-datatable']").getElements();
                 if (dataTableElements.size() > 0) {
                     return this.lookupFlightDataFromDataTable(dataTableElements.get(0), airlineCode, flightNumber, departureDate);
                 }
            }
        } catch (Exception e) {
            log.warn("Cannot retreive data from flightradar24 for flight {}{} on {}", airlineCode, flightNumber, departureDate, e);
        }
        return null;
    }

    private FlightData lookupFlightDataFromDataTable(Element dataTableElement, String airlineCode, String flightNumber, LocalDate departureDate) {
        LocalDate currentDate = LocalDate.now(this.getClock());
        Elements trElements = dataTableElement.getElementsByTag("tr");
        Element trElementForSpecificDate = currentDate.isBefore(departureDate) ? null : this.lookupDataTableRowForDate(trElements, departureDate);
        if (trElementForSpecificDate != null) {
            return this.lookupFlightDataFromDataTableRow(trElementForSpecificDate, true, airlineCode, flightNumber, departureDate);
        } else {
            Element trElementForCurrentDate = this.lookupDataTableRowForDate(trElements, LocalDate.now().minusDays(1));
            if (trElementForCurrentDate != null) {
                return this.lookupFlightDataFromDataTableRow(trElementForSpecificDate, false, airlineCode, flightNumber, departureDate);
            }
        }
        return null;
    }

    private Element lookupDataTableRowForDate(Elements trElements, LocalDate targetDate) {
        String targetDateString = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(targetDate);
        for (int i=0; i < trElements.size(); i++) {
            Element trElement = trElements.get(i);
            Elements tdElements = trElement.getElementsByTag("td");
            Element dateElement = tdElements.size() <= 1 ? null : tdElements.get(1);
            String dateValue = dateElement == null ? null : dateElement.text();
            if (dateValue != null && dateValue.equalsIgnoreCase(targetDateString)) {
                return trElement;
            }
        }
        return null;
    }

    private FlightData lookupFlightDataFromDataTableRow(Element trElement, boolean fullData, String airlineCode, String flightNumber, LocalDate departureDate) {

        System.err.println(trElement);
        System.err.println("----");

        FlightData flightData = new FlightData();
        flightData.setDepartureDateLocal(departureDate);

        Elements tdElements = trElement.getElementsByTag("td");
        Element aircraftElement = tdElements.size() <= 4 ? null : tdElements.get(4);
        Elements aircraftTypeElement = aircraftElement == null ? null : aircraftElement.getElementsByTag("span");
        if (aircraftTypeElement != null && !aircraftTypeElement.isEmpty()) {
            AircraftTypeEntity aircraftTypeEntity = this.getAircraftTypesRepository().loadAircraftTypeByCode(aircraftTypeElement.text());
            if (aircraftTypeEntity != null && !StringUtils.isEmpty(aircraftTypeEntity.getTitle())) {
                flightData.setAircraftType(aircraftTypeEntity.getTitle());
            } else {
                flightData.setAircraftType(aircraftTypeElement.text());
            }
        }
        Elements aircraftRegistryElement = aircraftElement == null ? null : aircraftElement.getElementsByTag("a");
        if (aircraftRegistryElement != null && !aircraftRegistryElement.isEmpty()) {
            flightData.setAircraftRegistration(aircraftRegistryElement.text().trim());
        }

        Element departureAirportElement = tdElements.size() <= 2 ? null : tdElements.get(2);
        Elements departureAirportCodeElements = departureAirportElement == null ? null : departureAirportElement.getElementsByTag("a");
        Element departureAirportCodeElement = departureAirportCodeElements == null || departureAirportCodeElements.isEmpty() ? null : departureAirportCodeElements.get(0);
        String departureAirportCode = departureAirportCodeElement == null ? null : departureAirportCodeElement.text().trim();
        flightData.setDepartureAirportCode(departureAirportCode);

        Element arrivalAirportElement = tdElements.size() <= 3 ? null : tdElements.get(3);
        Elements arrivalAirportCodeElements = arrivalAirportElement == null ? null : arrivalAirportElement.getElementsByTag("a");
        Element arrivalAirportCodeElement = arrivalAirportCodeElements == null || arrivalAirportCodeElements.isEmpty() ? null : arrivalAirportCodeElements.get(0);
        String arrivalAirportCode = arrivalAirportCodeElement == null ? null : arrivalAirportCodeElement.text().trim();
        flightData.setArrivalAirportCode(arrivalAirportCode);

        AirportEntity departureAirportEntity = this.getAirportsRepository().loadAirportByIataCode(departureAirportCode);
        AirportEntity arrivalAirportEntity = this.getAirportsRepository().loadAirportByIataCode(arrivalAirportCode);
        if (departureAirportEntity != null && arrivalAirportEntity != null) {

            Element actualDepartureTimeElement = tdElements.size() <= 7 ? null : tdElements.get(7);
            String actualDepartureTimeValue = actualDepartureTimeElement == null ? null : actualDepartureTimeElement.text().trim();
            if (!StringUtils.isEmpty(actualDepartureTimeValue) && !"-".equalsIgnoreCase(actualDepartureTimeValue)) {

                LocalTime actualDepartureTimeLocal = LocalTime.parse(actualDepartureTimeValue);
                flightData.setDepartureTimeLocal(actualDepartureTimeLocal);

                if (departureAirportEntity.getTimezoneId() != null && arrivalAirportEntity.getTimezoneId() != null) {

                    Element flightDurationElement = tdElements.size() <= 5 ? null : tdElements.get(5);
                    String flightDurationValue = flightDurationElement == null ? null : flightDurationElement.text().trim();
                    Duration flightDuration = FlighttrackerHelper.parseDuration(flightDurationValue);

                    ZonedDateTime actualDepartureTimeZoned = actualDepartureTimeLocal.atDate(departureDate).atZone(departureAirportEntity.getTimezoneId());
                    ZonedDateTime actualArrivalTimeZonedAtDeparture = actualDepartureTimeZoned.plus(flightDuration);
                    ZonedDateTime actualArrivalTimeZonedAtArrival = actualArrivalTimeZonedAtDeparture.withZoneSameInstant(arrivalAirportEntity.getTimezoneId());
                    flightData.setArrivalDateLocal(actualArrivalTimeZonedAtArrival.toLocalDate());
                    flightData.setArrivalTimeLocal(actualArrivalTimeZonedAtArrival.toLocalTime());

                }

            }

            System.err.println(actualDepartureTimeValue);

        }

        return flightData;

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
    @Autowired
    void setClock(Clock clock) {
        this.clock = clock;
    }

}
