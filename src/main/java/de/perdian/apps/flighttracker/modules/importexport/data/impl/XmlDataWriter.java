package de.perdian.apps.flighttracker.modules.importexport.data.impl;

import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import de.perdian.apps.flighttracker.modules.importexport.data.DataItem;
import de.perdian.apps.flighttracker.modules.importexport.data.DataWriter;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;

public class XmlDataWriter implements DataWriter<String> {

    @Override
    public String writeDataItems(List<DataItem> dataItems) throws Exception {

        DocumentFactory documentFactory = DocumentFactory.getInstance();
        Element rootElement = documentFactory.createElement("flights");
        for (DataItem dataItem : dataItems) {
            Element flightElement = rootElement.addElement("flight");
            Optional.ofNullable(dataItem.getAircraftName()).ifPresent(value -> flightElement.addElement("aircraftName").setText(value));
            Optional.ofNullable(dataItem.getAircraftRegistration()).ifPresent(value -> flightElement.addElement("aircraftRegistration").setText(value));
            Optional.ofNullable(dataItem.getAircraftType()).ifPresent(value -> flightElement.addElement("aircraftType").setText(value));
            Optional.ofNullable(dataItem.getAirlineCode()).ifPresent(value -> flightElement.addElement("airlineCode").setText(value));
            Optional.ofNullable(dataItem.getAirlineName()).ifPresent(value -> flightElement.addElement("airlineName").setText(value));
            Optional.ofNullable(dataItem.getArrivalAirportCode()).ifPresent(value -> flightElement.addElement("arrivalAirportCode").setText(value));
            Optional.ofNullable(dataItem.getArrivalDateLocal()).ifPresent(value -> flightElement.addElement("arrivalDateLocal").setText(FlighttrackerHelper.formatDate(value)));
            Optional.ofNullable(dataItem.getArrivalTimeLocal()).ifPresent(value -> flightElement.addElement("arrivalTimeLocal").setText(FlighttrackerHelper.formatTime(value)));
            Optional.ofNullable(dataItem.getCabinClass()).ifPresent(value -> flightElement.addElement("cabinClass").setText(value.name()));
            Optional.ofNullable(dataItem.getComment()).ifPresent(value -> flightElement.addElement("comment").setText(value));
            Optional.ofNullable(dataItem.getDepartureAirportCode()).ifPresent(value -> flightElement.addElement("departureAirportCode").setText(value));
            Optional.ofNullable(dataItem.getDepartureDateLocal()).ifPresent(value -> flightElement.addElement("departureDateLocal").setText(FlighttrackerHelper.formatDate(value)));
            Optional.ofNullable(dataItem.getDepartureTimeLocal()).ifPresent(value -> flightElement.addElement("departureTimeLocal").setText(FlighttrackerHelper.formatTime(value)));
            Optional.ofNullable(dataItem.getFlightDistance()).ifPresent(value -> flightElement.addElement("flightDistance").setText(String.valueOf(value)));
            Optional.ofNullable(dataItem.getFlightDuration()).ifPresent(value -> flightElement.addElement("flightDuration").setText(FlighttrackerHelper.formatDuration(value)));
            Optional.ofNullable(dataItem.getFlightNumber()).ifPresent(value -> flightElement.addElement("flightNumber").setText(value));
            Optional.ofNullable(dataItem.getFlightReason()).ifPresent(value -> flightElement.addElement("flightReason").setText(value.name()));
            Optional.ofNullable(dataItem.getSeatNumber()).ifPresent(value -> flightElement.addElement("seatNumber").setText(value));
            Optional.ofNullable(dataItem.getSeatType()).ifPresent(value -> flightElement.addElement("seatType").setText(value.name()));
        }

        Document document = documentFactory.createDocument(rootElement);
        StringWriter stringWriter = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(stringWriter, OutputFormat.createPrettyPrint());
        xmlWriter.write(document);
        xmlWriter.flush();
        return stringWriter.toString();

    }

}
