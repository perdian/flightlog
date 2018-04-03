package de.perdian.flightlog.modules.importexport.data.impl;

import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.perdian.flightlog.modules.importexport.data.DataItem;
import de.perdian.flightlog.modules.importexport.data.DataLoader;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;
import us.codecraft.xsoup.Xsoup;

public class FlugstatistikdeDataLoader implements DataLoader<FlugstatistikdeCredentials> {

    @Override
    public List<DataItem> loadDataItems(FlugstatistikdeCredentials credentials) throws Exception {

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setDefaultCookieStore(new BasicCookieStore());

        try (CloseableHttpClient httpClient = httpClientBuilder.build()) {

            Executor executor = Executor.newInstance(httpClient);

            Request loginRequest = Request.Post("https://www.flugstatistik.de/login/")
                .bodyForm(
                    Form.form()
                        .add("username", credentials.getUsername())
                        .add("passwort", credentials.getPassword())
                        .add("go", "login")
                        .build()
                );
            executor.execute(loginRequest).discardContent();

            boolean hasMoreFlights = true;
            List<Long> flightIdentifiers = new ArrayList<>();
            for (int dbPos = 0; hasMoreFlights; dbPos += 50) {

                Request overviewRequest = Request.Get("https://www.flugstatistik.de/login/?go=flugdaten&order=datum&dbpos=" + dbPos);
                String overviewResponseString = executor.execute(overviewRequest).returnContent().asString(Charset.forName("ISO-8859-1"));
                Document overviewResponseDocument = Jsoup.parse(overviewResponseString);
                Elements flightListTableRows = Xsoup.compile("//table[2]/tbody/tr").evaluate(overviewResponseDocument).getElements();
                int flightListTableRowsCount = flightListTableRows.size();

                for (int i=1; i < flightListTableRowsCount - 1; i++) {
                    Element flightListTableRow = flightListTableRows.get(i);
                    Element selectElement = flightListTableRow.getElementsByTag("select").get(0);
                    String selectElementString = selectElement.toString();
                    int idStartIndex = selectElementString.indexOf("id=");
                    int idEndIndex = selectElementString.indexOf("&", idStartIndex + "id=".length());
                    String idValue = selectElementString.substring(idStartIndex + "id=".length(), idEndIndex);
                    flightIdentifiers.add(Long.valueOf(idValue));
                }

                hasMoreFlights = flightListTableRowsCount >= 52;

            }

            return this.loadFlightsForIdentifiers(flightIdentifiers, executor);

        }

    }

    private List<DataItem> loadFlightsForIdentifiers(List<Long> flightIdentifiers, Executor executor) throws Exception {
        if (flightIdentifiers.isEmpty()) {
            return Collections.emptyList();
        } else {

            Map<Integer, DataItem> resultMap = new ConcurrentHashMap<>();

            List<Exception> suppressedExceptions = new LinkedList<>();
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (int i=flightIdentifiers.size() - 1; i >= 0; i--) {
                Integer flightIndex = flightIdentifiers.size() - (i + 1);
                Long flightIdentifier = flightIdentifiers.get(i);
                executorService.submit(() -> {
                    try {
                        resultMap.put(flightIndex, this.loadDataItemsForIdentifier(flightIdentifier, executor));
                    } catch (Exception e) {
                        suppressedExceptions.add(e);
                    }
                });
            }

            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            if (!suppressedExceptions.isEmpty()) {
                Exception executorException = new Exception("Could not load all flights");
                suppressedExceptions.forEach(executorException::addSuppressed);
                throw executorException;
            } else {
                return resultMap.entrySet().stream()
                    .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
            }

        }
    }

