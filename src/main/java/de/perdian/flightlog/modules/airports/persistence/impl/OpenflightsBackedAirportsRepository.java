package de.perdian.flightlog.modules.airports.persistence.impl;

import de.perdian.flightlog.modules.airports.model.Airport;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.support.openflights.OpenflightsHelper;
import de.perdian.flightlog.support.utils.ResourceUtils;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
class OpenflightsBackedAirportsRepository implements AirportsRepository {

    private static final Logger log = LoggerFactory.getLogger(OpenflightsBackedAirportsRepository.class);

    private ResourceLoader resourceLoader = null;
    private Map<String, Airport> airportBeansByCode = null;

    @PostConstruct
    void initialize() throws IOException {

        Map<String, String> countryCodesByTitle = new LinkedHashMap<>();
        Resource countriesResource = this.getResourceLoader().getResource("classpath:data/openflights/countries.dat");
        log.info("Loading countries from Openflights resource: {}", countriesResource);
        for (String countriesLine : ResourceUtils.resourceToLines(countriesResource)) {
            List<String> countryFields = OpenflightsHelper.tokenizeLine(countriesLine);
            String countryName = countryFields.get(0);
            String countryCode = countryFields.get(2);
            countryCodesByTitle.put(countryName, countryCode);
        }
        log.debug("Loaded {} countries from resource: {}", countryCodesByTitle.size(), countriesResource);

        Map<String, Airport> airportBeansByCode = new LinkedHashMap<>();
        Resource airportsResource = this.getResourceLoader().getResource("classpath:data/openflights/airports-extended.dat");
        log.info("Loading airports from Openflights resource: {}", airportsResource);
        for (String airportLine : ResourceUtils.resourceToLines(airportsResource)) {
            Airport airport = this.parseAirportFromLine(airportLine, countryCodesByTitle);
            if (airport != null && StringUtils.isNotEmpty(airport.getCode())) {
              airportBeansByCode.put(airport.getCode(), airport);
            }
        }

        Resource overwriteResource = this.getResourceLoader().getResource("classpath:data/openflights/airports-extended-overwrite.dat");
        if (overwriteResource.exists()) {
            log.info("Loading airports from overwrite resource: {}", airportsResource);
            for (String overwriteLine : ResourceUtils.resourceToLines(overwriteResource)) {
                Airport airport = this.parseAirportFromLine(overwriteLine, countryCodesByTitle);
                if (airport != null && StringUtils.isNotEmpty(airport.getCode())) {
                    airportBeansByCode.put(airport.getCode(), airport);
                }
            }
        }

        this.setAirportBeansByCode(airportBeansByCode);
        log.debug("Loaded {} airports from resource: {}", airportBeansByCode.size(), airportsResource);

    }

    private Airport parseAirportFromLine(String airportLine, Map<String, String> countryCodesByTitle) {
        List<String> airportFields = OpenflightsHelper.tokenizeLine(airportLine);
        String iataCode = airportFields.get(4);
        ZoneOffset zoneOffset = null;
        String zoneOffsetString = airportFields.get(9);
        if (zoneOffsetString != null && !zoneOffsetString.equals("\\N")) {
            float zoneOffsetValue = Float.parseFloat(airportFields.get(9));
            int zoneOffsetHours = (int) zoneOffsetValue;
            int zoneOffsetMinutes = (int) ((Math.abs(zoneOffsetValue) - Math.abs(zoneOffsetHours)) * 60 * Math.signum(zoneOffsetValue));
            zoneOffset = ZoneOffset.ofHoursMinutes(zoneOffsetHours, zoneOffsetMinutes);
        }

        String zoneIdValue = airportFields.get(11);
        ZoneId zoneId = zoneIdValue == null || zoneIdValue.equalsIgnoreCase("\\N") ? null : ZoneId.of(zoneIdValue);

        Airport airport = new Airport();
        airport.setName(airportFields.get(1));
        airport.setCity(airportFields.get(2));
        airport.setCountryCode(countryCodesByTitle.get(airportFields.get(3)));
        airport.setCode(iataCode);
        airport.setLatitude(Float.parseFloat(airportFields.get(6)));
        airport.setLongitude(Float.parseFloat(airportFields.get(7)));
        airport.setTimezoneOffset(zoneOffset);
        airport.setTimezoneId(zoneId);
        airport.setType(airportFields.get(12));
        return airport;

    }

    @Override
    public Airport loadAirportByCode(String airportCode) {
        return StringUtils.isEmpty(airportCode) ? null : this.getAirportBeansByCode().get(airportCode.toUpperCase());
    }

    ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }
    @Autowired
    void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    Map<String, Airport> getAirportBeansByCode() {
        return this.airportBeansByCode;
    }
    void setAirportBeansByCode(Map<String, Airport> airportBeansByCode) {
        this.airportBeansByCode = airportBeansByCode;
    }

}
