package de.perdian.flightlog.modules.flights.exchange.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangeHandler;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangePackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class XmlExchangeHandler implements FlightsExchangeHandler {

    private XmlMapper xmlMapper = null;

    public XmlExchangeHandler() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.setXmlMapper(xmlMapper);
    }

    @Override
    public FlightsExchangePackage importPackage(InputStream inputStream) throws IOException {
        return this.getXmlMapper().readValue(inputStream, FlightsExchangePackage.class);
    }

    @Override
    public void exportPackage(FlightsExchangePackage exchangePackage, OutputStream outputStream) throws IOException {
        this.getXmlMapper()
            .writerWithDefaultPrettyPrinter()
            .withRootName("flights-exchange-package")
            .writeValue(outputStream, exchangePackage);
    }

    XmlMapper getXmlMapper() {
        return this.xmlMapper;
    }
    void setXmlMapper(XmlMapper xmlMapper) {
        this.xmlMapper = xmlMapper;
    }

}
