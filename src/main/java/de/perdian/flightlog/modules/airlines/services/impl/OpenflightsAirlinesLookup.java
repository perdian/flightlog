package de.perdian.flightlog.modules.airlines.services.impl;

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
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import de.perdian.flightlog.modules.airlines.model.AirlineBean;
import de.perdian.flightlog.modules.airlines.services.AirlinesLookup;
import de.perdian.flightlog.modules.importexport.data.impl.OpenflightsHelper;
import de.perdian.flightlog.modules.users.persistence.UserEntity;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
class OpenflightsAirlinesLookup implements AirlinesLookup {

    private static final Logger log = LoggerFactory.getLogger(OpenflightsAirlinesLookup.class);

    private ResourceLoader resourceLoader = null;
    private Map<String, AirlineBean> airlineBeansByCode = null;

    @PostConstruct
    void initialize() throws IOException {

        Resource airlinesResource = this.getResourceLoader().getResource("classpath:de/perdian/flightlog/data/airlines.dat");
        log.info("Loading airlines from resource: {}", airlinesResource);

        Map<String, AirlineBean> airlineBeansByCode = new LinkedHashMap<>();
        try (BufferedReader airlinesReader = new BufferedReader(new InputStreamReader(airlinesResource.getInputStream(), "UTF-8"))) {
            for (String airlineLine = airlinesReader.readLine(); airlineLine != null; airlineLine = airlinesReader.readLine()) {
                try {
                    List<String> lineFields = OpenflightsHelper.tokenizeLine(airlineLine);
                    String iataCode = lineFields.get(3);
                    if (!StringUtils.isEmpty(iataCode)) {
                        AirlineBean airlineBean = new AirlineBean();
                        airlineBean.setName(lineFields.get(1));
                        airlineBean.setCode(iataCode);
                        airlineBean.setCountryCode(lineFields.get(6));
                        airlineBeansByCode.putIfAbsent(iataCode, airlineBean);
                    }
                } catch (Exception e) {
                    log.warn("Invalid airline line: {}", airlineLine, e);
                }
            }
        }
        this.setAirlineBeansByCode(airlineBeansByCode);
        log.debug("Loaded airlines from resource: {}", airlinesResource);

    }

    @Override
    public AirlineBean loadAirlineByCode(String airlineCode, UserEntity user) {
        return StringUtils.isEmpty(airlineCode) ? null : this.getAirlineBeansByCode().get(airlineCode);
    }

    @Override
    public AirlineBean loadAirlineByName(String airlineName, UserEntity user) {
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

    Map<String, AirlineBean> getAirlineBeansByCode() {
        return this.airlineBeansByCode;
    }
    void setAirlineBeansByCode(Map<String, AirlineBean> airlineBeansByCode) {
        this.airlineBeansByCode = airlineBeansByCode;
    }

}
