package de.perdian.flightlog.modules.flights.web;

import java.util.List;

public class FlightEditorHelper {

    private List<String> seatTypeValues = null;
    private List<String> cabinClassValues = null;
    private List<String> flightReasonValues = null;

    public List<String> getSeatTypeValues() {
        return this.seatTypeValues;
    }
    public void setSeatTypeValues(List<String> seatTypeValues) {
        this.seatTypeValues = seatTypeValues;
    }

    public List<String> getCabinClassValues() {
        return this.cabinClassValues;
    }
    public void setCabinClassValues(List<String> cabinClassValues) {
        this.cabinClassValues = cabinClassValues;
    }

    public List<String> getFlightReasonValues() {
        return this.flightReasonValues;
    }
    public void setFlightReasonValues(List<String> flightReasonValues) {
        this.flightReasonValues = flightReasonValues;
    }

}
