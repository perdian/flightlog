package de.perdian.apps.flighttracker.business.modules.data.impl.lufthansa;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.perdian.apps.flighttracker.business.modules.data.FlightData;
import de.perdian.apps.flighttracker.business.modules.data.FlightDataSource;

@Component
public class LufthansaFlightDataSource implements FlightDataSource {

    private static final Logger log = LoggerFactory.getLogger(LufthansaFlightDataSource.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<String> lufthansaAirlineCodes = Arrays.asList("LH");
    private LufthansaConfiguration configuration = null;
    private LufthansaAccessToken accessToken = null;
    private Clock clock = Clock.systemUTC();

    @Override
    public FlightData lookupFlightData(String airlineCode, String flightNumber, LocalDate departureDate) {
        if (!StringUtils.isEmpty(airlineCode) && this.getLufthansaAirlineCodes().contains(airlineCode.toUpperCase())) {

            // First we try to get the flight status for the specific departure date passed as parameter.
            // If we can't get any information using this we try the same request with the current date.
            FlightData specificFlightData = departureDate == null ? null : this.lookupFlightDataFromLufthansa(airlineCode, flightNumber, departureDate);
            if (specificFlightData != null) {
                return specificFlightData;
            } else {
                FlightData todaysFlightData = this.lookupFlightDataFromLufthansa(airlineCode, flightNumber, LocalDate.now(this.getClock()));
                if (todaysFlightData != null) {
                    FlightData responseFlightData = new FlightData();
                    responseFlightData.setArrivalAirportCode(responseFlightData.getArrivalAirportCode());
                    responseFlightData.setDepartureAirportCode(responseFlightData.getDepartureAirportCode());
                    return responseFlightData;
                }
            }

        }
        return null;
    }

    private FlightData lookupFlightDataFromLufthansa(String airlineCode, String flightNumber, LocalDate departureDate) {
        LufthansaAccessToken accessToken = this.lookupAccessToken();
        if (accessToken != null) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

                StringBuilder httpGetUrl = new StringBuilder();
                httpGetUrl.append("https://api.lufthansa.com/v1/operations/flightstatus/");
                httpGetUrl.append(airlineCode.toUpperCase()).append(flightNumber);
                httpGetUrl.append("/").append(departureDate.toString());

                HttpGet httpGet = new HttpGet(httpGetUrl.toString());
                httpGet.setHeader("Authorization", "Bearer " + accessToken.getValue());

                try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        JsonNode responseNode = this.getObjectMapper().readTree(httpResponse.getEntity().getContent());
                        JsonNode flightsNode = responseNode.get("FlightStatusResource").get("Flights");
                        if (flightsNode.size() == 1) {

                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

                            JsonNode flightNode = flightsNode.get(0);
                            JsonNode departureNode = flightNode.get("Departure");
                            JsonNode scheduledTimeNode =  departureNode.get("ActualTimeLocal");
                            LocalDateTime departureTime = LocalDateTime.parse(scheduledTimeNode.get("DateTime").asText(), dateTimeFormatter);

                            JsonNode actualTimeNode = departureNode.get("ActualTimeLocal");
                            if (actualTimeNode != null) {
                                String actualTimeValue = actualTimeNode
                            }

                            // {"FlightStatusResource":{"Flights":{"Flight":{"Departure":{"AirportCode":"FRA","ScheduledTimeLocal":{"DateTime":"2017-09-07T11:00"},"ScheduledTimeUTC":{"DateTime":"2017-09-07T09:00Z"},"ActualTimeLocal":{"DateTime":"2017-09-07T11:28"},"ActualTimeUTC":{"DateTime":"2017-09-07T09:28Z"},"TimeStatus":{"Code":"DL","Definition":"Flight Delayed"},"Terminal":{"Name":1,"Gate":"C14"}},"Arrival":{"AirportCode":"JFK","ScheduledTimeLocal":{"DateTime":"2017-09-07T13:30"},"ScheduledTimeUTC":{"DateTime":"2017-09-07T17:30Z"},"EstimatedTimeLocal":{"DateTime":"2017-09-07T14:05"},"EstimatedTimeUTC":{"DateTime":"2017-09-07T18:05Z"},"TimeStatus":{"Code":"DL","Definition":"Flight Delayed"},"Terminal":{"Name":1}},"MarketingCarrier":{"AirlineID":"LH","FlightNumber":400},"OperatingCarrier":{"AirlineID":"LH","FlightNumber":400},"Equipment":{"AircraftCode":744},"FlightStatus":{"Code":"DP","Definition":"Flight Departed"}}},"Meta":{"@Version":"1.0.0","Link":[{"@Href":"https://api.lufthansa.com/v1/operations/flightstatus/LH400/2017-09-07","@Rel":"self"},{"@Href":"https://api.lufthansa.com/v1/references/airports/{airportCode}","@Rel":"related"}]}}}

                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

                            FlightData flightData = new FlightData();
                            flightData.setDepartureAirportCode(flightNode.get("Departure").get("AirportCode").asText());
                            flightData.setDepartureDateLocal(departureTime.toLocalDate());
                            flightData.setDepartureTimeLocal(departureTime.toLocalTime());
                            return flightData;

                        }
                        System.err.println(responseNode.toString());
                    } else {
                        log.debug("Invalid response returned from Lufthansa for flight status for flight '{}{}': {}", airlineCode, flightNumber, httpResponse.getStatusLine());
                    }
                }

            } catch (Exception e) {
                log.debug("Cannot fetch flight information from Lufthansa for flight {}{}", airlineCode, flightNumber, e);
            }
        }
        return null;
    }

    private LocalDateTime extractDateTime() {

    }

    private LufthansaAccessToken lookupAccessToken() {
        LufthansaAccessToken existingAccessToken = this.getAccessToken();
        if (existingAccessToken != null && existingAccessToken.getExpirationTime().isAfter(this.getClock().instant())) {
            return existingAccessToken;
        } else {
            String clientId = this.getConfiguration().getClientId();
            String clientSecret = this.getConfiguration().getClientSecret();
            if (!StringUtils.isEmpty(clientId) && !StringUtils.isEmpty(clientSecret)) {
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

                    List<NameValuePair> httpParameters = new ArrayList<>();
                    httpParameters.add(new BasicNameValuePair("client_id", clientId));
                    httpParameters.add(new BasicNameValuePair("client_secret", clientSecret));
                    httpParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));

                    log.debug("Requesting OAuth access token for Lufthansa");
                    HttpPost httpPost = new HttpPost("https://api.lufthansa.com/v1/oauth/token");
                    httpPost.setEntity(new UrlEncodedFormEntity(httpParameters));
                    try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {

                        JsonNode responseNode = this.getObjectMapper().readTree(httpResponse.getEntity().getContent());
                        String accessTokenValue = responseNode.get("access_token").asText();
                        int expiresInValue = responseNode.get("expires_in").asInt();

                        log.debug("Successfully retrieved OAuth access token from Lufthansa");
                        LufthansaAccessToken accessToken = new LufthansaAccessToken();
                        accessToken.setExpirationTime(this.getClock().instant().plus(Duration.ofSeconds(expiresInValue)));
                        accessToken.setValue(accessTokenValue);
                        this.setAccessToken(accessToken);
                        return accessToken;

                    }

                } catch (Exception e) {
                    log.debug("Cannot fetch access token from Lufthansa", e);
                }
            }
            return null;
        }
    }

    LufthansaConfiguration getConfiguration() {
        return this.configuration;
    }
    @Autowired
    void setConfiguration(LufthansaConfiguration configuration) {
        this.configuration = configuration;
    }

    ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }
    void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    List<String> getLufthansaAirlineCodes() {
        return this.lufthansaAirlineCodes;
    }
    void setLufthansaAirlineCodes(List<String> lufthansaAirlineCodes) {
        this.lufthansaAirlineCodes = lufthansaAirlineCodes;
    }

    LufthansaAccessToken getAccessToken() {
        return this.accessToken;
    }
    void setAccessToken(LufthansaAccessToken accessToken) {
        this.accessToken = accessToken;
    }

    Clock getClock() {
        return this.clock;
    }
    void setClock(Clock clock) {
        this.clock = clock;
    }

}
