package de.perdian.flightlog.modules.flights.exchange.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangeHandler;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangePackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JsonExchangeHandler implements FlightsExchangeHandler {

    private ObjectMapper objectMapper = null;

    public JsonExchangeHandler() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.setObjectMapper(objectMapper);
    }

    @Override
    public FlightsExchangePackage importPackage(InputStream inputStream) throws IOException {
        return this.getObjectMapper().readValue(inputStream, FlightsExchangePackage.class);
    }

    @Override
    public void exportPackage(FlightsExchangePackage exchangePackage, OutputStream outputStream) throws IOException {
        this.getObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .writeValue(outputStream, exchangePackage);
    }

    ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }
    void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}
