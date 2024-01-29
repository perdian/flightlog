package de.perdian.flightlog.modules.flights.exchange.impl;

import de.perdian.flightlog.modules.flights.exchange.FlightsExchangeEditor;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangeHandler;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangePackage;

import java.io.FileNotFoundException;
import java.io.IOException;

abstract class AbstractFileBasedExchangeHandler implements FlightsExchangeHandler {

    @Override
    public FlightsExchangePackage importPackage(FlightsExchangeEditor editor) throws IOException {
        if (editor.getFile() == null || editor.getFile().isEmpty()) {
            throw new FileNotFoundException("No uploaded file found");
        } else {
            byte[] fileBytes = editor.getFile().getBytes();
            return null;
        }
    }

    @Override
    public byte[] exportPackage(FlightsExchangePackage exchangePackage) {
        return null;
    }

}
