package de.perdian.apps.flighttracker.modules.importexport.data.impl;

import java.io.BufferedReader;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.perdian.apps.flighttracker.modules.importexport.data.DataItem;
import de.perdian.apps.flighttracker.modules.importexport.data.DataLoader;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

public class OpenflightsCsvDataLoader implements DataLoader<Reader> {

    private static final Logger log = LoggerFactory.getLogger(OpenflightsCsvDataLoader.class);

    @Override
    public List<DataItem> loadDataItems(Reader reader) throws Exception {
        try (BufferedReader in = new BufferedReader(reader)) {

            // Skip the first line as this is the header that only lists the fields
            Map<Integer, String> fieldNameMap = new HashMap<>();
            StringTokenizer headerTokenizer = new StringTokenizer(in.readLine(), String.valueOf(OpenflightsConstants.FIELD_SEPARATOR));
            for (int i=0; headerTokenizer.hasMoreTokens(); i++) {
                fieldNameMap.put(Integer.valueOf(i), headerTokenizer.nextToken().trim().replace("\uFEFF", ""));
            }

            List<DataItem> resultList = new ArrayList<>();
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                if (!line.trim().isEmpty()) {
                    Map<String, String> lineFieldValues = this.loadFlightFieldsFromLine(line, fieldNameMap);
                    DataItem flightBean = this.loadFlightFromFieldValues(lineFieldValues);
                    if (flightBean != null) {
                        log.debug("Imported flight: {}", flightBean);
                        resultList.add(flightBean);
                    }
                }
            }
            return resultList;

        }
    }

    private DataItem loadFlightFromFieldValues(Map<String, String> fieldValues) {
        DataItem flight = new DataItem();

        String departureDateValue = fieldValues.get("Date");
        try {
            LocalDateTime departureDateTime = LocalDateTime.parse(departureDateValue, OpenflightsConstants.DATETIME_FORMATTER);
            flight.setDepartureDateLocal(departureDateTime.toLocalDate());
            flight.setDepartureTimeLocal(departureDateTime.toLocalTime());
        } catch (DateTimeParseException e) {
            flight.setDepartureDateLocal(LocalDate.parse(departureDateValue, OpenflightsConstants.DATE_FORMATTER));
        }

        flight.setDepartureAirportCode(fieldValues.get("From"));
        flight.setArrivalAirportCode(fieldValues.get("To"));

        String flightNumberValue = fieldValues.get("Flight_Number");
        Matcher flightNumberMatcher = flightNumberValue == null ? null : OpenflightsConstants.FLIGHT_NUMBER_PATTERN.matcher(flightNumberValue);
        if (flightNumberMatcher != null && flightNumberMatcher.matches()) {
            flight.setAirlineCode(flightNumberMatcher.group(1));
            flight.setFlightNumber(flightNumberMatcher.group(2));
        }
        flight.setAirlineName(fieldValues.get("AirlineData"));

        String flightDistancesMilesValue = fieldValues.get("Distance");
        if (flightDistancesMilesValue != null && !flightDistancesMilesValue.isEmpty()) {
            int flightDistanceMiles = Integer.parseInt(flightDistancesMilesValue);
            int flightDistanceKm = (int)(0.621371d * flightDistanceMiles);
            flight.setFlightDistance(Integer.valueOf(flightDistanceKm));
        }

        String durationValue = fieldValues.get("Duration");
        if (durationValue != null && !durationValue.isEmpty()) {
            int durationSeparatorIndex = durationValue.indexOf(":");
            int durationHours = Integer.parseInt(durationValue.substring(0, durationSeparatorIndex), 10);
            int durationMinutes = Integer.parseInt(durationValue.substring(durationSeparatorIndex + 1), 10);
            Duration duration = Duration.ofHours(durationHours).plusMinutes(durationMinutes);
            flight.setFlightDuration(duration);
        }

        flight.setSeatNumber(fieldValues.get("Seat"));
        flight.setSeatType(this.computeSeatType(fieldValues.get("Seat_Type")));
        flight.setCabinClass(this.computeCabinClass(fieldValues.get("Class")));
        flight.setFlightReason(this.computeFlightReason(fieldValues.get("Reason")));
        flight.setAircraftType(fieldValues.get("Plane"));
        flight.setAircraftRegistration(fieldValues.get("Registration"));
        flight.setComment(fieldValues.get("Note"));

        return flight;
    }

    private FlightReason computeFlightReason(String flightReason) {
        if ("L".equalsIgnoreCase(flightReason)) {
            return FlightReason.PRIVATE;
        } else if ("B".equalsIgnoreCase(flightReason)) {
            return FlightReason.BUSINESS;
        } else if ("C".equalsIgnoreCase(flightReason)) {
            return FlightReason.CREW;
        } else if ("O".equalsIgnoreCase(flightReason)) {
            return null;
        } else {
            return null;
        }
    }

    private SeatType computeSeatType(String seatType) {
        if ("A".equalsIgnoreCase(seatType)) {
            return SeatType.AISLE;
        } else if ("M".equalsIgnoreCase(seatType)) {
            return SeatType.MIDDLE;
        } else if ("W".equalsIgnoreCase(seatType)) {
            return SeatType.WINDOW;
        } else {
            return null;
        }
    }

    private CabinClass computeCabinClass(String cabinClass) {
        if ("Y".equalsIgnoreCase(cabinClass)) {
            return CabinClass.ECONOMY;
        } else if ("P".equalsIgnoreCase(cabinClass)) {
            return CabinClass.PREMIUM_ECONOMY;
        } else if ("C".equalsIgnoreCase(cabinClass)) {
            return CabinClass.BUSINESS;
        } else if ("F".equalsIgnoreCase(cabinClass)) {
            return CabinClass.FIRST;
        } else {
            return null;
        }
    }

    private Map<String, String> loadFlightFieldsFromLine(String line, Map<Integer, String> fieldNameMap) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        List<String> lineValues = OpenflightsHelper.tokenizeLine(line);
        for (int i=0; i < lineValues.size(); i++) {
            String fieldName = fieldNameMap.get(Integer.valueOf(i));
            if (fieldName != null) {
                resultMap.put(fieldName, lineValues.get(i));
            } else {
                log.warn("No field name declared for index {} from line: {}", i, line);
            }
        }
        return resultMap;
    }

}
