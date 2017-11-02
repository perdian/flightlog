package de.perdian.apps.flighttracker.modules.importexport.data.impl;

import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.perdian.apps.flighttracker.modules.importexport.data.DataItem;
import de.perdian.apps.flighttracker.modules.importexport.data.DataWriter;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;

public class JsonDataWriter implements DataWriter<String> {

    @Override
    public String writeDataItems(List<DataItem> dataItems) throws Exception {

        ArrayNode rootNode = JsonConstants.OBJECT_MAPPER.createArrayNode();

        for (DataItem dataItem : dataItems) {
            ObjectNode flightNode = rootNode.addObject();
            Optional.ofNullable(dataItem.getAircraftName()).ifPresent(value -> flightNode.put("aircraftName", value));
            Optional.ofNullable(dataItem.getAircraftRegistration()).ifPresent(value -> flightNode.put("aircraftRegistration", value));
            Optional.ofNullable(dataItem.getAircraftType()).ifPresent(value -> flightNode.put("aircraftType", value));
            Optional.ofNullable(dataItem.getAirlineCode()).ifPresent(value -> flightNode.put("airlineCode", value));
            Optional.ofNullable(dataItem.getAirlineName()).ifPresent(value -> flightNode.put("airlineName", value));
            Optional.ofNullable(dataItem.getArrivalAirportCode()).ifPresent(value -> flightNode.put("arrivalAirportCode", value));
            Optional.ofNullable(dataItem.getArrivalDateLocal()).ifPresent(value -> flightNode.put("arrivalDateLocal", FlighttrackerHelper.formatDate(value)));
            Optional.ofNullable(dataItem.getArrivalTimeLocal()).ifPresent(value -> flightNode.put("arrivalTimeLocal", FlighttrackerHelper.formatTime(value)));
            Optional.ofNullable(dataItem.getCabinClass()).ifPresent(value -> flightNode.put("cabinClass", value.name()));
            Optional.ofNullable(dataItem.getComment()).ifPresent(value -> flightNode.put("comment", value));
            Optional.ofNullable(dataItem.getDepartureAirportCode()).ifPresent(value -> flightNode.put("departureAirportCode", value));
            Optional.ofNullable(dataItem.getDepartureDateLocal()).ifPresent(value -> flightNode.put("departureDateLocal", FlighttrackerHelper.formatDate(value)));
            Optional.ofNullable(dataItem.getDepartureTimeLocal()).ifPresent(value -> flightNode.put("departureTimeLocal", FlighttrackerHelper.formatTime(value)));
            Optional.ofNullable(dataItem.getFlightDistance()).ifPresent(value -> flightNode.put("flightDistance", String.valueOf(value)));
            Optional.ofNullable(dataItem.getFlightDuration()).ifPresent(value -> flightNode.put("flightDuration", FlighttrackerHelper.formatDuration(value)));
            Optional.ofNullable(dataItem.getFlightNumber()).ifPresent(value -> flightNode.put("flightNumber", value));
            Optional.ofNullable(dataItem.getFlightReason()).ifPresent(value -> flightNode.put("flightReason", value.name()));
            Optional.ofNullable(dataItem.getSeatNumber()).ifPresent(value -> flightNode.put("seatNumber", value));
            Optional.ofNullable(dataItem.getSeatType()).ifPresent(value -> flightNode.put("seatType", value.name()));
        }

        StringWriter stringWriter = new StringWriter();
        JsonConstants.OBJECT_MAPPER.writeValue(stringWriter, rootNode);
        return stringWriter.toString();

    }

}
