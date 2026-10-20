package de.perdian.flightlog.modules.flights.lookup.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.perdian.flightlog.modules.flights.lookup.FlightLookupRequest;
import de.perdian.flightlog.modules.flights.lookup.FlightLookupRequestSanitizer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class Flightradar24LookupRequestSanitizer implements FlightLookupRequestSanitizer {

    private static final Logger log = LoggerFactory.getLogger(Flightradar24LookupRequestSanitizer.class);

    private static final Pattern FLIGHT_NUMBER_PATTERN = Pattern.compile("([a-zA-Z0-9]{2})(\\d+)");

    private OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public FlightLookupRequest sanitize(FlightLookupRequest flightLookupRequest) {
        try {

            String completeFlightNumber = flightLookupRequest.getAirlineCode() + flightLookupRequest.getFlightNumber();
            String httpUrl = "https://www.flightradar24.com/v1/search/web/find?query=" + completeFlightNumber;
            log.debug("Querying flightradar24 for potential codeshare information of flight on {} using URL: {}", flightLookupRequest, httpUrl);
            Request httpRequest = new Request.Builder()
                .get().url(httpUrl)
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36 OPR/124.0.0.0 (Edition developer)")
                .build();

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
                        Matcher flightNumberMatcher = StringUtils.isEmpty(actualFlightNumber) ? null : FLIGHT_NUMBER_PATTERN.matcher(actualFlightNumber);
                        if (flightNumberMatcher != null && flightNumberMatcher.matches()) {
                            FlightLookupRequest newRequest = flightLookupRequest.clone();
                            newRequest.setAirlineCode(flightNumberMatcher.group(1));
                            newRequest.setFlightNumber(flightNumberMatcher.group(2));
                            return newRequest;
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.debug("Cannot evaluate flightradar24 codeshare result", e);
        }
        return flightLookupRequest;
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
