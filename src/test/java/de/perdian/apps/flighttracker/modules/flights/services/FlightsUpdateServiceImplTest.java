package de.perdian.apps.flighttracker.modules.flights.services;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import de.perdian.apps.flighttracker.modules.flights.FlightsTestHelper;
import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.flights.persistence.FlightEntity;
import de.perdian.apps.flighttracker.modules.flights.persistence.FlightsRepository;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

public class FlightsUpdateServiceImplTest {

    @Test
    public void deleteFlight() {

        FlightsRepository flightsRepository = Mockito.mock(FlightsRepository.class);

        FlightBean flightBean = new FlightBean();
        flightBean.setEntityId(Long.valueOf(42));

        FlightsUpdateServiceImpl serviceImpl = new FlightsUpdateServiceImpl();
        serviceImpl.setFlightsRepository(flightsRepository);
        serviceImpl.deleteFlight(flightBean);

        Mockito.verify(flightsRepository).delete(Mockito.eq(Long.valueOf(42)));
        Mockito.verifyNoMoreInteractions(flightsRepository);

    }

    @Test
    public void saveFlightAlreadyExisting() {

        FlightBean returnBean = new FlightBean();
        FlightsQueryService flightsQueryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(flightsQueryService.loadFlightById(Mockito.eq(Long.valueOf(42)))).thenReturn(returnBean);
        FlightsRepository flightsRepository = Mockito.mock(FlightsRepository.class);
        FlightEntity flightEntity = FlightsTestHelper.createDefaultFlightEntity();
        Mockito.when(flightsRepository.findOne(Mockito.eq(Long.valueOf(42L)))).thenReturn(flightEntity);

        FlightBean flightBean = new FlightBean();
        flightBean.setEntityId(Long.valueOf(42));

        FlightsUpdateServiceImpl serviceImpl = new FlightsUpdateServiceImpl();
        serviceImpl.setAirlinesRepository(FlightsTestHelper.createDefaultAirlinesRepository());
        serviceImpl.setAirportsRepository(FlightsTestHelper.createDefaultAirportsRepository());
        serviceImpl.setFlightsQueryService(flightsQueryService);
        serviceImpl.setFlightsRepository(flightsRepository);
        Assert.assertEquals(returnBean, serviceImpl.saveFlight(flightBean));

        Mockito.verify(flightsRepository).findOne(Mockito.eq(Long.valueOf(42)));
        Mockito.verify(flightsRepository).save(Mockito.eq(flightEntity));
        Mockito.verifyNoMoreInteractions(flightsRepository);
        Mockito.verify(flightsQueryService).loadFlightById(Mockito.eq(Long.valueOf(42)));

    }

    @Test
    public void saveFlightNotExistingYet() {

        FlightsQueryService flightsQueryService = Mockito.mock(FlightsQueryService.class);
        FlightsRepository flightsRepository = Mockito.mock(FlightsRepository.class);

        FlightEntity newFlightEntity = new FlightEntity();
        newFlightEntity.setId(Long.valueOf(43));
        FlightBean flightBean = FlightsTestHelper.createDefaultFlightBean();

        FlightsUpdateServiceImpl serviceImpl = new FlightsUpdateServiceImpl();
        serviceImpl.setAirlinesRepository(FlightsTestHelper.createDefaultAirlinesRepository());
        serviceImpl.setAirportsRepository(FlightsTestHelper.createDefaultAirportsRepository());
        serviceImpl.setFlightsQueryService(flightsQueryService);
        serviceImpl.setFlightsRepository(flightsRepository);
        serviceImpl.setNewFlightEntitySupplier(() -> newFlightEntity);
        serviceImpl.saveFlight(flightBean);

        ArgumentCaptor<FlightEntity> entityCaptor = ArgumentCaptor.forClass(FlightEntity.class);
        Mockito.verify(flightsRepository).findOne(Mockito.eq(Long.valueOf(42)));
        Mockito.verify(flightsRepository).save(entityCaptor.capture());
        Mockito.verifyNoMoreInteractions(flightsRepository);

        Assert.assertEquals(1, entityCaptor.getAllValues().size());
        Assert.assertNotNull(entityCaptor.getValue());
        Assert.assertEquals(Long.valueOf(43), entityCaptor.getValue().getId());
        Assert.assertEquals("Frankfurt am Main", entityCaptor.getValue().getAircraftName());
        Assert.assertEquals("D-AIMA", entityCaptor.getValue().getAircraftRegistration());
        Assert.assertEquals("Airbus A380-800", entityCaptor.getValue().getAircraftType());
        Assert.assertEquals("MCO", entityCaptor.getValue().getArrivalAirportCode());
        Assert.assertEquals(LocalDate.of(2017, 12, 8), entityCaptor.getValue().getArrivalDateLocal());
        Assert.assertEquals(LocalTime.of(13, 12), entityCaptor.getValue().getArrivalTimeLocal());
        Assert.assertEquals(CabinClass.ECONOMY, entityCaptor.getValue().getCabinClass());
        Assert.assertEquals("this is a comment", entityCaptor.getValue().getComment());
        Assert.assertEquals("CGN", entityCaptor.getValue().getDepartureAirportCode());
        Assert.assertEquals(LocalDate.of(2017, 12, 8), entityCaptor.getValue().getDepartureDateLocal());
        Assert.assertEquals(LocalTime.of(8, 10), entityCaptor.getValue().getDepartureTimeLocal());
        Assert.assertEquals(Integer.valueOf(1234), entityCaptor.getValue().getFlightDistance());
        Assert.assertEquals(Integer.valueOf(662), entityCaptor.getValue().getFlightDuration());
        Assert.assertEquals("1234", entityCaptor.getValue().getFlightNumber());
        Assert.assertEquals(FlightReason.PRIVATE, entityCaptor.getValue().getFlightReason());
        Assert.assertEquals("42F", entityCaptor.getValue().getSeatNumber());
        Assert.assertEquals(SeatType.WINDOW, entityCaptor.getValue().getSeatType());

    }

}
