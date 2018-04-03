package de.perdian.flightlog.support;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;

import org.apache.commons.lang3.StringUtils;

/**
 * Some general helper methods that do not fit anywhere else
 *
 * @author Christian Robert
 */

public class FlightlogHelper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static Integer computeDistanceInKilometers(Float longitudeA, Float latitudeA, Float longitudeB, Float latitudeB) {
        if (longitudeA == null || latitudeA == null || longitudeB == null || latitudeB == null) {
            return null;
        } else {
            double earthRadius = 6371000; // Meters
            double dLat = Math.toRadians(latitudeB - latitudeA);
            double dLng = Math.toRadians(longitudeB - longitudeA);
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(latitudeA)) * Math.cos(Math.toRadians(latitudeB)) * Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            return (int)((earthRadius * c) / 1000);
        }
    }

    public static Duration computeDuration(ZoneId departureZoneId, LocalDate departureDate, LocalTime departureTime, ZoneId arrivalZoneId, LocalDate arrivalDate, LocalTime arrivalTime) {
        if (departureZoneId == null || departureDate == null || departureTime == null) {
            return null;
        } else if (arrivalZoneId == null || arrivalDate == null || arrivalTime == null) {
            return null;
        } else {
            ZonedDateTime departureDateTime = departureTime.atDate(departureDate).atZone(departureZoneId);
            ZonedDateTime arrivalDateTime = arrivalTime.atDate(arrivalDate).atZone(arrivalZoneId);
            return Duration.between(departureDateTime, arrivalDateTime);
        }
    }

    public static Duration parseDuration(String flightDuration) {
        int separatorIndex = flightDuration == null ? -1 : flightDuration.indexOf(":");
        if (flightDuration != null && separatorIndex > 0) {
            int hours = Integer.parseInt(flightDuration.substring(0, separatorIndex));
            int minutes = Integer.parseInt(flightDuration.substring(separatorIndex + 1));
            return Duration.ofHours(hours).plusMinutes(minutes);
        } else {
            return null;
        }
    }

    public static String formatDuration(Duration duration) {
        Number durationInMinutes = duration == null ? null : duration.toMinutes();
        if (durationInMinutes == null || durationInMinutes.intValue() <= 0) {
            return "";
        } else {
            long hours = durationInMinutes.intValue() / 60;
            long minutes = durationInMinutes.intValue() % 60;
            StringBuilder result = new StringBuilder();
            result.append(hours < 10 ? "0" : "").append(hours);
            result.append(":");
            result.append(minutes < 10 ? "0" : "").append(minutes);
            return result.toString();
        }
    }

    public static String computeOffsetDays(LocalDate referenceDate, LocalDate compareDate) {
        if (referenceDate == null || compareDate == null) {
            return null;
        } else {
            long daysDifference = Math.abs(ChronoUnit.DAYS.between(referenceDate, compareDate));
            if (daysDifference == 0d) {
                return null;
            } else {
                int offsetValue = (int)(compareDate.isAfter(referenceDate) ? daysDifference : (-1 * Math.abs(daysDifference)));
                return offsetValue > 0 ? ("+" + offsetValue) : ("-" + offsetValue);
            }
        }
    }

    public static LocalDate parseLocalDate(String dateValue) {
        return StringUtils.isEmpty(dateValue) ? null : LocalDate.parse(dateValue, DATE_FORMATTER);
    }

    public static String formatDate(TemporalAccessor date) {
        return date == null ? null : DATE_FORMATTER.format(date);
    }

    public static LocalTime parseLocalTime(String timeValue) {
        return StringUtils.isEmpty(timeValue) ? null : LocalTime.parse(timeValue, TIME_FORMATTER);
    }

    public static String formatTime(TemporalAccessor time) {
        return time == null ? null : TIME_FORMATTER.format(time);
    }

    public static <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else {
            return Enum.valueOf(enumClass, value);
        }
    }

}
