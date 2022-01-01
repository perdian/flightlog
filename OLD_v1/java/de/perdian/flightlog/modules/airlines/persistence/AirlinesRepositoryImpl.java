package de.perdian.flightlog.modules.airlines.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import de.perdian.flightlog.modules.importexport.data.impl.OpenflightsHelper;

@Repository
class AirlinesRepositoryImpl implements AirlinesRepository {

    private static final Logger log = LoggerFactory.getLogger(AirlinesRepositoryImpl.class);

    private ResourceLoader resourceLoader = null;
    private Map<String, AirlineEntity> airlineBeansByCode = null;

    @PostConstruct
    void initialize() throws IOException {

        Map<String, AirlineEntity> airlineBeansByCode = new LinkedHashMap<>();

        Resource openflightsAirlinesResource = this.getResourceLoader().getResource("classpath:de/perdian/flightlog/data/airlines.dat");
        log.info("Loading airlines from Openflights resource: {}", openflightsAirlinesResource);
        try (BufferedReader airlinesReader = new BufferedReader(new InputStreamReader(openflightsAirlinesResource.getInputStream(), "UTF-8"))) {
            for (String airlineLine = airlinesReader.readLine(); airlineLine != null; airlineLine = airlinesReader.readLine()) {
                try {
                    List<String> lineFields = OpenflightsHelper.tokenizeLine(airlineLine);
                    String iataCode = lineFields.get(3);
                    if (!StringUtils.isEmpty(iataCode)) {
                        AirlineEntity airlineEntity = new AirlineEntity();
                        airlineEntity.setName(lineFields.get(1));
                        airlineEntity.setCode(iataCode);
                        airlineEntity.setCountryCode(lineFields.get(6));
                        airlineBeansByCode.putIfAbsent(iataCode, airlineEntity);
                    }
                } catch (Exception e) {
                    log.warn("Invalid airline line: {}", airlineLine, e);
                }
            }
        }

        Resource overwriteAirlinesResource = this.getResourceLoader().getResource("classpath:de/perdian/flightlog/data/airlines-overwrite.dat");
        log.info("Loading airlines from overwrite resource: {}", overwriteAirlinesResource);
        try (BufferedReader airlinesReader = new BufferedReader(new InputStreamReader(overwriteAirlinesResource.getInputStream(), "UTF-8"))) {
            for (String airlineLine = airlinesReader.readLine(); airlineLine != null; airlineLine = airlinesReader.readLine()) {
                if (airlineLine.strip().length() > 0 && !airlineLine.startsWith("#")) {
                    try {
                        List<String> lineFields = OpenflightsHelper.tokenizeLine(airlineLine);
                        AirlineEntity airlineEntity = new AirlineEntity();
                        airlineEntity.setName(lineFields.get(1));
                        airlineEntity.setCode(lineFields.get(0));
                        airlineEntity.setCountryCode(lineFields.get(2));
                        airlineBeansByCode.put(airlineEntity.getCode(), airlineEntity);
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
    public AirlineEntity loadAirlineByCode(String airlineCode) {
        return StringUtils.isEmpty(airlineCode) ? null : this.getAirlineBeansByCode().get(airlineCode);
    }

    @Override
    public AirlineEntity loadAirlineByName(String airlineName) {
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

    Map<String, AirlineEntity> getAirlineBeansByCode() {
        return this.airlineBeansByCode;
    }
    void setAirlineBeansByCode(Map<String, AirlineEntity> airlineBeansByCode) {
        this.airlineBeansByCode = airlineBeansByCode;
    }

}
