package de.perdian.flightlog.modules.flights.web;

import de.perdian.flightlog.modules.airlines.persistence.AirlineEntity;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;
import de.perdian.flightlog.modules.airports.persistence.AirportEntity;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.modules.flights.web.editor.FlightEditor;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice(assignableTypes = {
    FlightsAddController.class,
    FlightsEditController.class
})
public class FlightsAdvice {

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

    @ModelAttribute("seatTypeValues")
    List<SeatType> seatTypeValues() {
        return List.of(SeatType.values());
    }

    @ModelAttribute("cabinClassValues")
    List<CabinClass> cabinClassValues() {
        return List.of(CabinClass.values());
    }

    @ModelAttribute("flightReasonValues")
    List<FlightReason> flightReasonValues() {
        return List.of(FlightReason.values());
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
