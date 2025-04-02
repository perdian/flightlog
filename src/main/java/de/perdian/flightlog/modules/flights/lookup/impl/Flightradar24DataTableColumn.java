package de.perdian.flightlog.modules.flights.lookup.impl;

import org.apache.commons.lang3.StringUtils;

enum Flightradar24DataTableColumn {

    DATE("DATE"),
    FROM("FROM"),
    TO("TO"),
    AIRCRAFT("AIRCRAFT"),
    FLIGHT_TIME("FLIGHT TIME"),
    SCHEDULE_DEPARTURE_TIME("STD"),
    ACTUAL_DEPARTURE_TIME("ATD"),
    SCHEDULED_ARRIVAL_TIME("STA"),
    STATUS("STATUS");

    private String title = null;

    Flightradar24DataTableColumn(String title) {
        this.setTitle(title);
    }

    static Flightradar24DataTableColumn forColumnName(String headerColumnName) {
        for (Flightradar24DataTableColumn column : Flightradar24DataTableColumn.values()) {
            if (StringUtils.equalsIgnoreCase(column.getTitle(), headerColumnName)) {
                return column;
            }
        }
        return null;
    }

    String getTitle() {
        return this.title;
    }
    private void setTitle(String title) {
        this.title = title;
    }
}
