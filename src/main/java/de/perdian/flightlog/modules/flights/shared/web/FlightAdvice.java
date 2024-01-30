package de.perdian.flightlog.modules.flights.shared.web;

import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class FlightAdvice {

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

}
