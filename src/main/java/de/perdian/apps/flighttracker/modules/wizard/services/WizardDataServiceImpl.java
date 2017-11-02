package de.perdian.apps.flighttracker.modules.wizard.services;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class WizardDataServiceImpl implements WizardDataService {

    private static final Logger log = LoggerFactory.getLogger(WizardDataServiceImpl.class);

    private List<WizardDataFactory> factories = null;

    @Override
    public WizardData createData(String airlineCode, String flightNumber, LocalDate departureDate) {

        // To get a valid WizardData object we simply loop through all the available sources until
        // the first one returns a valid response
        return Optional.ofNullable(this.getFactories()).orElseGet(Collections::emptyList).stream()
            .map(source -> this.createDataFromFactory(source, airlineCode, flightNumber, departureDate))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);

    }

    private WizardData createDataFromFactory(WizardDataFactory factory, String airlineCode, String flightNumber, LocalDate departureDate) {
        try {
            return factory.createData(airlineCode, flightNumber, departureDate);
        } catch (Exception e) {
            log.warn("Cannot load flight data from factory '{}' [airlineCode={}, flightNumber={}, departureDate={}]", factory, airlineCode, flightNumber, departureDate, e);
            return null;
        }
    }

    List<WizardDataFactory> getFactories() {
        return this.factories;
    }
    @Autowired
    void setFactories(List<WizardDataFactory> factories) {
        this.factories = factories;
    }

}
