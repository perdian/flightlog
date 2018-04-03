package de.perdian.flightlog.modules.importexport.data.impl;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.perdian.flightlog.modules.importexport.data.DataItem;
import de.perdian.flightlog.modules.importexport.data.DataLoader;
import de.perdian.flightlog.support.FlightlogHelper;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;

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
            dataItem.setArrivalDateLocal(FlightlogHelper.parseLocalDate(flightElement.elementText("arrivalDateLocal")));
            dataItem.setArrivalTimeLocal(FlightlogHelper.parseLocalTime(flightElement.elementText("arrivalTimeLocal")));
            dataItem.setCabinClass(FlightlogHelper.parseEnum(CabinClass.class, flightElement.elementText("cabinClass")));
            dataItem.setComment(flightElement.elementText("comment"));
            dataItem.setDepartureAirportCode(flightElement.elementText("departureAirportCode"));
            dataItem.setDepartureDateLocal(FlightlogHelper.parseLocalDate(flightElement.elementText("departureDateLocal")));
            dataItem.setDepartureTimeLocal(FlightlogHelper.parseLocalTime(flightElement.elementText("departureTimeLocal")));
            dataItem.setFlightDistance(StringUtils.isEmpty(flightElement.elementText("flightDistance")) ? null : Integer.valueOf(flightElement.elementText("flightDistance")));
            dataItem.setFlightDuration(FlightlogHelper.parseDuration(flightElement.elementText("flightDuration")));
            dataItem.setFlightNumber(flightElement.elementText("flightNumber"));
            dataItem.setFlightReason(FlightlogHelper.parseEnum(FlightReason.class, flightElement.elementText("flightReason")));
            dataItem.setSeatNumber(flightElement.elementText("seatNumber"));
            dataItem.setSeatType(FlightlogHelper.parseEnum(SeatType.class, flightElement.elementText("seatType")));
            dataItems.add(dataItem);

        }
        return dataItems;

    }

}
