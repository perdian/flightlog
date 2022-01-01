package de.perdian.flightlog.modules.flights.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.UUID;

import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import de.perdian.flightlog.FlightlogTestHelper;
import de.perdian.flightlog.modules.flights.model.FlightBean;
import de.perdian.flightlog.modules.flights.persistence.FlightEntity;
import de.perdian.flightlog.modules.flights.persistence.FlightsRepository;
import de.perdian.flightlog.support.persistence.PaginatedList;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;

public class FlightsQueryServiceImplTest {

    @Test
    public void loadFlightById() {

        FlightEntity flightEntity = FlightlogTestHelper.createDefaultFlightEntity();
        FlightsRepository flightsRepository = Mockito.mock(FlightsRepository.class);
        Mockito.when(flightsRepository.findAll(Mockito.any(), Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(flightEntity)));

        FlightsQueryServiceImpl serviceImpl = new FlightsQueryServiceImpl();
        serviceImpl.setAirlinesRepository(FlightlogTestHelper.createDefaultAirlinesRepository());
        serviceImpl.setAirportsRepository(FlightlogTestHelper.createDefaultAirportsRepository());
        serviceImpl.setFlightsRepository(flightsRepository);

        FlightBean flightBean = serviceImpl.loadFlightById(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), null);
        Assertions.assertNotNull(flightBean);
        Assertions.assertEquals("Frankfurt am Main", flightBean.getAircraft().getName());
        Assertions.assertEquals("D-AIMA", flightBean.getAircraft().getRegistration());
        Assertions.assertEquals("Airbus A380-800", flightBean.getAircraft().getType());
        Assertions.assertEquals("MCO", flightBean.getArrivalContact().getAirport().getCode());
        Assertions.assertEquals("US", flightBean.getArrivalContact().getAirport().getCountryCode());
        Assertions.assertEquals("Orlando International Airport", flightBean.getArrivalContact().getAirport().getName());
        Assertions.assertEquals(ZoneId.of("America/New_York"), flightBean.getArrivalContact().getAirport().getTimezoneId());
        Assertions.assertEquals(ZoneOffset.ofHours(-4), flightBean.getArrivalContact().getAirport().getTimezoneOffset());
        Assertions.assertEquals(LocalDate.of(2017, 12, 8), flightBean.getArrivalContact().getDateLocal());
        Assertions.assertNull(flightBean.getArrivalContact().getDateOffset());
        Assertions.assertEquals(LocalDate.of(2017, 12, 8).atTime(13, 12).atZone(ZoneId.of("America/New_York")).toInstant(), flightBean.getArrivalContact().getDateTimeUtc());
        Assertions.assertEquals(LocalTime.of(13, 12), flightBean.getArrivalContact().getTimeLocal());
        Assertions.assertEquals(164.5d, flightBean.getAverageSpeed(), 0.1);
        Assertions.assertEquals(CabinClass.ECONOMY, flightBean.getCabinClass());
        Assertions.assertEquals("this is a comment", flightBean.getComment());
        Assertions.assertEquals("CGN", flightBean.getDepartureContact().getAirport().getCode());
        Assertions.assertEquals("DE", flightBean.getDepartureContact().getAirport().getCountryCode());
        Assertions.assertEquals("Cologne Bonn Airport", flightBean.getDepartureContact().getAirport().getName());
        Assertions.assertEquals(ZoneId.of("Europe/Berlin"), flightBean.getDepartureContact().getAirport().getTimezoneId());
        Assertions.assertEquals(ZoneOffset.ofHours(2), flightBean.getDepartureContact().getAirport().getTimezoneOffset());
        Assertions.assertEquals(LocalDate.of(2017, 12, 8), flightBean.getDepartureContact().getDateLocal());
        Assertions.assertNull(flightBean.getDepartureContact().getDateOffset());
        Assertions.assertEquals(LocalDate.of(2017, 12, 8).atTime(8, 10).atZone(ZoneId.of("Europe/Berlin")).toInstant(), flightBean.getDepartureContact().getDateTimeUtc());
        Assertions.assertEquals(LocalTime.of(8, 10), flightBean.getDepartureContact().getTimeLocal());
        Assertions.assertEquals(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), flightBean.getEntityId());
        Assertions.assertEquals(Integer.valueOf(1234), flightBean.getFlightDistance());
        Assertions.assertEquals(Duration.ofHours(7).plusMinutes(30), flightBean.getFlightDuration());
        Assertions.assertEquals("7:30", flightBean.getFlightDurationString());
        Assertions.assertEquals("1234", flightBean.getFlightNumber());
        Assertions.assertEquals(FlightReason.PRIVATE, flightBean.getFlightReason());
        Assertions.assertEquals("42F", flightBean.getSeatNumber());
        Assertions.assertEquals(SeatType.WINDOW, flightBean.getSeatType());

    }

    @Test
    public void loadFlightByIdNotFound() {

        FlightsQueryServiceImpl serviceImpl = new FlightsQueryServiceImpl();
        serviceImpl.setAirlinesRepository(FlightlogTestHelper.createDefaultAirlinesRepository());
        serviceImpl.setAirportsRepository(FlightlogTestHelper.createDefaultAirportsRepository());
        serviceImpl.setFlightsRepository(Mockito.mock(FlightsRepository.class));

        Assertions.assertNull(serviceImpl.loadFlightById(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"), null));
        Assertions.assertNull(serviceImpl.loadFlightById(null, null));

    }

    @Test
    public void loadFlights() {

        Page<FlightEntity> flightEntities = new PageImpl<>(Arrays.asList(FlightlogTestHelper.createDefaultFlightEntity()));
        FlightsRepository flightsRepository = Mockito.mock(FlightsRepository.class);
        Mockito.when(flightsRepository.findAll(Mockito.any(), Mockito.any(PageRequest.class))).thenReturn(flightEntities);

        FlightsQueryServiceImpl serviceImpl = new FlightsQueryServiceImpl();
        serviceImpl.setAirlinesRepository(FlightlogTestHelper.createDefaultAirlinesRepository());
        serviceImpl.setAirportsRepository(FlightlogTestHelper.createDefaultAirportsRepository());
        serviceImpl.setFlightsRepository(flightsRepository);

        FlightsQuery flightsQuery = new FlightsQuery();
        PaginatedList<FlightBean> flightBeans = serviceImpl.loadFlights(flightsQuery);
        Assertions.assertNotNull(flightBeans);
        MatcherAssert.assertThat(flightBeans.getItems(), IsCollectionWithSize.hasSize(1));

    }

}
