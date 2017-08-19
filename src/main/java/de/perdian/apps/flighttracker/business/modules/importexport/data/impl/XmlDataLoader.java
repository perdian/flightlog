package de.perdian.apps.flighttracker.business.modules.importexport.data.impl;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.perdian.apps.flighttracker.business.modules.importexport.data.DataItem;
import de.perdian.apps.flighttracker.business.modules.importexport.data.DataLoader;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

public class XmlDataLoader implements DataLoader<Reader> {

    @Override
    public List<DataItem> loadDataItems(Reader source) throws Exception {

        SAXReader saxReader = new SAXReader();
        Element rootElement = saxReader.read(source).getRootElement();
        List<?> rootElementChildren = rootElement.elements("flight");

        List<DataItem> dataItems = new ArrayList<>();
        for (int i=0; i < rootElementChildren.size(); i++) {

            Element flightElement = (Element)rootElementChildren.get(i);
            DataItem dataItem = new DataItem();

            dataItem.setAircraftName(flightElement.elementText("aircraftName"));
            dataItem.setAircraftRegistration(flightElement.elementText("aircraftRegistration"));
            dataItem.setAircraftType(flightElement.elementText("aircraftType"));
            dataItem.setAirlineCode(flightElement.elementText("airlineCode"));
            dataItem.setAirlineName(flightElement.elementText("airlineName"));
            dataItem.setArrivalAirportCode(flightElement.elementText("arrivalAirportCode"));
            dataItem.setArrivalDateLocal(FlighttrackerHelper.parseLocalDate(flightElement.elementText("arrivalDateLocal")));
            dataItem.setArrivalTimeLocal(FlighttrackerHelper.parseLocalTime(flightElement.elementText("arrivalTimeLocal")));
            dataItem.setCabinClass(FlighttrackerHelper.parseEnum(CabinClass.class, flightElement.elementText("cabinClass")));
            dataItem.setComment(flightElement.elementText("comment"));
            dataItem.setDepartureAirportCode(flightElement.elementText("departureAirportCode"));
            dataItem.setDepartureDateLocal(FlighttrackerHelper.parseLocalDate(flightElement.elementText("arrivalDateLocal")));
            dataItem.setDepartureTimeLocal(FlighttrackerHelper.parseLocalTime(flightElement.elementText("arrivalTimeLocal")));
            dataItem.setFlightDistance(StringUtils.isEmpty(flightElement.elementText("flightDistance")) ? null : Integer.valueOf(flightElement.elementText("flightDistance")));
            dataItem.setFlightDuration(FlighttrackerHelper.parseDuration(flightElement.elementText("flightDuration")));
            dataItem.setFlightNumber(flightElement.elementText("flightNumber"));
            dataItem.setFlightReason(FlighttrackerHelper.parseEnum(FlightReason.class, flightElement.elementText("flightReason")));
            dataItem.setSeatNumber(flightElement.elementText("seatNumber"));
            dataItem.setSeatType(FlighttrackerHelper.parseEnum(SeatType.class, flightElement.elementText("seatType")));
            dataItems.add(dataItem);

        }
        return dataItems;

    }

}
