package de.perdian.flightlog.modules.aircrafts.persistence;

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
class AircraftTypesRepositoryImpl implements AircraftTypesRepository {

    private static final Logger log = LoggerFactory.getLogger(AircraftTypesRepositoryImpl.class);

    private ResourceLoader resourceLoader = null;
    private Map<String, AircraftTypeEntity> aircraftTypeBeansByIataCode = null;
    private Map<String, AircraftTypeEntity> aircraftTypeBeansByIcaoCode = null;

    @PostConstruct
    void initialize() throws IOException {

        Resource aircraftTypesResource = this.getResourceLoader().getResource("classpath:data/aircrafttypes.dat");
        log.info("Loading aircraftTypes from resource: {}", aircraftTypesResource);

        int totalAircraftTypesLoaded = 0;
        Map<String, AircraftTypeEntity> aircraftTypeBeansByIataCode = new LinkedHashMap<>();
        Map<String, AircraftTypeEntity> aircraftTypeBeansByIcaoCode = new LinkedHashMap<>();
        try (BufferedReader aircraftTypesReader = new BufferedReader(new InputStreamReader(aircraftTypesResource.getInputStream(), "UTF-8"))) {
            for (String aircraftTypeLine = aircraftTypesReader.readLine(); aircraftTypeLine != null; aircraftTypeLine = aircraftTypesReader.readLine()) {
                try {
                    List<String> lineFields = OpenflightsHelper.tokenizeLine(aircraftTypeLine);
                    if (lineFields.size() >= 3) {

                        String iataCode = lineFields.get(0);
                        String icaoCode = lineFields.get(1);
                        String title = lineFields.get(2);

                        if (!StringUtils.isEmpty(title)) {

                            AircraftTypeEntity aircraftTypeBean = new AircraftTypeEntity();
                            aircraftTypeBean.setIataCode(iataCode);
                            aircraftTypeBean.setIcaoCode(icaoCode);
                            aircraftTypeBean.setTitle(title);

                            if (!StringUtils.isEmpty(iataCode)) {
                                aircraftTypeBeansByIataCode.putIfAbsent(iataCode, aircraftTypeBean);
                            }
                            if (!StringUtils.isEmpty(icaoCode)) {
                                aircraftTypeBeansByIcaoCode.putIfAbsent(icaoCode, aircraftTypeBean);
                            }
                            totalAircraftTypesLoaded++;

                        }
                    }
                } catch (Exception e) {
                    log.warn("Invalid aircraftType line: {}", aircraftTypeLine, e);
                }
            }
        }
        this.setAircraftTypeBeansByIataCode(aircraftTypeBeansByIataCode);
        this.setAircraftTypeBeansByIcaoCode(aircraftTypeBeansByIcaoCode);
        log.debug("Loaded {} aircraftTypes from resource: {}", totalAircraftTypesLoaded, aircraftTypesResource);

    }

    @Override
    public AircraftTypeEntity loadAircraftTypeByIataCode(String code) {
        return StringUtils.isEmpty(code) ? null : this.getAircraftTypeBeansByIataCode().get(code);
    }

    @Override
    public AircraftTypeEntity loadAircraftTypeByIcaoCode(String code) {
        return StringUtils.isEmpty(code) ? null : this.getAircraftTypeBeansByIcaoCode().get(code);
    }

    @Override
    public AircraftTypeEntity loadAircraftTypeByCode(String code) {
        AircraftTypeEntity iataAircraftType = this.getAircraftTypeBeansByIataCode().get(code);
        return iataAircraftType == null ? this.getAircraftTypeBeansByIcaoCode().get(code) : iataAircraftType;
    }

    ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }
    @Autowired
    void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    Map<String, AircraftTypeEntity> getAircraftTypeBeansByIataCode() {
        return this.aircraftTypeBeansByIataCode;
    }
    void setAircraftTypeBeansByIataCode(Map<String, AircraftTypeEntity> aircraftTypeBeansByIataCode) {
        this.aircraftTypeBeansByIataCode = aircraftTypeBeansByIataCode;
    }

    Map<String, AircraftTypeEntity> getAircraftTypeBeansByIcaoCode() {
        return this.aircraftTypeBeansByIcaoCode;
    }
    void setAircraftTypeBeansByIcaoCode(Map<String, AircraftTypeEntity> aircraftTypeBeansByIcaoCode) {
        this.aircraftTypeBeansByIcaoCode = aircraftTypeBeansByIcaoCode;
    }


}
