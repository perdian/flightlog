package de.perdian.flightlog.modules.flights.exchange;

import de.perdian.flightlog.modules.flights.exchange.impl.JsonExchangeHandler;
import de.perdian.flightlog.modules.flights.exchange.impl.XmlExchangeHandler;
import jakarta.activation.DataSource;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public enum FlightsExchangeFormat {

    XML(new XmlExchangeHandler(), MediaType.APPLICATION_XML),
    JSON(new JsonExchangeHandler(), MediaType.APPLICATION_JSON);

    private FlightsExchangeHandler handler = null;
    private MediaType mimeType = null;

    FlightsExchangeFormat(FlightsExchangeHandler handler, MediaType mimeType) {
        this.setHandler(handler);
        this.setMimeType(mimeType);
    }

    public DataSource toDataSource(FlightsExchangePackage exchangePackage) {
        try (ByteArrayOutputStream backupStream = new ByteArrayOutputStream()) {
            this.getHandler().exportPackage(exchangePackage, backupStream);
            return new ByteArrayDataSource(backupStream.toByteArray(), this.getMimeType().toString());
        } catch (IOException e) {
            throw new RuntimeException("Cannot encode exchange package using format: " + this.name(), e);
        }
    }

    public FlightsExchangeHandler getHandler() {
        return this.handler;
    }
    private void setHandler(FlightsExchangeHandler handler) {
        this.handler = handler;
    }

    public MediaType getMimeType() {
        return this.mimeType;
    }
    private void setMimeType(MediaType mimeType) {
        this.mimeType = mimeType;
    }

}
