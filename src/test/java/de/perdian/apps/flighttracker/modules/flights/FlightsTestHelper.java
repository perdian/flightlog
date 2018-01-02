package de.perdian.apps.flighttracker.modules.flights;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.mockito.Mockito;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.airlines.services.AirlinesService;
import de.perdian.apps.flighttracker.modules.airports.persistence.AirportEntity;
import de.perdian.apps.flighttracker.modules.airports.persistence.AirportsRepository;
import de.perdian.apps.flighttracker.modules.flights.model.AircraftBean;
import de.perdian.apps.flighttracker.modules.flights.model.AirportBean;
import de.perdian.apps.flighttracker.modules.flights.model.AirportContactBean;
import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.flights.persistence.FlightEntity;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

public class FlightsTestHelper {

    public static FlightEntity createDefaultFlightEntity() {
        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setId(Long.valueOf(42));
        flightEntity.setAircraftName("Frankfurt am Main");
        flightEntity.setAircraftRegistration("D-AIMA");
        flightEntity.setAircraftType("Airbus A380-800");
        flightEntity.setAirlineCode("LH");
        flightEntity.setAirlineName("Lufthansa");
        flightEntity.setArrivalAirportCode("MCO");
        flightEntity.setArrivalDateLocal(LocalDate.of(2017, 12, 8));
        flightEntity.setArrivalTimeLocal(LocalTime.of(13, 12));
        flightEntity.setCabinClass(CabinClass.ECONOMY);
        flightEntity.setComment("this is a comment");
        flightEntity.setDepartureAirportCode("CGN");
        flightEntity.setDepartureDateLocal(LocalDate.of(2017, 12, 8));
        flightEntity.setDepartureTimeLocal(LocalTime.of(8, 10));
        flightEntity.setFlightDistance(1234);
        flightEntity.setFlightDuration((int)Duration.ofHours(7).plusMinutes(30).toMinutes());
        flightEntity.setFlightNumber("1234");
        flightEntity.setFlightReason(FlightReason.PRIVATE);
        flightEntity.setSeatNumber("42F");
        flightEntity.setSeatType(SeatType.WINDOW);
        return flightEntity;
    }

    public static FlightBean createDefaultFlightBean() {

        AircraftBean aircraftBean = new AircraftBean();
        aircraftBean.setName("Frankfurt am Main");
        aircraftBean.setRegistration("D-AIMA");
        aircraftBean.setType("Airbus A380-800");

        AirlineBean airlineBean = new AirlineBean();
        airlineBean.setIataCode("LH");
        airlineBean.setName("Lufthansa");

        AirportBean arrivalAirportBean = new AirportBean();
        arrivalAirportBean.setCode("MCO");
        AirportContactBean arrivalContactBean = new AirportContactBean();
        arrivalContactBean.setAirport(arrivalAirportBean);
        arrivalContactBean.setDateLocal(LocalDate.of(2017, 12, 8));
        arrivalContactBean.setTimeLocal(LocalTime.of(13, 12));

        AirportBean departureAirportBean = new AirportBean();
        departureAirportBean.setCode("CGN");
        AirportContactBean departureContactBean = new AirportContactBean();
        departureContactBean.setAirport(departureAirportBean);
        departureContactBean.setDateLocal(LocalDate.of(2017, 12, 8));
        departureContactBean.setTimeLocal(LocalTime.of(8, 10));

        FlightBean flightBean = new FlightBean();
        flightBean.setEntityId(Long.valueOf(42));
        flightBean.setAircraft(aircraftBean);
        flightBean.setAirline(airlineBean);
        flightBean.setArrivalContact(arrivalContactBean);
        flightBean.setCabinClass(CabinClass.ECONOMY);
        flightBean.setComment("this is a comment");
        flightBean.setDepartureContact(departureContactBean);
        flightBean.setFlightDistance(1234);
        flightBean.setFlightNumber("1234");
        flightBean.setFlightReason(FlightReason.PRIVATE);
        flightBean.setSeatNumber("42F");
        flightBean.setSeatType(SeatType.WINDOW);
        return flightBean;

    }

    public static AirlinesService createDefaultAirlinesService() {

        AirlineBean lufthansaEntity = Mockito.mock(AirlineBean.class);
        Mockito.when(lufthansaEntity.getName()).thenReturn("Lufthansa");
        Mockito.when(lufthansaEntity.getIataCode()).thenReturn("LH");

        AirlinesService airlinesService = Mockito.mock(AirlinesService.class);
        Mockito.when(airlinesService.loadAirlineByIataCode(Mockito.eq("LH"))).thenReturn(lufthansaEntity);
        return airlinesService;

    }

    public static AirportsRepository createDefaultAirportsRepository() {

        AirportEntity cologneEntity = Mockito.mock(AirportEntity.class);
        Mockito.when(cologneEntity.getCountryCode()).thenReturn("DE");
        Mockito.when(cologneEntity.getIataCode()).thenReturn("CGN");
        Mockito.when(cologneEntity.getIcaoCode()).thenReturn("EDDK");
        Mockito.when(cologneEntity.getLatitude()).thenReturn(50.8658981323f);
        Mockito.when(cologneEntity.getLongitude()).thenReturn(7.1427397728f);
        Mockito.when(cologneEntity.getName()).thenReturn("Cologne Bonn Airport");
        Mockito.when(cologneEntity.getTimezoneId()).thenReturn(ZoneId.of("Europe/Berlin"));
        Mockito.when(cologneEntity.getTimezoneOffset()).thenReturn(ZoneOffset.ofHours(2));

        AirportEntity orlandoEntity = Mockito.mock(AirportEntity.class);
        Mockito.when(orlandoEntity.getCountryCode()).thenReturn("US");
        Mockito.when(orlandoEntity.getIataCode()).thenReturn("MCO");
        Mockito.when(orlandoEntity.getIcaoCode()).thenReturn("KMCO");
        Mockito.when(orlandoEntity.getLatitude()).thenReturn(28.429399490356445f);
        Mockito.when(orlandoEntity.getLongitude()).thenReturn(-81.30899810791016f);
        Mockito.when(orlandoEntity.getName()).thenReturn("Orlando International Airport");
        Mockito.when(orlandoEntity.getTimezoneId()).thenReturn(ZoneId.of("America/New_York"));
        Mockito.when(orlandoEntity.getTimezoneOffset()).thenReturn(ZoneOffset.ofHours(-4));

        AirportsRepository airportsRepository = Mockito.mock(AirportsRepository.class);
        Mockito.when(airportsRepository.loadAirportByIataCode(Mockito.eq("CGN"))).thenReturn(cologneEntity);
        Mockito.when(airportsRepository.loadAirportByIataCode(Mockito.eq("MCO"))).thenReturn(orlandoEntity);
        return airportsRepository;

    }


}
