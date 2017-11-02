package de.perdian.apps.flighttracker.modules.importexport.data.impl;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

class FlugstatistikdeConstants {

    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    static final Pattern FLIGHT_NUMBER_PATTERN = Pattern.compile("\\s*(.{2})\\s*(.*?)");

}
