package de.perdian.apps.flighttracker.business.modules.data;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class FlightDataServiceImplTest {

    @Test
    public void lookupFlightData() {

        FlightData flightData2 = new FlightData();
        FlightDataSource source1 = Mockito.mock(FlightDataSource.class);
        FlightDataSource source2 = Mockito.mock(FlightDataSource.class);
        Mockito.when(source2.lookupFlightData(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(flightData2);
        FlightDataSource source3 = Mockito.mock(FlightDataSource.class);

        FlightDataServiceImpl serviceImpl = new FlightDataServiceImpl();
        serviceImpl.setSources(Arrays.asList(source1, source2, source3));

        FlightData flightDataResolved = serviceImpl.lookupFlightData("LH", "123", LocalDate.of(2017, 10, 9));
        Assert.assertEquals(flightData2, flightDataResolved);
        Mockito.verify(source1).lookupFlightData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(2017, 10, 9)));
        Mockito.verify(source2).lookupFlightData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(2017, 10, 9)));
        Mockito.verifyNoMoreInteractions(source3);

    }

    @Test
    public void lookupFlightDataNothingFound() {

        FlightDataSource source1 = Mockito.mock(FlightDataSource.class);
        FlightDataSource source2 = Mockito.mock(FlightDataSource.class);

        FlightDataServiceImpl serviceImpl = new FlightDataServiceImpl();
        serviceImpl.setSources(Arrays.asList(source1, source2));

        Assert.assertNull(serviceImpl.lookupFlightData("LH", "123", LocalDate.of(2017, 10, 9)));
        Mockito.verify(source1).lookupFlightData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(2017, 10, 9)));
        Mockito.verify(source2).lookupFlightData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(2017, 10, 9)));

    }

    @Test
    public void lookupFlightDataExceptionFromFlightDataSource() {

        FlightDataSource source1 = Mockito.mock(FlightDataSource.class);
        Mockito.when(source1.lookupFlightData(Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new IllegalArgumentException());
        FlightDataSource source2 = Mockito.mock(FlightDataSource.class);

        FlightDataServiceImpl serviceImpl = new FlightDataServiceImpl();
        serviceImpl.setSources(Arrays.asList(source1, source2));

        Assert.assertNull(serviceImpl.lookupFlightData("LH", "123", LocalDate.of(2017, 10, 9)));
        Mockito.verify(source1).lookupFlightData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(2017, 10, 9)));
        Mockito.verify(source2).lookupFlightData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(2017, 10, 9)));

    }

}
