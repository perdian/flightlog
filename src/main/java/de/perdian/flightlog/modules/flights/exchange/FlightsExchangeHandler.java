package de.perdian.flightlog.modules.flights.exchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FlightsExchangeHandler {

    FlightsExchangePackage importPackage(InputStream inputStream) throws IOException;

    void exportPackage(FlightsExchangePackage exchangePackage, OutputStream targetStream) throws IOException;

}
