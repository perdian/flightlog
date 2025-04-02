package de.perdian.flightlog.modules.flights.lookup.impl;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

class Flightradar24DataTable {

    private static final Logger log = LoggerFactory.getLogger(Flightradar24DataTable.class);

    private Map<Flightradar24DataTableColumn, Integer> columnsToIndexMap = null;
    private Elements rowElements = null;

    Flightradar24DataTable(Element tableElement) {
        Map<Flightradar24DataTableColumn, Integer> columnsToIndexMap = new EnumMap<>(Flightradar24DataTableColumn.class);
        Element headerElement = tableElement.select("thead").first();
        Elements headerColumnElements = headerElement.select("th");
        for (int columnIndex = 0; columnIndex < headerColumnElements.size(); columnIndex++) {
            Element headerColumnElement = headerColumnElements.get(columnIndex);
            String headerColumnName = headerColumnElement.text().strip();
            Flightradar24DataTableColumn headerColumn = Flightradar24DataTableColumn.forColumnName(headerColumnName);
            if (headerColumn != null) {
                columnsToIndexMap.put(headerColumn, columnIndex);
            }
        }
        this.setColumnsToIndexMap(columnsToIndexMap);
        this.setRowElements(tableElement.select("tbody tr"));
    }

    List<Flightradar24DataFlight> extractFlights() {
        return this.getRowElements().stream()
            .map(this::extractFlightForRow)
            .filter(Objects::nonNull)
            .toList();
    }

    private Flightradar24DataFlight extractFlightForRow(Element rowElement) {
        Instant actualDepartureTime = this.extractTimeValueForColumn(Flightradar24DataTableColumn.ACTUAL_DEPARTURE_TIME, rowElement);
        Instant actualArrivalTime = this.extractTimeValueForColumn(Flightradar24DataTableColumn.STATUS, rowElement);
        if (actualDepartureTime == null || actualArrivalTime == null) {
            return null;
        } else {
            Flightradar24DataFlight resultFlight = new Flightradar24DataFlight();
            resultFlight.setDepartureAirportCode(this.extractAirportCodeValueForColumn(Flightradar24DataTableColumn.FROM, rowElement));
            resultFlight.setDepartureTime(actualDepartureTime);
            resultFlight.setArrivalAirportCode(this.extractAirportCodeValueForColumn(Flightradar24DataTableColumn.TO, rowElement));
            resultFlight.setArrivalTime(actualArrivalTime);
            resultFlight.setAircraftTypeCode(this.extractAircraftCodeValueForColumn(Flightradar24DataTableColumn.AIRCRAFT, rowElement));
            resultFlight.setAircraftRegistration(this.extractAircraftRegistrationValueForColumn(Flightradar24DataTableColumn.AIRCRAFT, rowElement));
            return resultFlight;
        }
    }

    private String extractAirportCodeValueForColumn(Flightradar24DataTableColumn column, Element rowElement) {
        return this.extractValueForColumn(column, rowElement, cellElement -> {
            Element airportElement = cellElement.selectFirst("a");
            String airportCodeString = airportElement == null ? null : airportElement.text().strip();
            if (StringUtils.isEmpty(airportCodeString)) {
                return null;
            } else if (airportCodeString.startsWith("(") && airportCodeString.endsWith(")")) {
                return airportCodeString.substring(1, airportCodeString.length() - 1);
            } else {
                return null;
            }
        });
    }

    private Instant extractTimeValueForColumn(Flightradar24DataTableColumn column, Element rowElement) {
        return this.extractValueForColumn(column, rowElement, cellElement -> {
            String dataTimestampValue = cellElement == null ? null : cellElement.attr("data-timestamp");
            if (StringUtils.isEmpty(dataTimestampValue)) {
                return null;
            } else {
                return Instant.ofEpochSecond(Long.parseLong(dataTimestampValue));
            }
        });
    }

    private String extractAircraftCodeValueForColumn(Flightradar24DataTableColumn column, Element rowElement) {
        return this.extractValueForColumn(column, rowElement, cellElement -> cellElement.ownText().strip());
    }

    private String extractAircraftRegistrationValueForColumn(Flightradar24DataTableColumn column, Element rowElement) {
        return this.extractValueForColumn(column, rowElement, cellElement -> {
            Element aircraftRegistrationElement = cellElement.selectFirst("a");
            String aircraftRegistrationString = aircraftRegistrationElement == null ? null : aircraftRegistrationElement.text().strip();
            if (StringUtils.isEmpty(aircraftRegistrationString)) {
                return null;
            } else if (aircraftRegistrationString.startsWith("(") && aircraftRegistrationString.endsWith(")")) {
                return aircraftRegistrationString.substring(1, aircraftRegistrationString.length() - 1);
            } else {
                return null;
            }
        });
    }

    private <T> T extractValueForColumn(Flightradar24DataTableColumn column, Element rowElement, Function<Element, T> cellToValueFunction) {
        Integer columnIndex = this.getColumnsToIndexMap().get(column);
        if (columnIndex == null) {
            log.warn("Cannot resolve flightradar24 column index for requested column: {}", column);
            return null;
        } else {
            Element cellElement = rowElement.select("td").get(columnIndex);
            return cellElement == null ? null : cellToValueFunction.apply(cellElement);
        }
    }

    private Map<Flightradar24DataTableColumn, Integer> getColumnsToIndexMap() {
        return this.columnsToIndexMap;
    }
    private void setColumnsToIndexMap(Map<Flightradar24DataTableColumn, Integer> columnsToIndexMap) {
        this.columnsToIndexMap = columnsToIndexMap;
    }

    private Elements getRowElements() {
        return this.rowElements;
    }
    private void setRowElements(Elements rowElements) {
        this.rowElements = rowElements;
    }

}
