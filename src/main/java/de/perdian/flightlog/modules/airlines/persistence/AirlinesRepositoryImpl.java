package de.perdian.flightlog.modules.airlines.persistence;

import de.perdian.flightlog.modules.airlines.model.Airline;
import de.perdian.flightlog.support.openflights.OpenflightsHelper;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
class AirlinesRepositoryImpl implements AirlinesRepository {

    private static final Logger log = LoggerFactory.getLogger(AirlinesRepositoryImpl.class);

    private ResourceLoader resourceLoader = null;
    private Map<String, Airline> airlineBeansByCode = null;

    @PostConstruct
    void initialize() throws IOException {

        Map<String, Airline> airlineBeansByCode = new LinkedHashMap<>();

        Resource openflightsAirlinesResource = this.getResourceLoader().getResource("classpath:data/airlines.dat");
        log.info("Loading airlines from Openflights resource: {}", openflightsAirlinesResource);
        try (BufferedReader airlinesReader = new BufferedReader(new InputStreamReader(openflightsAirlinesResource.getInputStream(), "UTF-8"))) {
            for (String airlineLine = airlinesReader.readLine(); airlineLine != null; airlineLine = airlinesReader.readLine()) {
                try {
                    List<String> lineFields = OpenflightsHelper.tokenizeLine(airlineLine);
                    String iataCode = lineFields.get(3);
                    if (StringUtils.isNotEmpty(iataCode)) {
                        Airline airline = new Airline();
                        airline.setName(lineFields.get(1));
                        airline.setCode(iataCode);
                        airline.setCountryCode(lineFields.get(6));
                        airlineBeansByCode.putIfAbsent(iataCode, airline);
                    }
                } catch (Exception e) {
                    log.warn("Invalid airline line: {}", airlineLine, e);
                }
            }
        }

        Resource overwriteAirlinesResource = this.getResourceLoader().getResource("classpath:data/airlines-overwrite.dat");
        log.info("Loading airlines from overwrite resource: {}", overwriteAirlinesResource);
        try (BufferedReader airlinesReader = new BufferedReader(new InputStreamReader(overwriteAirlinesResource.getInputStream(), "UTF-8"))) {
            for (String airlineLine = airlinesReader.readLine(); airlineLine != null; airlineLine = airlinesReader.readLine()) {
                if (airlineLine.strip().length() > 0 && !airlineLine.startsWith("#")) {
                    try {
                        List<String> lineFields = OpenflightsHelper.tokenizeLine(airlineLine);
                        Airline airline = new Airline();
                        airline.setName(lineFields.get(1));
                        airline.setCode(lineFields.get(0));
                        airline.setCountryCode(lineFields.get(2));
                        airlineBeansByCode.put(airline.getCode(), airline);
                    } catch (Exception e) {
                        log.warn("Invalid overwrite airline line: {}", airlineLine, e);
                    }
                }
            }
        }

        this.setAirlineBeansByCode(airlineBeansByCode);
        log.debug("Loaded {} airlines from resources: {}", airlineBeansByCode.size(), List.of(openflightsAirlinesResource, overwriteAirlinesResource));

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
