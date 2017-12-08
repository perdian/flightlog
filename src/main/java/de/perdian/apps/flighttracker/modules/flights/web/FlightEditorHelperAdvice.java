package de.perdian.apps.flighttracker.modules.flights.web;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import de.perdian.apps.flighttracker.modules.flights.services.FlightsQueryService;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

@ControllerAdvice
public class FlightEditorHelperAdvice {

    private FlightsQueryService flightsQueryService = null;

    @ModelAttribute
    public FlightEditorHelper flightEditorHelper() {
        FlightEditorHelper flightEditorHelper = new FlightEditorHelper();
        flightEditorHelper.setCabinClassValues(Arrays.stream(CabinClass.values()).map(Enum::name).collect(Collectors.toList()));
        flightEditorHelper.setFlightReasonValues(Arrays.stream(FlightReason.values()).map(Enum::name).collect(Collectors.toList()));
        flightEditorHelper.setSeatTypeValues(Arrays.stream(SeatType.values()).map(Enum::name).collect(Collectors.toList()));
        return flightEditorHelper;
    }

    FlightsQueryService getFlightsQueryService() {
        return this.flightsQueryService;
    }
    @Autowired
    void setFlightsQueryService(FlightsQueryService flightsQueryService) {
        this.flightsQueryService = flightsQueryService;
    }

}