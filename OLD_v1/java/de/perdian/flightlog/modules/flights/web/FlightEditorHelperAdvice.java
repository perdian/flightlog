package de.perdian.flightlog.modules.flights.web;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;

@ControllerAdvice
public class FlightEditorHelperAdvice {

    @ModelAttribute
    public FlightEditorHelper flightEditorHelper() {
        FlightEditorHelper flightEditorHelper = new FlightEditorHelper();
        flightEditorHelper.setCabinClassValues(Arrays.stream(CabinClass.values()).map(Enum::name).collect(Collectors.toList()));
        flightEditorHelper.setFlightReasonValues(Arrays.stream(FlightReason.values()).map(Enum::name).collect(Collectors.toList()));
        flightEditorHelper.setSeatTypeValues(Arrays.stream(SeatType.values()).map(Enum::name).collect(Collectors.toList()));
        return flightEditorHelper;
    }

}
