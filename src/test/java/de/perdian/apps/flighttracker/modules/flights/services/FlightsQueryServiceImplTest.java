package de.perdian.apps.flighttracker.modules.flights.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import de.perdian.apps.flighttracker.modules.flights.FlightsTestHelper;
import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.flights.persistence.FlightEntity;
import de.perdian.apps.flighttracker.modules.flights.persistence.FlightsRepository;
import de.perdian.apps.flighttracker.support.persistence.PaginatedList;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

public class FlightsQueryServiceImplTest {

    @Test
    public void loadFlightById() {

        FlightEntity flightEntity = FlightsTestHelper.createDefaultFlightEntity();
        FlightsRepository flightsRepository = Mockito.mock(FlightsRepository.class);
        Mockito.when(flightsRepository.findOne(Mockito.eq(4711L))).thenReturn(flightEntity);

        FlightsQueryServiceImpl serviceImpl = new FlightsQueryServiceImpl();
        serviceImpl.setAirlinesRepository(FlightsTestHelper.createDefaultAirlinesRepository());
        serviceImpl.setAirportsRepository(FlightsTestHelper.createDefaultAirportsRepository());
        serviceImpl.setFlightsRepository(flightsRepository);

        FlightBean flightBean = serviceImpl.loadFlightById(Long.valueOf(4711));
        Assert.assertNotNull(flightBean);
        Assert.assertEquals("Frankfurt am Main", flightBean.getAircraft().getName());
        Assert.assertEquals("D-AIMA", flightBean.getAircraft().getRegistration());
        Assert.assertEquals("Airbus A380-800", flightBean.getAircraft().getType());
        Assert.assertEquals("MCO", flightBean.getArrivalContact().getAirport().getCode());
        Assert.assertEquals("US", flightBean.getArrivalContact().getAirport().getCountryCode());
        Assert.assertEquals("Orlando International Airport", flightBean.getArrivalContact().getAirport().getName());
        Assert.assertEquals(ZoneId.of("America/New_York"), flightBean.getArrivalContact().getAirport().getTimezoneId());
        Assert.assertEquals(ZoneOffset.ofHours(-4), flightBean.getArrivalContact().getAirport().getTimezoneOffset());
        Assert.assertEquals(LocalDate.of(2017, 12, 8), flightBean.getArrivalContact().getDateLocal());
        Assert.assertNull(flightBean.getArrivalContact().getDateOffset());
        Assert.assertEquals(LocalDate.of(2017, 12, 8).atTime(13, 12).atZone(ZoneId.of("America/New_York")).toInstant(), flightBean.getArrivalContact().getDateTimeUtc());
        Assert.assertEquals(LocalTime.of(13, 12), flightBean.getArrivalContact().getTimeLocal());
        Assert.assertEquals(164.5d, flightBean.getAverageSpeed(), 0.1);
        Assert.assertEquals(CabinClass.ECONOMY, flightBean.getCabinClass());
        Assert.assertEquals("this is a comment", flightBean.getComment());
        Assert.assertEquals("CGN", flightBean.getDepartureContact().getAirport().getCode());
        Assert.assertEquals("DE", flightBean.getDepartureContact().getAirport().getCountryCode());
        Assert.assertEquals("Cologne Bonn Airport", flightBean.getDepartureContact().getAirport().getName());
        Assert.assertEquals(ZoneId.of("Europe/Berlin"), flightBean.getDepartureContact().getAirport().getTimezoneId());
        Assert.assertEquals(ZoneOffset.ofHours(2), flightBean.getDepartureContact().getAirport().getTimezoneOffset());
        Assert.assertEquals(LocalDate.of(2017, 12, 8), flightBean.getDepartureContact().getDateLocal());
        Assert.assertNull(flightBean.getDepartureContact().getDateOffset());
        Assert.assertEquals(LocalDate.of(2017, 12, 8).atTime(8, 10).atZone(ZoneId.of("Europe/Berlin")).toInstant(), flightBean.getDepartureContact().getDateTimeUtc());
        Assert.assertEquals(LocalTime.of(8, 10), flightBean.getDepartureContact().getTimeLocal());
        Assert.assertEquals(Long.valueOf(42), flightBean.getEntityId());
        Assert.assertEquals(Integer.valueOf(1234), flightBean.getFlightDistance());
        Assert.assertEquals(Duration.ofHours(7).plusMinutes(30), flightBean.getFlightDuration());
        Assert.assertEquals("7:30", flightBean.getFlightDurationString());
        Assert.assertEquals("1234", flightBean.getFlightNumber());
        Assert.assertEquals(FlightReason.PRIVATE, flightBean.getFlightReason());
        Assert.assertEquals("42F", flightBean.getSeatNumber());
        Assert.assertEquals(SeatType.WINDOW, flightBean.getSeatType());

    }

    @Test
    public void loadFlightByIdNotFound() {

        FlightsQueryServiceImpl serviceImpl = new FlightsQueryServiceImpl();
        serviceImpl.setAirlinesRepository(FlightsTestHelper.createDefaultAirlinesRepository());
        serviceImpl.setAirportsRepository(FlightsTestHelper.createDefaultAirportsRepository());
        serviceImpl.setFlightsRepository(Mockito.mock(FlightsRepository.class));

        Assert.assertNull(serviceImpl.loadFlightById(Long.valueOf(42)));
        Assert.assertNull(serviceImpl.loadFlightById(null));

    }

    @Test
    public void loadFlights() {

        Page<FlightEntity> flightEntities = new PageImpl<>(Arrays.asList(FlightsTestHelper.createDefaultFlightEntity()));
        FlightsRepository flightsRepository = Mockito.mock(FlightsRepository.class);
        Mockito.when(flightsRepository.findAll(Mockito.any(), Mockito.any(PageRequest.class))).thenReturn(flightEntities);

        FlightsQueryServiceImpl serviceImpl = new FlightsQueryServiceImpl();
        serviceImpl.setAirlinesRepository(FlightsTestHelper.createDefaultAirlinesRepository());
        serviceImpl.setAirportsRepository(FlightsTestHelper.createDefaultAirportsRepository());
        serviceImpl.setFlightsRepository(flightsRepository);

        FlightsQuery flightsQuery = new FlightsQuery();
        PaginatedList<FlightBean> flightBeans = serviceImpl.loadFlights(flightsQuery);
        Assert.assertNotNull(flightBeans);
        Assert.assertThat(flightBeans.getItems(), IsCollectionWithSize.hasSize(1));

    }

}
