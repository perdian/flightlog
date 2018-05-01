package de.perdian.flightlog.modules.wizard.services.impl.lufthansa;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import de.perdian.flightlog.modules.aircrafts.persistence.AircraftTypeEntity;
import de.perdian.flightlog.modules.aircrafts.persistence.AircraftTypesRepository;
import de.perdian.flightlog.modules.wizard.services.WizardData;
import de.perdian.flightlog.modules.wizard.services.WizardDataFactory;

@Component
public class LufthansaDataFactory implements WizardDataFactory {

    private static final Logger log = LoggerFactory.getLogger(LufthansaDataFactory.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<String> lufthansaAirlineCodes = Arrays.asList("LH");
    private LufthansaDataConfiguration configuration = null;
    private AircraftTypesRepository aircraftTypesRepository = null;
    private LufthansaAccessToken accessToken = null;
    private Clock clock = Clock.systemUTC();

    @Override
    public List<WizardData> createData(String airlineCode, String flightNumber, LocalDate departureDate, String departureAirportCode) {
        if (!StringUtils.isEmpty(airlineCode) && this.getLufthansaAirlineCodes().contains(airlineCode.toUpperCase())) {

            // First we try to get the flight status for the specific departure date passed as parameter.
            // If we can't get any information using this we try the same request with the current date.
            WizardData specificData = departureDate == null ? null : this.lookupFlightDataFromLufthansa(airlineCode, flightNumber, departureDate);
            if (specificData != null) {
                return Collections.singletonList(specificData);
            } else {
                WizardData todaysData = this.lookupFlightDataFromLufthansa(airlineCode, flightNumber, LocalDate.now(this.getClock()));
                if (todaysData != null) {
                    WizardData responseFlightData = new WizardData();
                    responseFlightData.setArrivalAirportCode(todaysData.getArrivalAirportCode());
                    responseFlightData.setDepartureAirportCode(todaysData.getDepartureAirportCode());
                    responseFlightData.setAircraftType(todaysData.getAircraftType());
                    return Collections.singletonList(responseFlightData);
                }
            }

        }
        return null;
    }

    private WizardData lookupFlightDataFromLufthansa(String airlineCode, String flightNumber, LocalDate departureDate) {
        LufthansaAccessToken accessToken = this.lookupAccessToken();
        if (accessToken != null) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

                StringBuilder httpGetUrl = new StringBuilder();
                httpGetUrl.append("https://api.lufthansa.com/v1/operations/flightstatus/");
                httpGetUrl.append(airlineCode.toUpperCase()).append(flightNumber);
                httpGetUrl.append("/").append(departureDate.toString());

                HttpGet httpGet = new HttpGet(httpGetUrl.toString());
                httpGet.setHeader("Authorization", "Bearer " + accessToken.getValue());

                // {"FlightStatusResource":{"Flights":{"Flight":{"Departure":{"AirportCode":"FRA","ScheduledTimeLocal":{"DateTime":"2017-09-07T11:00"},"ScheduledTimeUTC":{"DateTime":"2017-09-07T09:00Z"},"ActualTimeLocal":{"DateTime":"2017-09-07T11:28"},"ActualTimeUTC":{"DateTime":"2017-09-07T09:28Z"},"TimeStatus":{"Code":"DL","Definition":"Flight Delayed"},"Terminal":{"Name":1,"Gate":"C14"}},"Arrival":{"AirportCode":"JFK","ScheduledTimeLocal":{"DateTime":"2017-09-07T13:30"},"ScheduledTimeUTC":{"DateTime":"2017-09-07T17:30Z"},"EstimatedTimeLocal":{"DateTime":"2017-09-07T14:05"},"EstimatedTimeUTC":{"DateTime":"2017-09-07T18:05Z"},"TimeStatus":{"Code":"DL","Definition":"Flight Delayed"},"Terminal":{"Name":1}},"MarketingCarrier":{"AirlineID":"LH","FlightNumber":400},"OperatingCarrier":{"AirlineID":"LH","FlightNumber":400},"Equipment":{"AircraftCode":744},"FlightStatus":{"Code":"DP","Definition":"Flight Departed"}}},"Meta":{"@Version":"1.0.0","Link":[{"@Href":"https://api.lufthansa.com/v1/operations/flightstatus/LH400/2017-09-07","@Rel":"self"},{"@Href":"https://api.lufthansa.com/v1/references/airports/{airportCode}","@Rel":"related"}]}}}

                try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        JsonNode responseNode = this.getObjectMapper().readTree(httpResponse.getEntity().getContent());
                        JsonNode flightsNode = responseNode.get("FlightStatusResource").get("Flights");
                        if (flightsNode.size() == 1) {

                            JsonNode flightNode = flightsNode.get("Flight");
                            JsonNode departureNode = flightNode.get("Departure");
                            LocalDateTime departureTime = this.extractDateTime(departureNode, "ActualTimeLocal", "ScheduledTimeLocal");
                            JsonNode arrivalNode = flightNode.get("Arrival");
                            LocalDateTime arrivalTime = this.extractDateTime(arrivalNode, "ActualTimeLocal", "ScheduledTimeLocal");

                            JsonNode equipmentNode = flightNode.get("Equipment");
                            JsonNode aircraftCodeNode = equipmentNode == null ? null : equipmentNode.get("AircraftCode");
                            String aircraftCode = aircraftCodeNode == null ? null : aircraftCodeNode.asText();
                            AircraftTypeEntity aircraftTypeResolved = this.getAircraftTypesRepository().loadAircraftTypeByCode(aircraftCode);
                            String aircraftType = aircraftTypeResolved == null ? this.extractAircraftType(aircraftCode, httpClient, accessToken) : aircraftTypeResolved.getTitle();

                            WizardData data = new WizardData();
                            data.setDepartureAirportCode(departureNode.get("AirportCode").asText());
                            data.setDepartureDateLocal(departureTime == null ? null : departureTime.toLocalDate());
                            data.setDepartureTimeLocal(departureTime == null ? null : departureTime.toLocalTime());
                            data.setArrivalAirportCode(arrivalNode.get("AirportCode").asText());
                            data.setArrivalDateLocal(arrivalTime == null ? null : arrivalTime.toLocalDate());
                            data.setArrivalTimeLocal(arrivalTime == null ? null : arrivalTime.toLocalTime());
                            data.setAircraftType(aircraftType);
                            return data;

                        }
                    } else {
                        log.debug("Invalid response returned from Lufthansa for flight status for flight '{}{}' on {}: {}", airlineCode, flightNumber, departureDate, httpResponse.getStatusLine());
                    }
                }

            } catch (Exception e) {
                log.debug("Cannot fetch flight information from Lufthansa for flight '{}{}' on {}", airlineCode, flightNumber, departureDate, e);
            }
        }
        return null;
    }

    private LocalDateTime extractDateTime(JsonNode parentNode, String... childNodeNames) {
        for (String childNodeName : childNodeNames) {
            JsonNode childNode = parentNode.get(childNodeName);
            JsonNode dateTimeNode = childNode == null ? null : childNode.get("DateTime");
            String dateTimeValue = dateTimeNode == null ? null : dateTimeNode.asText();
            if (!StringUtils.isEmpty(dateTimeValue)) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                try {
                    return LocalDateTime.parse(dateTimeValue, dateTimeFormatter);
                } catch (Exception e) {
                    log.trace("Invalid date time value: " + dateTimeValue, e);
                }
            }
        }
        return null;
    }

    private String extractAircraftType(String aircraftCode, CloseableHttpClient httpClient, LufthansaAccessToken accessToken2) {
        if (!StringUtils.isEmpty(aircraftCode)) {
            String httpGetUrl = "https://api.lufthansa.com/v1/references/aircraft/" + aircraftCode;
            HttpGet httpGet = new HttpGet(httpGetUrl.toString());
            httpGet.setHeader("Authorization", "Bearer " + this.accessToken.getValue());
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                if (httpResponse.getStatusLine().getStatusCode() == 200) {

                    // {"AircraftResource":{"AircraftSummaries":{"AircraftSummary":{"AircraftCode":"74H","Names":{"Name":{"@LanguageCode":"EN","$":"Boeing 747-8i"}},"AirlineEquipCode":"747-8i","MediaLinks":{"MediaLink":{"@LanguageCode":"EN","$":"291853146"}},"Comments":{"Comment":[{"@LanguageCode":"EN","$":"New First & Business Class"},{"@LanguageCode":"DE","$":"Neue First & Business Class"}]},"OnBoardEquipment":{"InflightEntertainment":true,"Compartment":[{"ClassCode":"F","ClassDesc":"FirstClass","FlyNet":true,"SeatPower":true,"Usb":true,"LiveTv":true},{"ClassCode":"C","ClassDesc":"BusinessClass","FlyNet":true,"SeatPower":true,"Usb":true,"LiveTv":true},{"ClassCode":"E","ClassDesc":"PremiumEconomy","FlyNet":true,"SeatPower":true,"Usb":true,"LiveTv":true},{"ClassCode":"Y","ClassDesc":"Economy","FlyNet":true,"SeatPower":true,"Usb":true,"LiveTv":true}]}}},"Meta":{"@Version":"1.0.0","Link":[{"@Href":"https://api.lufthansa.com/v1/references/aircraft/74H","@Rel":"self"},{"@Href":"http://www.lufthansa.com/mediapool/pdf/46/media_291853146.pdf","@Rel":"alternate"}]}}}

                    JsonNode responseNode = this.getObjectMapper().readTree(httpResponse.getEntity().getContent());
                    return responseNode.get("AircraftResource").get("AircraftSummaries").get("AircraftSummary").get("Names").get("Name").get("$").asText();

                } else {
                    log.debug("Invalid response returned from Lufthansa for aircraft code '{}': {}", aircraftCode, httpResponse.getStatusLine());
                }
            } catch (Exception e) {
                log.debug("Cannot fetch aircraft information from Lufthansa for aircraft code '{}'", aircraftCode, e);
            }
        }
        return null;
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

    class LufthansaAccessToken {

        private String value = null;
        private Instant expirationTime = null;

        String getValue() {
            return this.value;
        }
        void setValue(String value) {
            this.value = value;
        }

        Instant getExpirationTime() {
            return this.expirationTime;
        }
        void setExpirationTime(Instant expirationTime) {
            this.expirationTime = expirationTime;
        }

    }

    LufthansaDataConfiguration getConfiguration() {
        return this.configuration;
    }
    @Autowired
    void setConfiguration(LufthansaDataConfiguration configuration) {
        this.configuration = configuration;
    }

    AircraftTypesRepository getAircraftTypesRepository() {
        return this.aircraftTypesRepository;
    }
    @Autowired
    void setAircraftTypesRepository(AircraftTypesRepository aircraftTypesRepository) {
        this.aircraftTypesRepository = aircraftTypesRepository;
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
