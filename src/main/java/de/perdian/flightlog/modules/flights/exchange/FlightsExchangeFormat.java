package de.perdian.flightlog.modules.flights.exchange;

import de.perdian.flightlog.modules.flights.exchange.impl.JsonExchangeHandler;
import de.perdian.flightlog.modules.flights.exchange.impl.XmlExchangeHandler;
import org.springframework.http.MediaType;

public enum FlightsExchangeFormat {

    XML(new XmlExchangeHandler(), MediaType.APPLICATION_XML),
    JSON(new JsonExchangeHandler(), MediaType.APPLICATION_JSON);

    private FlightsExchangeHandler handler = null;
    private MediaType mimeType = null;

    FlightsExchangeFormat(FlightsExchangeHandler handler, MediaType mimeType) {
        this.setHandler(handler);
        this.setMimeType(mimeType);
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
