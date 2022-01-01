package de.perdian.flightlog.modules.wizard.services;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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
    public List<WizardData> createData(String airlineCode, String flightNumber, LocalDate departureDate, String departureAirportCode) {

        // To get a valid WizardData object we simply loop through all the available sources until
        // the first one returns a valid response
        return Optional.ofNullable(this.getFactories()).orElseGet(Collections::emptyList).stream()
            .map(source -> this.createDataFromFactory(source, airlineCode, flightNumber, departureDate, departureAirportCode))
            .filter(list -> list != null && !list.isEmpty())
            .findFirst()
            .orElse(null);

    }

    private List<WizardData> createDataFromFactory(WizardDataFactory factory, String airlineCode, String flightNumber, LocalDate departureDate, String departureAirportCode) {
        try {
            return factory.createData(airlineCode, flightNumber, departureDate, departureAirportCode);
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
