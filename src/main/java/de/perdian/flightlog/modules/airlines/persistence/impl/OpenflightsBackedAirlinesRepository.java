package de.perdian.flightlog.modules.airlines.persistence.impl;

import de.perdian.flightlog.modules.airlines.model.Airline;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
class OpenflightsBackedAirlinesRepository implements AirlinesRepository {

    private static final Logger log = LoggerFactory.getLogger(OpenflightsBackedAirlinesRepository.class);

    private ResourceLoader resourceLoader = null;
    private Map<String, Airline> airlineBeansByCode = null;

    @PostConstruct
    void initialize() throws IOException {

        Map<String, Airline> airlineBeansByCode = new LinkedHashMap<>();

        Resource openflightsResource = this.getResourceLoader().getResource("classpath:data/openflights/airlines.dat");
        log.info("Loading airlines from Openflights resource: {}", openflightsResource);
        for (String openflightsLine : ResourceUtils.resourceToLines(openflightsResource)) {
            Airline openflightsAirline = this.parseAirlineFromLine(openflightsLine);
            if (openflightsAirline != null) {
                airlineBeansByCode.putIfAbsent(openflightsAirline.getCode().toUpperCase(), openflightsAirline);
            }
        }

        Resource overwriteResource = this.getResourceLoader().getResource("classpath:data/openflights/airlines-overwrite.dat");
        log.info("Loading airlines from overwrite resource: {}", overwriteResource);
        for (String overwriteLine : ResourceUtils.resourceToLines(overwriteResource)) {
            Airline overwriteAirline = this.parseAirlineFromLine(overwriteLine);
            if (overwriteAirline != null) {
                airlineBeansByCode.put(overwriteAirline.getCode().toUpperCase(), overwriteAirline);
            }
        }

        this.setAirlineBeansByCode(airlineBeansByCode);
        log.debug("Loaded {} airlines from resources: {}", airlineBeansByCode.size(), List.of(openflightsResource, overwriteResource));

    }

    private Airline parseAirlineFromLine(String airlineLine) {
        List<String> lineFields = OpenflightsHelper.tokenizeLine(airlineLine);
        String iataCode = lineFields.get(3);
        if (StringUtils.isEmpty(iataCode)) {
            return null;
        } else {
            Airline airline = new Airline();
            airline.setName(lineFields.get(1));
            airline.setCode(iataCode);
            airline.setCountryCode(lineFields.get(6));
            return airline;
        }
    }

    @Override
    public Airline loadAirlineByCode(String airlineCode) {
        return StringUtils.isEmpty(airlineCode) ? null : this.getAirlineBeansByCode().get(airlineCode.toUpperCase());
    }

    @Override
    public Airline loadAirlineByName(String airlineName) {
        return this.getAirlineBeansByCode().values().stream()
            .filter(airline -> airline.getName().equalsIgnoreCase(airlineName))
            .findFirst()
            .orElse(null);
    }

    ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }
    @Autowired
    void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    Map<String, Airline> getAirlineBeansByCode() {
        return this.airlineBeansByCode;
    }
    void setAirlineBeansByCode(Map<String, Airline> airlineBeansByCode) {
        this.airlineBeansByCode = airlineBeansByCode;
    }

}
