package de.perdian.flightlog.modules.flights.web;

import de.perdian.flightlog.modules.airports.persistence.AirportEntity;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.modules.flights.web.editor.FlightEditor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = {
    FlightsAddController.class
})
public class FlightsAdvice {

    private AirportsRepository airportsRepository = null;

    @ModelAttribute("flightEditor")
    FlightEditor flightEditor(FlightEditor flightEditor) {
        if (StringUtils.isNotEmpty(flightEditor.getDepartureAirportCode())) {
            AirportEntity departureAirportEntity = this.getAirportsRepository().loadAirportByIataCode(flightEditor.getDepartureAirportCode());
            if (departureAirportEntity != null) {
                flightEditor.setDepartureAirportName(departureAirportEntity.getName());
                flightEditor.setDepartureAirportCountryCode(departureAirportEntity.getCountryCode());
            }
        }
        if (StringUtils.isNotEmpty(flightEditor.getArrivalAirportCode())) {
            AirportEntity arrivalAirportEntity = this.getAirportsRepository().loadAirportByIataCode(flightEditor.getArrivalAirportCode());
            if (arrivalAirportEntity != null) {
                flightEditor.setArrivalAirportName(arrivalAirportEntity.getName());
                flightEditor.setArrivalAirportCountryCode(arrivalAirportEntity.getCountryCode());
            }
        }
        return flightEditor;
    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

}
