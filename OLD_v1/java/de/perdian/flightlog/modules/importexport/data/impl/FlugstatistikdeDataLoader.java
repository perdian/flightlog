package de.perdian.flightlog.modules.importexport.data.impl;

import java.io.IOException;
import java.net.CookieManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.perdian.flightlog.modules.importexport.data.DataItem;
import de.perdian.flightlog.modules.importexport.data.DataLoader;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;
import okhttp3.FormBody;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FlugstatistikdeDataLoader implements DataLoader<FlugstatistikdeCredentials> {

    private static final Logger log = LoggerFactory.getLogger(FlugstatistikdeDataLoader.class);

    @Override
    public List<DataItem> loadDataItems(FlugstatistikdeCredentials credentials) throws Exception {
        OkHttpClient httpClient = this.createLoggedInHttpClient(credentials);
        List<Long> flugstatistikIdentifiers = this.loadFlugstatistikIdentifiers(httpClient);
        return flugstatistikIdentifiers.isEmpty() ? Collections.emptyList() : this.loadDataItemsForFlugstatistikIdentifiers(flugstatistikIdentifiers, httpClient);
    }

    private OkHttpClient createLoggedInHttpClient(FlugstatistikdeCredentials credentials) throws IOException {

        OkHttpClient httpClient = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(new CookieManager())).build();

        FormBody loginRequestBody = new FormBody.Builder()
            .add("username", credentials.getUsername())
            .add("passwort", credentials.getPassword())
            .add("go", "login")
            .build();
        Request loginRequest = new Request.Builder().post(loginRequestBody).url("https://www.flugstatistik.de/login/") .build();

        log.debug("Performing login into flugstatistik.de");
        try (Response loginResponse = httpClient.newCall(loginRequest).execute()) {
            if (loginResponse.code() != 200) {
                throw new IllegalArgumentException("Login failed at flugstatistik.de with HTTP response code: " + loginResponse.code());
            }
        }

        return httpClient;

    }

    private List<Long> loadFlugstatistikIdentifiers(OkHttpClient httpClient) throws IOException {
        boolean hasMoreFlights = true;
        List<Long> flightIdentifiers = new ArrayList<>();
        for (int dbPos = 0; hasMoreFlights; dbPos += 50) {
            Request overviewRequest = new Request.Builder().get().url("https://www.flugstatistik.de/login/?go=flugdaten&order=datum&dbpos=" + dbPos).build();
            try (Response overviewResponseResponse = httpClient.newCall(overviewRequest).execute()) {
                String overviewResponseString = overviewResponseResponse.body().string();
                Document overviewResponseDocument = Jsoup.parse(overviewResponseString);
                Elements flightListTableRows = overviewResponseDocument.select("table").get(2).select("tr");
                int flightListTableRowsCount = flightListTableRows.size();
                for (int i=1; i < flightListTableRowsCount - 1; i++) {
                    Element flightListTableRow = flightListTableRows.get(i);
                    Element selectElement = flightListTableRow.selectFirst("select");
                    if (selectElement != null) {
                        String selectElementString = selectElement.toString();
                        int idStartIndex = selectElementString.indexOf("id=");
                        int idEndIndex = selectElementString.indexOf("&", idStartIndex + "id=".length());
                        String idValue = selectElementString.substring(idStartIndex + "id=".length(), idEndIndex);
                        flightIdentifiers.add(Long.valueOf(idValue));
                    }
                }
                hasMoreFlights = flightListTableRowsCount >= 52;
            }
        }
        return flightIdentifiers;
    }

    private List<DataItem> loadDataItemsForFlugstatistikIdentifiers(List<Long> flightIdentifiers, OkHttpClient httpClient) throws Exception {

        log.debug("Loading details for {} flugstatistik.de items", flightIdentifiers.size());
        Map<Integer, Future<DataItem>> resultMap = new ConcurrentHashMap<>();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i=flightIdentifiers.size() - 1; i >= 0; i--) {
            Integer flightIndex = flightIdentifiers.size() - (i + 1);
            Long flightIdentifier = flightIdentifiers.get(i);
            resultMap.put(flightIndex, executorService.submit(() -> this.loadDataItemsForFlugstatusIdentifier(flightIdentifier, httpClient)));
        }
        executorService.shutdown();

        List<Throwable> suppressedExceptions = new LinkedList<>();
        List<DataItem> resultList = new ArrayList<>(resultMap.size());
        for (Map.Entry<Integer, Future<DataItem>> resultEntry : resultMap.entrySet()) {
            try {
                resultList.add(resultEntry.getValue().get());
            } catch (ExecutionException e) {
                suppressedExceptions.add(e.getCause());
            }
        }

        if (!suppressedExceptions.isEmpty()) {
            Exception executorException = new Exception("Could not load all flights");
            for (Throwable suppressedException : suppressedExceptions) {
                executorException.addSuppressed(suppressedException);
            }
            throw executorException;
        } else {
            return resultList;
        }
    }

    private DataItem loadDataItemsForFlugstatusIdentifier(Long id, OkHttpClient httpClient) throws Exception {
        String editFormUrl =  "https://www.flugstatistik.de/login/?go=flugdaten_edit&id=" + id;
        Request editFormRequest = new Request.Builder().get().url(editFormUrl).build();
        try (Response editFormResponse = httpClient.newCall(editFormRequest).execute()) {
            String editFormString = editFormResponse.body().string();
            Document editFormDocument = Jsoup.parse(editFormString);
            DataItem dataItem = new DataItem();

            Elements tableRows = editFormDocument.select("form table tr");
            String departureAirportCode = tableRows.get(3).selectFirst("input[name='ab_3letter']").val();
            String departureDate = tableRows.get(4).selectFirst("td input").attr("value");
            String departureTime = tableRows.get(5).selectFirst("td input").attr("value");
            dataItem.setDepartureAirportCode(departureAirportCode);
            dataItem.setDepartureDateLocal(LocalDate.parse(departureDate, FlugstatistikdeConstants.DATE_FORMATTER));
            dataItem.setDepartureTimeLocal(departureTime.isEmpty() ? null : LocalTime.parse(departureTime, FlugstatistikdeConstants.TIME_FORMATTER));

            String arrivalAirportCode = tableRows.get(13).selectFirst("input[name='an_3letter']").val();
            String arrivalDate = tableRows.get(14).selectFirst("td input").attr("value");
            String arrivaleTime = tableRows.get(15).selectFirst("td input").attr("value");
            dataItem.setArrivalAirportCode(arrivalAirportCode);
            dataItem.setArrivalDateLocal(LocalDate.parse(arrivalDate, FlugstatistikdeConstants.DATE_FORMATTER));
            dataItem.setArrivalTimeLocal(arrivaleTime.isEmpty() ? null : LocalTime.parse(arrivaleTime, FlugstatistikdeConstants.TIME_FORMATTER));

            String durationString = tableRows.get(23).selectFirst("input").val();
            String[] durationItems = durationString.isEmpty() ? null : durationString.split(":");
            Duration duration = durationItems == null ? null : Duration.ofHours(Long.parseLong(durationItems[0])) .plusMinutes(Long.parseLong(durationItems[1]));
            dataItem.setFlightDuration(duration);

            String distanceValue = tableRows.get(24).selectFirst("input").val();
            Integer distance = distanceValue.isEmpty() ? null : Integer.valueOf(distanceValue);
            dataItem.setFlightDistance(distance);

            String flightNumber = tableRows.get(25).selectFirst("input").val();
            if (!StringUtils.isEmpty(flightNumber)) {
                Matcher flightNumberMatcher = FlugstatistikdeConstants.FLIGHT_NUMBER_PATTERN.matcher(flightNumber);
                if (flightNumberMatcher.matches()) {
                    dataItem.setAirlineCode(flightNumberMatcher.group(1).toUpperCase().trim());
                    dataItem.setFlightNumber(flightNumberMatcher.group(2).trim());
                } else {
                    dataItem.setFlightNumber(flightNumber);
                }
            }

            dataItem.setAirlineName(tableRows.get(26).selectFirst("input").val());
            dataItem.setAircraftType(tableRows.get(28).selectFirst("input").val());
            dataItem.setAircraftRegistration(tableRows.get(30).selectFirst("input").val());
            dataItem.setAircraftName(tableRows.get(31).selectFirst("input").val());

            Element seatTypeElement = tableRows.get(32).selectFirst("input[checked]");
            dataItem.setSeatType(seatTypeElement == null ? null : this.parseSeatType(seatTypeElement.attr("value")));
            dataItem.setSeatNumber(tableRows.get(32).selectFirst("input").val());

            Element cabinClassElement = tableRows.get(33).selectFirst("input[checked]");
            dataItem.setCabinClass(cabinClassElement == null ? null : this.parseCabinClass(cabinClassElement.attr("value")));

            Element flightReasonElement = tableRows.get(35).selectFirst("input[checked]");
            dataItem.setFlightReason(flightReasonElement == null ? null : this.parseFlightReason(flightReasonElement.attr("value")));

            dataItem.setComment(tableRows.get(37).selectFirst("textarea").val());
            return dataItem;
        }
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
