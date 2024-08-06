package de.perdian.flightlog.modules.flights.update;

import de.perdian.flightlog.modules.airlines.model.Airline;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;
import de.perdian.flightlog.modules.airports.model.Airport;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = {
    FlightUpdateController.class
})
class FlightUpdateAdvice {

    private AirportsRepository airportsRepository = null;
    private AirlinesRepository airlinesRepository = null;

    @ModelAttribute("flightUpdateEditor")
    FlightUpdateEditor flightUpdateEditor(FlightUpdateEditor flightUpdateEditor) {
        if (StringUtils.isNotEmpty(flightUpdateEditor.getDepartureAirportCode())) {
            Airport departureAirport = this.getAirportsRepository().loadAirportByCode(flightUpdateEditor.getDepartureAirportCode());
            if (departureAirport != null) {
                flightUpdateEditor.setDepartureAirportName(departureAirport.getName());
                flightUpdateEditor.setDepartureAirportCountryCode(departureAirport.getCountryCode());
            }
        }
        if (StringUtils.isNotEmpty(flightUpdateEditor.getArrivalAirportCode())) {
            Airport arrivalAirport = this.getAirportsRepository().loadAirportByCode(flightUpdateEditor.getArrivalAirportCode());
            if (arrivalAirport != null) {
                flightUpdateEditor.setArrivalAirportName(arrivalAirport.getName());
                flightUpdateEditor.setArrivalAirportCountryCode(arrivalAirport.getCountryCode());
            }
        }
        if (StringUtils.isNotEmpty(flightUpdateEditor.getAirlineCode()) && StringUtils.isEmpty(flightUpdateEditor.getAirlineName())) {
            Airline airline = this.getAirlinesRepository().loadAirlineByCode(flightUpdateEditor.getAirlineCode());
            if (airline != null) {
                flightUpdateEditor.setAirlineName(airline.getName());
            }
        }
        return flightUpdateEditor;
    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

    AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

}
