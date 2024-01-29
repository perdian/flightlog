package de.perdian.flightlog.modules.flights.exchange;

import java.io.IOException;

public interface FlightsExchangeHandler {

    FlightsExchangePackage importPackage(FlightsExchangeEditor editor) throws IOException;

    byte[] exportPackage(FlightsExchangePackage exchangePackage) throws IOException;

}
