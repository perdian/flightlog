package de.perdian.flightlog.modules.flights.exchange;

import de.perdian.flightlog.modules.flights.exchange.impl.JsonExchangeHandler;
import de.perdian.flightlog.modules.flights.exchange.impl.XmlExchangeHandler;

public enum FlightsExchangeFormat {

    XML(new XmlExchangeHandler()),
    JSON(new JsonExchangeHandler());

    private FlightsExchangeHandler handler = null;

    FlightsExchangeFormat(FlightsExchangeHandler handler) {
        this.setHandler(handler);
    }

    public FlightsExchangeHandler getHandler() {
        return this.handler;
    }
    private void setHandler(FlightsExchangeHandler handler) {
        this.handler = handler;
    }

}