    private DataItem loadDataItemsForIdentifier(Long id, Executor executor) throws Exception {
        DataItem dataItem = new DataItem();

        String editFormUrl = "https://www.flugstatistik.de/login/?go=flugdaten_edit&id=" + id;
        Request editFormRequest = Request.Get(editFormUrl);
        String editFormResponse = executor.execute(editFormRequest).returnContent().asString(Charset.forName("ISO-8859-1"));
        Document editFormDocument = Jsoup.parse(editFormResponse);

        String departureTime = Xsoup.compile("//form/table/tbody/tr[6]/td/input").evaluate(editFormDocument).getElements().get(0).attr("value");
        dataItem.setDepartureAirportCode(Xsoup.compile("//form/table/tbody/tr[4]/td[2]//b").evaluate(editFormDocument).getElements().get(0).text());
        dataItem.setDepartureDateLocal(LocalDate.parse(Xsoup.compile("//form/table/tbody/tr[5]/td/input").evaluate(editFormDocument).getElements().get(0).attr("value"), FlugstatistikdeConstants.DATE_FORMATTER));
        dataItem.setDepartureTimeLocal(departureTime.isEmpty() ? null : LocalTime.parse(departureTime, FlugstatistikdeConstants.TIME_FORMATTER));

        String arrivalTime = Xsoup.compile("//form/table/tbody/tr[16]/td/input").evaluate(editFormDocument).getElements().get(0).attr("value");
        dataItem.setArrivalAirportCode(Xsoup.compile("//form/table/tbody/tr[14]/td[2]//b").evaluate(editFormDocument).getElements().get(0).text());
        dataItem.setArrivalDateLocal(LocalDate.parse(Xsoup.compile("//form/table/tbody/tr[15]/td/input").evaluate(editFormDocument).getElements().get(0).attr("value"), FlugstatistikdeConstants.DATE_FORMATTER));
        dataItem.setArrivalTimeLocal(arrivalTime.isEmpty() ? null : LocalTime.parse(arrivalTime, FlugstatistikdeConstants.TIME_FORMATTER));

        String durationString = Xsoup.compile("//form/table[2]/tbody/tr[2]/td[2]/input").evaluate(editFormDocument).getElements().get(0).attr("value");
        String[] durationItems = durationString.isEmpty() ? null : durationString.split(":");
        Duration duration = durationItems == null ? null : Duration.ofHours(Long.parseLong(durationItems[0])) .plusMinutes(Long.parseLong(durationItems[1]));

        String distanceValue = Xsoup.compile("//form/table[2]/tbody/tr[3]/td[2]/input").evaluate(editFormDocument).getElements().get(0).attr("value");
        Integer distance = distanceValue.isEmpty() ? null : Integer.valueOf(distanceValue);

        String flightNumber = Xsoup.compile("//form/table[2]/tbody/tr[4]/td[2]/input").evaluate(editFormDocument).getElements().get(0).attr("value");
        if (!StringUtils.isEmpty(flightNumber)) {
            Matcher flightNumberMatcher = FlugstatistikdeConstants.FLIGHT_NUMBER_PATTERN.matcher(flightNumber);
            if (flightNumberMatcher.matches()) {
                dataItem.setAirlineCode(flightNumberMatcher.group(1).toUpperCase().trim());
                dataItem.setFlightNumber(flightNumberMatcher.group(2).trim());
            } else {
                dataItem.setFlightNumber(flightNumber);
            }
        }

        dataItem.setAirlineName(Xsoup.compile("//form/table[2]/tbody/tr[5]/td[2]/input").evaluate(editFormDocument).getElements().get(0).attr("value"));

        dataItem.setAircraftType(Xsoup.compile("//form/table[2]/tbody/tr[7]/td[2]/input").evaluate(editFormDocument).getElements().get(0).attr("value"));
        dataItem.setAircraftRegistration(Xsoup.compile("//form/table[2]/tbody/tr[9]/td[2]/input").evaluate(editFormDocument).getElements().get(0).attr("value"));
        dataItem.setAircraftName(Xsoup.compile("//form/table[2]/tbody/tr[10]/td[2]/input").evaluate(editFormDocument).getElements().get(0).attr("value"));

        Elements seatTypeElements = Xsoup.compile("//form/table[2]/tbody/tr[11]/td[2]/input[@type='radio'][@checked]").evaluate(editFormDocument).getElements();
        Element seatTypeElement = seatTypeElements.isEmpty() ? null : seatTypeElements.get(0);
        dataItem.setSeatNumber(Xsoup.compile("//form/table[2]/tbody/tr[11]/td[2]/input").evaluate(editFormDocument).getElements().get(0).attr("value"));
        dataItem.setSeatType(seatTypeElement == null ? null : this.parseSeatType(seatTypeElement.attr("value")));

        Elements cabinClassElements = Xsoup.compile("//form/table[2]/tbody/tr[12]/td[2]/input[@type='radio'][@checked]").evaluate(editFormDocument).getElements();
        Element cabinClassElement = cabinClassElements.isEmpty() ? null : cabinClassElements.get(0);
        CabinClass cabinClass = cabinClassElement == null ? null : this.parseCabinClass(cabinClassElement.attr("value"));

        Elements flightTypeElements = Xsoup.compile("//form/table[2]/tbody/tr[14]/td[2]/input[@type='radio'][@checked]").evaluate(editFormDocument).getElements();
        Element flightTypeElement = flightTypeElements.isEmpty() ? null : flightTypeElements.get(0);
        FlightReason flightReason = flightTypeElement == null ? null : this.parseFlightReason(flightTypeElement.attr("value"));

        String comment = Xsoup.compile("//form/table[2]/tbody/tr[16]/td/textarea").evaluate(editFormDocument).getElements().get(0).text();

        dataItem.setFlightDuration(duration);
        dataItem.setFlightDistance(distance);
        dataItem.setCabinClass(cabinClass);
        dataItem.setFlightReason(flightReason);
        dataItem.setComment(comment);

        return dataItem;
    }

    private SeatType parseSeatType(String value) {
        if ("Fenster".equalsIgnoreCase(value)) {
            return SeatType.WINDOW;
        } else if ("Gang".equalsIgnoreCase(value)) {
            return SeatType.AISLE;
        } else if ("Mitte".equalsIgnoreCase(value)) {
            return SeatType.MIDDLE;
        } else {
            return null;
        }
    }

    private CabinClass parseCabinClass(String value) {
        if ("Economy".equalsIgnoreCase(value)) {
            return CabinClass.ECONOMY;
        } else if ("EconomyPlus".equalsIgnoreCase(value)) {
            return CabinClass.PREMIUM_ECONOMY;
        } else if ("Business".equalsIgnoreCase(value)) {
            return CabinClass.BUSINESS;
        } else if ("First".equalsIgnoreCase(value)) {
            return CabinClass.FIRST;
        } else {
            return null;
        }
    }

    private FlightReason parseFlightReason(String value) {
        if ("privat".equalsIgnoreCase(value)) {
            return FlightReason.PRIVATE;
        } else if ("geschäftlich".equalsIgnoreCase(value)) {
            return FlightReason.BUSINESS;
        } else if ("virtuell".equalsIgnoreCase(value)) {
            return FlightReason.VIRTUAL;
        } else {
            return null;
        }
    }

}
