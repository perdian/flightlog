package de.perdian.apps.flighttracker.persistence.repositories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import de.perdian.apps.flighttracker.business.modules.importexport.data.impl.OpenflightsHelper;
import de.perdian.apps.flighttracker.persistence.entities.AirportEntity;

/**
 * Airline data is used from
 * https://openflights.org/data.html#airport
 */

@Repository
class AirportsRepositoryImpl implements AirportsRepository {

    private static final Logger log = LoggerFactory.getLogger(AirportsRepositoryImpl.class);

    private ResourceLoader resourceLoader = null;
    private List<AirportEntity> airportBeans = null;
    private Map<String, AirportEntity> airportBeansByIataCode = null;

    @PostConstruct
    void initialize() throws IOException {

        Resource countriesResource = this.getResourceLoader().getResource("classpath:de/perdian/apps/flighttracker/data/countries.dat");
        log.info("Loading countries from resource: {}", countriesResource);
        Map<String, String> countryCodesByTitle = new LinkedHashMap<>();
        try (BufferedReader countriesReader = new BufferedReader(new InputStreamReader(countriesResource.getInputStream(), "UTF-8"))) {
            for (String countryLine = countriesReader.readLine(); countryLine != null; countryLine = countriesReader.readLine()) {
                List<String> countryFields = OpenflightsHelper.tokenizeLine(countryLine);
                String countryName = countryFields.get(0);
                String countryCode = countryFields.get(2);
                countryCodesByTitle.put(countryName, countryCode);
            }
        }
        log.debug("Loaded {} countries from resource: {}", countryCodesByTitle.size(), countriesResource);

        Resource airportsResource = this.getResourceLoader().getResource("classpath:de/perdian/apps/flighttracker/data/airports-extended.dat");
        log.info("Loading airports from resource: {}", airportsResource);

        List<AirportEntity> airportBeans = new ArrayList<>();
        Map<String, AirportEntity> airportBeansByIataCode = new LinkedHashMap<>();
        try (BufferedReader airportsReader = new BufferedReader(new InputStreamReader(airportsResource.getInputStream(), "UTF-8"))) {
            for (String airportLine = airportsReader.readLine(); airportLine != null; airportLine = airportsReader.readLine()) {
                try {

                    List<String> lineFields = OpenflightsHelper.tokenizeLine(airportLine);
                    String iataCode = lineFields.get(4);
                    String icaoCode = lineFields.get(5);
                    ZoneOffset zoneOffset = null;
                    String zoneOffsetString = lineFields.get(9);
                    if (zoneOffsetString != null && !zoneOffsetString.equals("\\N")) {
                        float zoneOffsetValue = Float.parseFloat(lineFields.get(9));
                        int zoneOffsetHours = (int)zoneOffsetValue;
                        int zoneOffsetMinutes = (int)((Math.abs(zoneOffsetValue) - Math.abs(zoneOffsetHours)) * 60 * Math.signum(zoneOffsetValue));
                        zoneOffset = ZoneOffset.ofHoursMinutes(zoneOffsetHours, zoneOffsetMinutes);
                    }

                    String zoneIdValue = lineFields.get(11);
                    ZoneId zoneId = zoneIdValue == null || zoneIdValue.equalsIgnoreCase("\\N") ? null : ZoneId.of(zoneIdValue);

                    AirportEntity airportBean = new AirportEntity();
                    airportBean.setName(lineFields.get(1));
                    airportBean.setCity(lineFields.get(2));
                    airportBean.setCountryCode(countryCodesByTitle.get(lineFields.get(3)));
                    airportBean.setIataCode(iataCode);
                    airportBean.setIcaoCode(icaoCode);
                    airportBean.setLatitude(Float.parseFloat(lineFields.get(6)));
                    airportBean.setLongitude(Float.parseFloat(lineFields.get(7)));
                    airportBean.setTimezoneOffset(zoneOffset);
                    airportBean.setTimezoneId(zoneId);
                    airportBean.setType(lineFields.get(12));

                    airportBeans.add(airportBean);
                    airportBeansByIataCode.put(iataCode, airportBean);

                } catch (Exception e) {
                    log.warn("Invalid airport line: {}", airportLine, e);
                }
            }
        }
        this.setAirportBeans(airportBeans);
        this.setAirportBeansByIataCode(airportBeansByIataCode);
        log.debug("Loaded {} airports from resource: {}", airportBeans.size(), airportsResource);

    }

    @Override
    public AirportEntity loadAirportByIataCode(String iataAirportCode) {
        return StringUtils.isEmpty(iataAirportCode) ? null : this.getAirportBeansByIataCode().get(iataAirportCode);
    }

    ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }
    @Autowired
    void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    List<AirportEntity> getAirportBeans() {
        return this.airportBeans;
    }
    void setAirportBeans(List<AirportEntity> airportBeans) {
        this.airportBeans = airportBeans;
    }

    Map<String, AirportEntity> getAirportBeansByIataCode() {
        return this.airportBeansByIataCode;
    }
    void setAirportBeansByIataCode(Map<String, AirportEntity> airportBeansByIataCode) {
        this.airportBeansByIataCode = airportBeansByIataCode;
    }

}
