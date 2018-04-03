package de.perdian.flightlog.modules.importexport.data.impl;

import java.util.List;

import de.perdian.flightlog.modules.importexport.data.DataItem;
import de.perdian.flightlog.modules.importexport.data.DataWriter;
import de.perdian.flightlog.support.FlightlogHelper;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;

public class OpenflightsCsvDataWriter implements DataWriter<String> {

    @Override
    public String writeDataItems(List<DataItem> dataItems) throws Exception {

        StringBuilder out = new StringBuilder();

        out.append("Date,From,To,Flight_Number,AirlineData,Distance,Duration,Seat,Seat_Type,Class,Reason,Plane,Registration,Trip,Note");
        out.append("\r\n");

        for (DataItem dataItem : dataItems) {

            out.append(this.computeDateTime(dataItem));
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(dataItem.getDepartureAirportCode());
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(dataItem.getArrivalAirportCode());
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append((dataItem.getAirlineCode()) + " " + dataItem.getFlightNumber().trim());
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(this.escape(dataItem.getAirlineName()));
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(dataItem.getFlightDistance() == null ? "" : ((int)(dataItem.getFlightDistance() * 0.621371))); // km to miles
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(FlightlogHelper.formatDuration(dataItem.getFlightDuration()));
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(this.escape(dataItem.getSeatNumber()));
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(this.computeSeatType(dataItem));
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(this.computeClass(dataItem));
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(this.computeReason(dataItem));
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(this.escape(dataItem.getAircraftType()));
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(this.escape(dataItem.getAircraftRegistration()));
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(""); // Trip
            out.append(OpenflightsConstants.FIELD_SEPARATOR);
            out.append(this.escape(dataItem.getComment()));
            out.append("\r\n");

        }
        return out.toString();

    }

    private String computeDateTime(DataItem dataItem) {
        StringBuilder out = new StringBuilder();
        out.append(OpenflightsConstants.DATE_FORMATTER.format(dataItem.getDepartureDateLocal()));
        if (dataItem.getDepartureTimeLocal() != null) {
            out.append(" ").append(OpenflightsConstants.TIME_FORMATTER.format(dataItem.getDepartureTimeLocal()));
        }
        return out.toString();
    }

    private String computeSeatType(DataItem dataItem) {
        if (SeatType.AISLE.equals(dataItem.getSeatType())) {
            return "A";
        } else if (SeatType.MIDDLE.equals(dataItem.getSeatType())) {
            return "M";
        } else if (SeatType.WINDOW.equals(dataItem.getSeatType())) {
            return "W";
        } else {
            return "";
        }
    }

    private String computeReason(DataItem dataItem) {
        if (FlightReason.PRIVATE.equals(dataItem.getFlightReason())) {
            return "L";
        } else if (FlightReason.BUSINESS.equals(dataItem.getFlightReason())) {
            return "B";
        } else if (FlightReason.CREW.equals(dataItem.getFlightReason())) {
            return "C";
        } else {
            return "O";
        }
    }

    private String computeClass(DataItem dataItem) {
        if (CabinClass.ECONOMY.equals(dataItem.getCabinClass())) {
            return "Y";
        } else if (CabinClass.PREMIUM_ECONOMY.equals(dataItem.getCabinClass())) {
            return "P";
        } else if (CabinClass.BUSINESS.equals(dataItem.getCabinClass())) {
            return "C";
        } else if (CabinClass.FIRST.equals(dataItem.getCabinClass())) {
            return "F";
        } else {
            return "";
        }
    }

    private String escape(Object object) {
        if (object == null) {
            return "";
        } else {
            boolean escaped = false;
            String text = object.toString();
            StringBuilder result = new StringBuilder(text.length());
            for (char c : text.toCharArray()) {
                if (c == OpenflightsConstants.FIELD_SEPARATOR || c == OpenflightsConstants.FIELD_DELIMITER) {
                    escaped = true;
                    result.append("\\").append(c);
                } else if (c == '\r') {
                    // Ignore
                } else if (c == '\n') {
                    escaped = true;
                    result.append(" ");
                } else {
                    if (Character.isWhitespace(c)) {
                        escaped = true;
                    }
                    result.append(c);
                }
            }
            if (escaped) {
                result.insert(0, OpenflightsConstants.FIELD_DELIMITER).append(OpenflightsConstants.FIELD_DELIMITER);
            }
            return result.toString().trim();
        }
    }

}
