package de.perdian.apps.flighttracker.business.modules.importexport.data.impl;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

class OpenflightsConstants {

    static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    static final char FIELD_SEPARATOR = ',';
    static final char FIELD_DELIMITER = '\"';
    static final Pattern FLIGHT_NUMBER_PATTERN = Pattern.compile("\\s*(.{2})\\s*(.*?)");

}
