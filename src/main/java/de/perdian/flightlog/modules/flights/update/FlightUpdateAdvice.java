package de.perdian.flightlog.modules.flights.update;

import de.perdian.flightlog.modules.airlines.persistence.AirlineEntity;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;
import de.perdian.flightlog.modules.airports.persistence.AirportEntity;
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
        if (StringUtils.isNotEmpty(flightEditor.getAirlineCode()) && StringUtils.isEmpty(flightEditor.getAirlineName())) {
            AirlineEntity airlineEntity = this.getAirlinesRepository().loadAirlineByCode(flightEditor.getAirlineCode());
            if (airlineEntity != null) {
                flightEditor.setAirlineName(airlineEntity.getName());
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

    AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

}
