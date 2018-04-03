package de.perdian.flightlog.modules.importexport.data.impl;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import de.perdian.flightlog.modules.importexport.data.DataItem;
import de.perdian.flightlog.modules.importexport.data.DataLoader;
import de.perdian.flightlog.support.FlightlogHelper;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;

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
            dataItem.setArrivalDateLocal(FlightlogHelper.parseLocalDate(JsonDataLoader.getStringFromNode(flightNode, "arrivalDateLocal")));
            dataItem.setArrivalTimeLocal(FlightlogHelper.parseLocalTime(JsonDataLoader.getStringFromNode(flightNode, "arrivalTimeLocal")));
            dataItem.setCabinClass(FlightlogHelper.parseEnum(CabinClass.class, JsonDataLoader.getStringFromNode(flightNode, "cabinClass")));
            dataItem.setComment(JsonDataLoader.getStringFromNode(flightNode, "comment"));
            dataItem.setDepartureAirportCode(JsonDataLoader.getStringFromNode(flightNode, "departureAirportCode"));
            dataItem.setDepartureDateLocal(FlightlogHelper.parseLocalDate(JsonDataLoader.getStringFromNode(flightNode, "arrivalDateLocal")));
            dataItem.setDepartureTimeLocal(FlightlogHelper.parseLocalTime(JsonDataLoader.getStringFromNode(flightNode, "arrivalTimeLocal")));
            dataItem.setFlightDistance(StringUtils.isEmpty(JsonDataLoader.getStringFromNode(flightNode, "flightDistance")) ? null : Integer.valueOf(JsonDataLoader.getStringFromNode(flightNode, "flightDistance")));
            dataItem.setFlightDuration(FlightlogHelper.parseDuration(JsonDataLoader.getStringFromNode(flightNode, "flightDuration")));
            dataItem.setFlightNumber(JsonDataLoader.getStringFromNode(flightNode, "flightNumber"));
            dataItem.setFlightReason(FlightlogHelper.parseEnum(FlightReason.class, JsonDataLoader.getStringFromNode(flightNode, "flightReason")));
            dataItem.setSeatNumber(JsonDataLoader.getStringFromNode(flightNode, "seatNumber"));
            dataItem.setSeatType(FlightlogHelper.parseEnum(SeatType.class, JsonDataLoader.getStringFromNode(flightNode, "seatType")));
            dataItems.add(dataItem);

        }
        return dataItems;

    }

    private static String getStringFromNode(JsonNode node, String propertyName) {
        return Optional.ofNullable(node.get(propertyName)).map(JsonNode::textValue).orElse(null);
    }

}
