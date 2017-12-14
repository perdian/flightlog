package de.perdian.apps.flighttracker;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.mockito.Mockito;

import de.perdian.apps.flighttracker.modules.airlines.persistence.AirlineEntity;
import de.perdian.apps.flighttracker.modules.airlines.persistence.AirlinesRepository;
import de.perdian.apps.flighttracker.modules.airports.persistence.AirportEntity;
import de.perdian.apps.flighttracker.modules.airports.persistence.AirportsRepository;
import de.perdian.apps.flighttracker.modules.flights.model.AircraftBean;
import de.perdian.apps.flighttracker.modules.flights.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.flights.model.AirportBean;
import de.perdian.apps.flighttracker.modules.flights.model.AirportContactBean;
import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.flights.persistence.FlightEntity;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

public class FlighttrackerTestHelper {

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
        FlightBean flightBean = new FlightBean();
        flightBean.setEntityId(Long.valueOf(42));
        flightBean.setAircraft(FlighttrackerTestHelper.createAircraftBean("Airbus A380-800", "D-AIMA", "Frankfurt am Main"));
        flightBean.setAirline(FlighttrackerTestHelper.createAirlineBean("LH", "Lufthansa"));
        flightBean.setArrivalContact(FlighttrackerTestHelper.createAirportContactBean("MCO", "US", LocalDate.of(2017, 12, 8), LocalTime.of(13, 12)));
        flightBean.setCabinClass(CabinClass.ECONOMY);
        flightBean.setComment("this is a comment");
        flightBean.setDepartureContact(FlighttrackerTestHelper.createAirportContactBean("CGN", "DE", LocalDate.of(2017, 12, 8), LocalTime.of(8, 10)));
        flightBean.setFlightDistance(1234);
        flightBean.setFlightNumber("1234");
        flightBean.setFlightReason(FlightReason.PRIVATE);
        flightBean.setSeatNumber("42F");
        flightBean.setSeatType(SeatType.WINDOW);
        return flightBean;
    }

    public static AirlinesRepository createDefaultAirlinesRepository() {

        AirlineEntity lufthansaEntity = Mockito.mock(AirlineEntity.class);
        Mockito.when(lufthansaEntity.getName()).thenReturn("Lufthansa");
        Mockito.when(lufthansaEntity.getIataCode()).thenReturn("LH");

        AirlinesRepository airlinesRepository = Mockito.mock(AirlinesRepository.class);
        Mockito.when(airlinesRepository.loadAirlineByIataCode(Mockito.eq("LH"))).thenReturn(lufthansaEntity);
        return airlinesRepository;

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

        AirportEntity newYorkEntity = Mockito.mock(AirportEntity.class);
        Mockito.when(newYorkEntity.getCountryCode()).thenReturn("US");
        Mockito.when(newYorkEntity.getIataCode()).thenReturn("JFK");
        Mockito.when(newYorkEntity.getIcaoCode()).thenReturn("KJFK");
        Mockito.when(newYorkEntity.getName()).thenReturn("New Jork Kennedy");
        Mockito.when(newYorkEntity.getTimezoneId()).thenReturn(ZoneId.of("America/New_York"));
        Mockito.when(newYorkEntity.getTimezoneOffset()).thenReturn(ZoneOffset.ofHours(-4));

        AirportEntity dusseldorfEntity = Mockito.mock(AirportEntity.class);
        Mockito.when(dusseldorfEntity.getCountryCode()).thenReturn("DE");
        Mockito.when(dusseldorfEntity.getIataCode()).thenReturn("DUS");
        Mockito.when(dusseldorfEntity.getIcaoCode()).thenReturn("EDDL");
        Mockito.when(dusseldorfEntity.getName()).thenReturn("Dusseldorf International");
        Mockito.when(dusseldorfEntity.getTimezoneId()).thenReturn(ZoneId.of("Europe/Berlin"));
        Mockito.when(dusseldorfEntity.getTimezoneOffset()).thenReturn(ZoneOffset.ofHours(2));

        AirportsRepository airportsRepository = Mockito.mock(AirportsRepository.class);
        Mockito.when(airportsRepository.loadAirportByIataCode(Mockito.eq("CGN"))).thenReturn(cologneEntity);
        Mockito.when(airportsRepository.loadAirportByIataCode(Mockito.eq("MCO"))).thenReturn(orlandoEntity);
        Mockito.when(airportsRepository.loadAirportByIataCode(Mockito.eq("JFK"))).thenReturn(newYorkEntity);
        Mockito.when(airportsRepository.loadAirportByIataCode(Mockito.eq("DUS"))).thenReturn(dusseldorfEntity);
        return airportsRepository;

    }

    public static AircraftBean createAircraftBean(String type, String registration, String name) {
        AircraftBean aircraftBean = new AircraftBean();
        aircraftBean.setType(type);
        aircraftBean.setRegistration(registration);
        aircraftBean.setName(name);
        return aircraftBean;
    }

    public static AirportContactBean createAirportContactBean(String airportCode, String countryCode, LocalDate date, LocalTime time) {
        AirportContactBean airportContactBean = new AirportContactBean();
        airportContactBean.setAirport(FlighttrackerTestHelper.createAirportBean(airportCode, countryCode));
        airportContactBean.setDateLocal(date);
        airportContactBean.setTimeLocal(time);
        return airportContactBean;
    }

    public static AirportBean createAirportBean(String airportCode, String countryCode) {
        AirportBean airportBean = new AirportBean();
        airportBean.setCode(airportCode);
        airportBean.setCountryCode(countryCode);
        return airportBean;
    }

    public static AirlineBean createAirlineBean(String code, String name) {
        AirlineBean airlineBean = new AirlineBean();
        airlineBean.setCode(code);
        airlineBean.setName(name);
        return airlineBean;
    }

}
