package de.perdian.apps.flighttracker.web.modules.flights;

import java.io.Serializable;

public class FlightsWizardData implements Serializable {

    static final long serialVersionUID = 1L;

    private String wizDepartureDateLocal = null;
    private String wizDepartureTimeLocal = null;
    private String wizAirlineCode = null;
    private String wizFlightNumber = null;
    private String wizDepartureAirportCode = null;
    private String wizArrivalAirportCode = null;

    public String getWizDepartureDateLocal() {
        return this.wizDepartureDateLocal;
    }
    public void setWizDepartureDateLocal(String wizDepartureDateLocal) {
        this.wizDepartureDateLocal = wizDepartureDateLocal;
    }

    public String getWizDepartureTimeLocal() {
        return this.wizDepartureTimeLocal;
    }
    public void setWizDepartureTimeLocal(String wizDepartureTimeLocal) {
        this.wizDepartureTimeLocal = wizDepartureTimeLocal;
    }

    public String getWizAirlineCode() {
        return this.wizAirlineCode;
    }
    public void setWizAirlineCode(String wizAirlineCode) {
        this.wizAirlineCode = wizAirlineCode;
    }

    public String getWizFlightNumber() {
        return this.wizFlightNumber;
    }
    public void setWizFlightNumber(String wizFlightNumber) {
        this.wizFlightNumber = wizFlightNumber;
    }

    public String getWizDepartureAirportCode() {
        return this.wizDepartureAirportCode;
    }
    public void setWizDepartureAirportCode(String wizDepartureAirportCode) {
        this.wizDepartureAirportCode = wizDepartureAirportCode;
    }

    public String getWizArrivalAirportCode() {
        return this.wizArrivalAirportCode;
    }
    public void setWizArrivalAirportCode(String wizArrivalAirportCode) {
        this.wizArrivalAirportCode = wizArrivalAirportCode;
    }

}
