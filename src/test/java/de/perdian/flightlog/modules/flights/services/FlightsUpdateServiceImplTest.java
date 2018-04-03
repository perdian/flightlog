package de.perdian.flightlog.modules.flights.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import de.perdian.flightlog.FlightlogTestHelper;
import de.perdian.flightlog.modules.flights.model.FlightBean;
import de.perdian.flightlog.modules.flights.persistence.FlightEntity;
import de.perdian.flightlog.modules.flights.persistence.FlightsRepository;
import de.perdian.flightlog.modules.flights.services.FlightsQueryService;
import de.perdian.flightlog.modules.flights.services.FlightsUpdateServiceImpl;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;

public class FlightsUpdateServiceImplTest {

    @Test
    public void deleteFlight() {

        FlightsRepository flightsRepository = Mockito.mock(FlightsRepository.class);

        FlightBean flightBean = new FlightBean();
        flightBean.setEntityId(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"));

        FlightsUpdateServiceImpl serviceImpl = new FlightsUpdateServiceImpl();
        serviceImpl.setFlightsRepository(flightsRepository);
        serviceImpl.deleteFlight(flightBean);

        Mockito.verify(flightsRepository).deleteById(Mockito.eq(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe")));
        Mockito.verifyNoMoreInteractions(flightsRepository);

    }

    @Test
    public void saveFlightAlreadyExisting() {

        FlightBean returnBean = new FlightBean();
        FlightsQueryService flightsQueryService = Mockito.mock(FlightsQueryService.class);
        Mockito.when(flightsQueryService.loadFlightById(Mockito.eq(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe")), Mockito.any())).thenReturn(returnBean);
        FlightsRepository flightsRepository = Mockito.mock(FlightsRepository.class);
        FlightEntity flightEntity = FlightlogTestHelper.createDefaultFlightEntity();
        Mockito.when(flightsRepository.findOne(Mockito.any(Specification.class))).thenReturn(Optional.of(flightEntity));

        FlightBean flightBean = new FlightBean();
        flightBean.setEntityId(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe"));

        FlightsUpdateServiceImpl serviceImpl = new FlightsUpdateServiceImpl();
        serviceImpl.setAirlinesService(FlightlogTestHelper.createDefaultAirlinesService());
        serviceImpl.setAirportsRepository(FlightlogTestHelper.createDefaultAirportsRepository());
        serviceImpl.setFlightsQueryService(flightsQueryService);
        serviceImpl.setFlightsRepository(flightsRepository);
        Assertions.assertEquals(returnBean, serviceImpl.saveFlight(flightBean, null));

        Mockito.verify(flightsRepository).findOne(Mockito.any(Specification.class));
        Mockito.verify(flightsRepository).save(Mockito.eq(flightEntity));
        Mockito.verifyNoMoreInteractions(flightsRepository);
        Mockito.verify(flightsQueryService).loadFlightById(Mockito.eq(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cfe")), Mockito.any());

    }

    @Test
    public void saveFlightNotExistingYet() {

        FlightsQueryService flightsQueryService = Mockito.mock(FlightsQueryService.class);
        FlightsRepository flightsRepository = Mockito.mock(FlightsRepository.class);

        FlightEntity newFlightEntity = new FlightEntity();
        newFlightEntity.setId(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cff"));
        FlightBean flightBean = FlightlogTestHelper.createDefaultFlightBean();

        FlightsUpdateServiceImpl serviceImpl = new FlightsUpdateServiceImpl();
        serviceImpl.setAirlinesService(FlightlogTestHelper.createDefaultAirlinesService());
        serviceImpl.setAirportsRepository(FlightlogTestHelper.createDefaultAirportsRepository());
        serviceImpl.setFlightsQueryService(flightsQueryService);
        serviceImpl.setFlightsRepository(flightsRepository);
        serviceImpl.setNewFlightEntitySupplier(() -> newFlightEntity);
        serviceImpl.saveFlight(flightBean, null);

        ArgumentCaptor<FlightEntity> entityCaptor = ArgumentCaptor.forClass(FlightEntity.class);
        Mockito.verify(flightsRepository).findOne(Mockito.any(Specification.class));
        Mockito.verify(flightsRepository).save(entityCaptor.capture());
        Mockito.verifyNoMoreInteractions(flightsRepository);

        Assertions.assertEquals(1, entityCaptor.getAllValues().size());
        Assertions.assertNotNull(entityCaptor.getValue());
        Assertions.assertEquals(UUID.fromString("c2bb2c43-e029-4cc2-a80c-7445cdea0cff"), entityCaptor.getValue().getId());
        Assertions.assertEquals("Frankfurt am Main", entityCaptor.getValue().getAircraftName());
        Assertions.assertEquals("D-AIMA", entityCaptor.getValue().getAircraftRegistration());
        Assertions.assertEquals("Airbus A380-800", entityCaptor.getValue().getAircraftType());
        Assertions.assertEquals("MCO", entityCaptor.getValue().getArrivalAirportCode());
        Assertions.assertEquals(LocalDate.of(2017, 12, 8), entityCaptor.getValue().getArrivalDateLocal());
        Assertions.assertEquals(LocalTime.of(13, 12), entityCaptor.getValue().getArrivalTimeLocal());
        Assertions.assertEquals(CabinClass.ECONOMY, entityCaptor.getValue().getCabinClass());
        Assertions.assertEquals("this is a comment", entityCaptor.getValue().getComment());
        Assertions.assertEquals("CGN", entityCaptor.getValue().getDepartureAirportCode());
        Assertions.assertEquals(LocalDate.of(2017, 12, 8), entityCaptor.getValue().getDepartureDateLocal());
        Assertions.assertEquals(LocalTime.of(8, 10), entityCaptor.getValue().getDepartureTimeLocal());
        Assertions.assertEquals(Integer.valueOf(1234), entityCaptor.getValue().getFlightDistance());
        Assertions.assertEquals(Integer.valueOf(662), entityCaptor.getValue().getFlightDuration());
        Assertions.assertEquals("1234", entityCaptor.getValue().getFlightNumber());
        Assertions.assertEquals(FlightReason.PRIVATE, entityCaptor.getValue().getFlightReason());
        Assertions.assertEquals("42F", entityCaptor.getValue().getSeatNumber());
        Assertions.assertEquals(SeatType.WINDOW, entityCaptor.getValue().getSeatType());

    }

}
