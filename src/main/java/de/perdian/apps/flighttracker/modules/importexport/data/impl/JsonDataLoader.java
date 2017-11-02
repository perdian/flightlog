package de.perdian.apps.flighttracker.modules.importexport.data.impl;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import de.perdian.apps.flighttracker.modules.importexport.data.DataItem;
import de.perdian.apps.flighttracker.modules.importexport.data.DataLoader;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

public class JsonDataLoader implements DataLoader<Reader> {

    @Override
    public List<DataItem> loadDataItems(Reader source) throws Exception {

        JsonNode rootNode = JsonConstants.OBJECT_MAPPER.reader().readTree(source);
        List<DataItem> dataItems = new ArrayList<>(rootNode.size());
        for (int i=0; i < rootNode.size(); i++) {

            JsonNode flightNode = rootNode.get(i);
            DataItem dataItem = new DataItem();

            dataItem.setAircraftName(JsonDataLoader.getStringFromNode(flightNode, "aircraftName"));
            dataItem.setAircraftRegistration(JsonDataLoader.getStringFromNode(flightNode, "aircraftRegistration"));
            dataItem.setAircraftType(JsonDataLoader.getStringFromNode(flightNode, "aircraftType"));
            dataItem.setAirlineCode(JsonDataLoader.getStringFromNode(flightNode, "airlineCode"));
            dataItem.setAirlineName(JsonDataLoader.getStringFromNode(flightNode, "airlineName"));
            dataItem.setArrivalAirportCode(JsonDataLoader.getStringFromNode(flightNode, "arrivalAirportCode"));
            dataItem.setArrivalDateLocal(FlighttrackerHelper.parseLocalDate(JsonDataLoader.getStringFromNode(flightNode, "arrivalDateLocal")));
            dataItem.setArrivalTimeLocal(FlighttrackerHelper.parseLocalTime(JsonDataLoader.getStringFromNode(flightNode, "arrivalTimeLocal")));
            dataItem.setCabinClass(FlighttrackerHelper.parseEnum(CabinClass.class, JsonDataLoader.getStringFromNode(flightNode, "cabinClass")));
            dataItem.setComment(JsonDataLoader.getStringFromNode(flightNode, "comment"));
            dataItem.setDepartureAirportCode(JsonDataLoader.getStringFromNode(flightNode, "departureAirportCode"));
            dataItem.setDepartureDateLocal(FlighttrackerHelper.parseLocalDate(JsonDataLoader.getStringFromNode(flightNode, "arrivalDateLocal")));
            dataItem.setDepartureTimeLocal(FlighttrackerHelper.parseLocalTime(JsonDataLoader.getStringFromNode(flightNode, "arrivalTimeLocal")));
            dataItem.setFlightDistance(StringUtils.isEmpty(JsonDataLoader.getStringFromNode(flightNode, "flightDistance")) ? null : Integer.valueOf(JsonDataLoader.getStringFromNode(flightNode, "flightDistance")));
            dataItem.setFlightDuration(FlighttrackerHelper.parseDuration(JsonDataLoader.getStringFromNode(flightNode, "flightDuration")));
            dataItem.setFlightNumber(JsonDataLoader.getStringFromNode(flightNode, "flightNumber"));
            dataItem.setFlightReason(FlighttrackerHelper.parseEnum(FlightReason.class, JsonDataLoader.getStringFromNode(flightNode, "flightReason")));
            dataItem.setSeatNumber(JsonDataLoader.getStringFromNode(flightNode, "seatNumber"));
            dataItem.setSeatType(FlighttrackerHelper.parseEnum(SeatType.class, JsonDataLoader.getStringFromNode(flightNode, "seatType")));
            dataItems.add(dataItem);

        }
        return dataItems;

    }

    private static String getStringFromNode(JsonNode node, String propertyName) {
        return Optional.ofNullable(node.get(propertyName)).map(JsonNode::textValue).orElse(null);
    }

}
