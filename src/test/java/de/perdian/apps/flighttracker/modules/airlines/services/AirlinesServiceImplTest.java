package de.perdian.apps.flighttracker.modules.airlines.services;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;

public class AirlinesServiceImplTest {

    @Test
    public void loadAirlineByCodeFromFirstLookup() {

        AirlineBean firstAirlineBean = new AirlineBean();
        AirlinesLookup firstLookup = Mockito.mock(AirlinesLookup.class);
        Mockito.when(firstLookup.loadAirlineByCode(Mockito.eq("LH"), Mockito.any())).thenReturn(firstAirlineBean);
        AirlinesLookup secondLookup = Mockito.mock(AirlinesLookup.class);

        AirlinesServiceImpl serviceImpl = new AirlinesServiceImpl();
        serviceImpl.setAirlinesLookups(Arrays.asList(firstLookup, secondLookup));
        AirlineBean serviceResult = serviceImpl.loadAirlineByCode("LH", null);

        Assertions.assertEquals(firstAirlineBean, serviceResult);
        Mockito.verify(firstLookup).loadAirlineByCode(Mockito.eq("LH"), Mockito.any());
        Mockito.verifyNoMoreInteractions(firstLookup);
        Mockito.verifyNoMoreInteractions(secondLookup);

    }

    @Test
    public void loadAirlineByCodeFromSecondLookup() {

        AirlinesLookup firstLookup = Mockito.mock(AirlinesLookup.class);
        AirlineBean secondAirlineBean = new AirlineBean();
        AirlinesLookup secondLookup = Mockito.mock(AirlinesLookup.class);
        Mockito.when(secondLookup.loadAirlineByCode(Mockito.eq("LH"), Mockito.any())).thenReturn(secondAirlineBean);

        AirlinesServiceImpl serviceImpl = new AirlinesServiceImpl();
        serviceImpl.setAirlinesLookups(Arrays.asList(firstLookup, secondLookup));
        AirlineBean serviceResult = serviceImpl.loadAirlineByCode("LH", null);

        Assertions.assertEquals(secondAirlineBean, serviceResult);
        Mockito.verify(firstLookup).loadAirlineByCode(Mockito.eq("LH"), Mockito.any());
        Mockito.verifyNoMoreInteractions(firstLookup);
        Mockito.verify(secondLookup).loadAirlineByCode(Mockito.eq("LH"), Mockito.any());
        Mockito.verifyNoMoreInteractions(secondLookup);

    }

    @Test
    public void loadAirlineByCodeNothingFound() {

        AirlinesLookup firstLookup = Mockito.mock(AirlinesLookup.class);
        AirlinesLookup secondLookup = Mockito.mock(AirlinesLookup.class);

        AirlinesServiceImpl serviceImpl = new AirlinesServiceImpl();
        serviceImpl.setAirlinesLookups(Arrays.asList(firstLookup, secondLookup));
        AirlineBean serviceResult = serviceImpl.loadAirlineByCode("LH", null);

        Assertions.assertNull(serviceResult);
        Mockito.verify(firstLookup).loadAirlineByCode(Mockito.eq("LH"), Mockito.any());
        Mockito.verifyNoMoreInteractions(firstLookup);
        Mockito.verify(secondLookup).loadAirlineByCode(Mockito.eq("LH"), Mockito.any());
        Mockito.verifyNoMoreInteractions(secondLookup);

    }

    @Test
    public void loadAirlineByNameFromFirstLookup() {

        AirlineBean firstAirlineBean = new AirlineBean();
        AirlinesLookup firstLookup = Mockito.mock(AirlinesLookup.class);
        Mockito.when(firstLookup.loadAirlineByName(Mockito.eq("Lufthansa"), Mockito.any())).thenReturn(firstAirlineBean);
        AirlinesLookup secondLookup = Mockito.mock(AirlinesLookup.class);

        AirlinesServiceImpl serviceImpl = new AirlinesServiceImpl();
        serviceImpl.setAirlinesLookups(Arrays.asList(firstLookup, secondLookup));
        AirlineBean serviceResult = serviceImpl.loadAirlineByName("Lufthansa", null);

        Assertions.assertEquals(firstAirlineBean, serviceResult);
        Mockito.verify(firstLookup).loadAirlineByName(Mockito.eq("Lufthansa"), Mockito.any());
        Mockito.verifyNoMoreInteractions(firstLookup);
        Mockito.verifyNoMoreInteractions(secondLookup);

    }

    @Test
    public void loadAirlineByNameFromSecondLookup() {

        AirlinesLookup firstLookup = Mockito.mock(AirlinesLookup.class);
        AirlineBean secondAirlineBean = new AirlineBean();
        AirlinesLookup secondLookup = Mockito.mock(AirlinesLookup.class);
        Mockito.when(secondLookup.loadAirlineByName(Mockito.eq("Lufthansa"), Mockito.any())).thenReturn(secondAirlineBean);

        AirlinesServiceImpl serviceImpl = new AirlinesServiceImpl();
        serviceImpl.setAirlinesLookups(Arrays.asList(firstLookup, secondLookup));
        AirlineBean serviceResult = serviceImpl.loadAirlineByName("Lufthansa", null);

        Assertions.assertEquals(secondAirlineBean, serviceResult);
        Mockito.verify(firstLookup).loadAirlineByName(Mockito.eq("Lufthansa"), Mockito.any());
        Mockito.verifyNoMoreInteractions(firstLookup);
        Mockito.verify(secondLookup).loadAirlineByName(Mockito.eq("Lufthansa"), Mockito.any());
        Mockito.verifyNoMoreInteractions(secondLookup);

    }

    @Test
    public void loadAirlineByNameNothingFound() {

        AirlinesLookup firstLookup = Mockito.mock(AirlinesLookup.class);
        AirlinesLookup secondLookup = Mockito.mock(AirlinesLookup.class);

        AirlinesServiceImpl serviceImpl = new AirlinesServiceImpl();
        serviceImpl.setAirlinesLookups(Arrays.asList(firstLookup, secondLookup));
        AirlineBean serviceResult = serviceImpl.loadAirlineByName("Lufthansa", null);

        Assertions.assertNull(serviceResult);
        Mockito.verify(firstLookup).loadAirlineByName(Mockito.eq("Lufthansa"), Mockito.any());
        Mockito.verifyNoMoreInteractions(firstLookup);
        Mockito.verify(secondLookup).loadAirlineByName(Mockito.eq("Lufthansa"), Mockito.any());
        Mockito.verifyNoMoreInteractions(secondLookup);

    }

}
