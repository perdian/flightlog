package de.perdian.flightlog.modules.flights.exchange.impl;

import de.perdian.flightlog.modules.flights.exchange.FlightsExchangeFormat;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangePackage;
import de.perdian.flightlog.modules.flights.exchange.FlightsExchangePackageFlight;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;
import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.hamcrest.text.IsEmptyString;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ExchangeHandlerTest {

    @ParameterizedTest
    @EnumSource(FlightsExchangeFormat.class)
    void importPackageFromHandler(FlightsExchangeFormat format) throws IOException {
        URL flightsResourceUrl = this.getClass().getResource("flights." + format.name().toLowerCase());
        try (InputStream flightsResourceStream = flightsResourceUrl.openStream()) {
            FlightsExchangePackage importedPackage = format.getHandler().importPackage(flightsResourceStream);
            this.validatePackage(importedPackage);
        }
    }

    @ParameterizedTest
    @EnumSource(FlightsExchangeFormat.class)
    void exportPackageFromHandler(FlightsExchangeFormat format) throws IOException {

        FlightsExchangePackageFlight firstFlight = new FlightsExchangePackageFlight();
        firstFlight.setAircraftName("Johannesburg");
        firstFlight.setAircraftRegistration("D-AIME");
        firstFlight.setAircraftType("Airbus A380-800");
        firstFlight.setAirlineCode("LH");
        firstFlight.setAirlineName("Lufthansa");
        firstFlight.setArrivalAirportCode("FRA");
        firstFlight.setArrivalDateLocal(LocalDate.of(2015, 5, 8));
        firstFlight.setArrivalTimeLocal(LocalTime.of(7, 15));
        firstFlight.setCabinClass(CabinClass.PREMIUM_ECONOMY);
        firstFlight.setComment("Comment X");
        firstFlight.setDepartureAirportCode("DEL");
        firstFlight.setDepartureDateLocal(LocalDate.of(2015, 5, 8));
        firstFlight.setDepartureTimeLocal(LocalTime.of(3, 0));
        firstFlight.setFlightDistance(6120);
        firstFlight.setFlightDuration("07:45");
        firstFlight.setFlightNumber("761");
        firstFlight.setFlightReason(FlightReason.BUSINESS);
        firstFlight.setSeatNumber("54A");
        firstFlight.setSeatType(SeatType.WINDOW);

        FlightsExchangePackageFlight secondFlight = new FlightsExchangePackageFlight();
        secondFlight.setAircraftType("Boeing 737-700");
        secondFlight.setAirlineCode("AB");
        secondFlight.setAirlineName("Air Berlin");
        secondFlight.setArrivalAirportCode("TXL");
        secondFlight.setArrivalDateLocal(LocalDate.of(2015, 5, 14));
        secondFlight.setCabinClass(CabinClass.ECONOMY);
        secondFlight.setDepartureAirportCode("CGN");
        secondFlight.setDepartureDateLocal(LocalDate.of(2015, 5, 14));
        secondFlight.setFlightNumber("6508");
        secondFlight.setFlightReason(FlightReason.PRIVATE);

        FlightsExchangePackage exchangePackage = new FlightsExchangePackage();
        exchangePackage.setFlights(List.of(firstFlight, secondFlight));

        try (ByteArrayOutputStream exchangeStream = new ByteArrayOutputStream()) {

            format.getHandler().exportPackage(exchangePackage, exchangeStream);
            byte[] exchangePackageBytes = exchangeStream.toByteArray();

            // Now we make sure that the output output contains exactly what we want put in
            FlightsExchangePackage reimportedPackage = format.getHandler().importPackage(new ByteArrayInputStream(exchangePackageBytes));
            this.validatePackage(reimportedPackage);

        }

    }

    private void validatePackage(FlightsExchangePackage exchangePackage) {

        MatcherAssert.assertThat(exchangePackage.getFlights(), IsCollectionWithSize.hasSize(2));

        FlightsExchangePackageFlight firstFlight = exchangePackage.getFlights().get(0);
        MatcherAssert.assertThat(firstFlight.getAircraftName(), IsEqual.equalTo("Johannesburg"));
        MatcherAssert.assertThat(firstFlight.getAircraftRegistration(), IsEqual.equalTo("D-AIME"));
        MatcherAssert.assertThat(firstFlight.getAircraftType(), IsEqual.equalTo("Airbus A380-800"));
        MatcherAssert.assertThat(firstFlight.getAirlineCode(), IsEqual.equalTo("LH"));
        MatcherAssert.assertThat(firstFlight.getAirlineName(), IsEqual.equalTo("Lufthansa"));
        MatcherAssert.assertThat(firstFlight.getArrivalAirportCode(), IsEqual.equalTo("FRA"));
        MatcherAssert.assertThat(firstFlight.getArrivalDateLocal(), IsEqual.equalTo(LocalDate.of(2015, 5, 8)));
        MatcherAssert.assertThat(firstFlight.getArrivalTimeLocal(), IsEqual.equalTo(LocalTime.of(7, 15)));
        MatcherAssert.assertThat(firstFlight.getCabinClass(), IsEqual.equalTo(CabinClass.PREMIUM_ECONOMY));
        MatcherAssert.assertThat(firstFlight.getComment(), IsEqual.equalTo("Comment X"));
        MatcherAssert.assertThat(firstFlight.getDepartureAirportCode(), IsEqual.equalTo("DEL"));
        MatcherAssert.assertThat(firstFlight.getDepartureDateLocal(), IsEqual.equalTo(LocalDate.of(2015, 5, 8)));
        MatcherAssert.assertThat(firstFlight.getDepartureTimeLocal(), IsEqual.equalTo(LocalTime.of(3, 0)));
        MatcherAssert.assertThat(firstFlight.getFlightDistance(), IsEqual.equalTo(6120));
        MatcherAssert.assertThat(firstFlight.getFlightDuration(), IsEqual.equalTo("07:45"));
        MatcherAssert.assertThat(firstFlight.getFlightNumber(), IsEqual.equalTo("761"));
        MatcherAssert.assertThat(firstFlight.getFlightReason(), IsEqual.equalTo(FlightReason.BUSINESS));
        MatcherAssert.assertThat(firstFlight.getSeatNumber(), IsEqual.equalTo("54A"));
        MatcherAssert.assertThat(firstFlight.getSeatType(), IsEqual.equalTo(SeatType.WINDOW));

        FlightsExchangePackageFlight secondFlight = exchangePackage.getFlights().get(1);
        MatcherAssert.assertThat(secondFlight.getAircraftName(), IsEmptyString.emptyOrNullString());
        MatcherAssert.assertThat(secondFlight.getAircraftRegistration(), IsEmptyString.emptyOrNullString());
        MatcherAssert.assertThat(secondFlight.getAircraftType(), IsEqual.equalTo("Boeing 737-700"));
        MatcherAssert.assertThat(secondFlight.getAirlineCode(), IsEqual.equalTo("AB"));
        MatcherAssert.assertThat(secondFlight.getAirlineName(), IsEqual.equalTo("Air Berlin"));
        MatcherAssert.assertThat(secondFlight.getArrivalAirportCode(), IsEqual.equalTo("TXL"));
        MatcherAssert.assertThat(secondFlight.getArrivalDateLocal(), IsEqual.equalTo(LocalDate.of(2015, 5, 14)));
        MatcherAssert.assertThat(secondFlight.getArrivalTimeLocal(), IsNull.nullValue());
        MatcherAssert.assertThat(secondFlight.getCabinClass(), IsEqual.equalTo(CabinClass.ECONOMY));
        MatcherAssert.assertThat(secondFlight.getComment(), IsEmptyString.emptyOrNullString());
        MatcherAssert.assertThat(secondFlight.getDepartureAirportCode(), IsEqual.equalTo("CGN"));
        MatcherAssert.assertThat(secondFlight.getDepartureDateLocal(), IsEqual.equalTo(LocalDate.of(2015, 5, 14)));
        MatcherAssert.assertThat(secondFlight.getDepartureTimeLocal(), IsNull.nullValue());
        MatcherAssert.assertThat(secondFlight.getFlightDistance(), IsNull.nullValue());
        MatcherAssert.assertThat(secondFlight.getFlightDuration(), IsNull.nullValue());
        MatcherAssert.assertThat(secondFlight.getFlightNumber(), IsEqual.equalTo("6508"));
        MatcherAssert.assertThat(secondFlight.getFlightReason(), IsEqual.equalTo(FlightReason.PRIVATE));
        MatcherAssert.assertThat(secondFlight.getSeatNumber(), IsNull.nullValue());
        MatcherAssert.assertThat(secondFlight.getSeatType(), IsNull.nullValue());

    }

}
