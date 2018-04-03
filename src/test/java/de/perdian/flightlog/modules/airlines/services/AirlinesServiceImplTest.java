package de.perdian.flightlog.modules.airlines.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import de.perdian.flightlog.FlightlogTestHelper;
import de.perdian.flightlog.modules.airlines.model.AirlineBean;
import de.perdian.flightlog.modules.airlines.persistence.AirlineEntity;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;
import de.perdian.flightlog.modules.airlines.services.AirlinesLookup;
import de.perdian.flightlog.modules.airlines.services.AirlinesServiceImpl;
import de.perdian.flightlog.modules.users.persistence.UserEntity;

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

    @Test
    public void loadUserSpecificAirlines() {

        AirlinesRepository airlinesRepository = FlightlogTestHelper.createDefaultAirlinesRepository();

        AirlinesServiceImpl serviceImpl = new AirlinesServiceImpl();
        serviceImpl.setAirlinesRepository(airlinesRepository);

        List<AirlineBean> airlineBeans = serviceImpl.loadUserSpecificAirlines(null);
        Assertions.assertEquals(2, airlineBeans.size());
        Assertions.assertEquals("LH", airlineBeans.get(0).getCode());
        Assertions.assertEquals("DE", airlineBeans.get(0).getCountryCode());
        Assertions.assertEquals("Lufthansa", airlineBeans.get(0).getName());
        Assertions.assertEquals("UA", airlineBeans.get(1).getCode());
        Assertions.assertEquals("US", airlineBeans.get(1).getCountryCode());
        Assertions.assertEquals("United", airlineBeans.get(1).getName());

    }

    @Test
    public void updateUserSpecificAirlineNewAirline() {

        UserEntity userEntity = new UserEntity();
        AirlinesRepository airlinesRepository = FlightlogTestHelper.createDefaultAirlinesRepository();

        AirlinesServiceImpl serviceImpl = new AirlinesServiceImpl();
        serviceImpl.setAirlinesRepository(airlinesRepository);

        AirlineBean newAirlineBean = new AirlineBean();
        newAirlineBean.setCode("DL");
        newAirlineBean.setCountryCode("US");
        newAirlineBean.setName("Delta");

        AirlineBean updatedAirlineBean = serviceImpl.updateUserSpecificAirline(userEntity, newAirlineBean);
        Assertions.assertEquals(newAirlineBean, updatedAirlineBean);

        ArgumentCaptor<AirlineEntity> entityCaptor = ArgumentCaptor.forClass(AirlineEntity.class);
        Mockito.verify(airlinesRepository).save(entityCaptor.capture());
        Assertions.assertEquals("DL", entityCaptor.getValue().getCode());
        Assertions.assertEquals("US", entityCaptor.getValue().getCountryCode());
        Assertions.assertEquals("Delta", entityCaptor.getValue().getName());
        Assertions.assertEquals(userEntity, entityCaptor.getValue().getUser());

    }

    @Test
    public void updateUserSpecificAirlineExistingAirline() {

        UserEntity userEntity = new UserEntity();

        AirlineEntity oldAirlineEntity = new AirlineEntity();
        oldAirlineEntity.setCode("LH");
        oldAirlineEntity.setCountryCode("DE");
        oldAirlineEntity.setName("Lufthansa");

        AirlinesRepository airlinesRepository = FlightlogTestHelper.createDefaultAirlinesRepository();
        Mockito.when(airlinesRepository.findOne(Mockito.any(Specification.class))).thenReturn(Optional.of(oldAirlineEntity));

        AirlinesServiceImpl serviceImpl = new AirlinesServiceImpl();
        serviceImpl.setAirlinesRepository(airlinesRepository);

        AirlineBean newAirlineBean = new AirlineBean();
        newAirlineBean.setCode("DL");
        newAirlineBean.setCountryCode("US");
        newAirlineBean.setName("Delta");

        AirlineBean updatedAirlineBean = serviceImpl.updateUserSpecificAirline(userEntity, newAirlineBean);
        Assertions.assertEquals(newAirlineBean, updatedAirlineBean);

        ArgumentCaptor<AirlineEntity> entityCaptor = ArgumentCaptor.forClass(AirlineEntity.class);
        Mockito.verify(airlinesRepository).save(entityCaptor.capture());
        Assertions.assertEquals("DL", entityCaptor.getValue().getCode());
        Assertions.assertEquals("US", entityCaptor.getValue().getCountryCode());
        Assertions.assertEquals("Delta", entityCaptor.getValue().getName());
        Assertions.assertNull(entityCaptor.getValue().getUser()); // Old value from oldAirlineEntity - will not be changed by the service!

    }

    @Test
    public void deleteUserSpecificAirline() {

        AirlineEntity oldAirlineEntity = new AirlineEntity();
        oldAirlineEntity.setCode("LH");
        oldAirlineEntity.setCountryCode("DE");
        oldAirlineEntity.setName("Lufthansa");

        AirlinesRepository airlinesRepository = FlightlogTestHelper.createDefaultAirlinesRepository();
        Mockito.when(airlinesRepository.findOne(Mockito.any(Specification.class))).thenReturn(Optional.of(oldAirlineEntity));

        AirlineBean newAirlineBean = new AirlineBean();
        newAirlineBean.setCode("LH");
        newAirlineBean.setCountryCode("DE");
        newAirlineBean.setName("Lufthansa");

        AirlinesServiceImpl serviceImpl = new AirlinesServiceImpl();
        serviceImpl.setAirlinesRepository(airlinesRepository);
        serviceImpl.deleteUserSpecificAirline(null, newAirlineBean);

        ArgumentCaptor<AirlineEntity> entityCaptor = ArgumentCaptor.forClass(AirlineEntity.class);
        Mockito.verify(airlinesRepository).delete(entityCaptor.capture());
        Assertions.assertEquals("LH", entityCaptor.getValue().getCode());
        Assertions.assertEquals("DE", entityCaptor.getValue().getCountryCode());
        Assertions.assertEquals("Lufthansa", entityCaptor.getValue().getName());

    }

    @Test
    public void deleteUserSpecificAirlineNotFound() {

        AirlinesRepository airlinesRepository = FlightlogTestHelper.createDefaultAirlinesRepository();

        AirlineBean newAirlineBean = new AirlineBean();
        newAirlineBean.setCode("LH");
        newAirlineBean.setCountryCode("DE");
        newAirlineBean.setName("Lufthansa");

        AirlinesServiceImpl serviceImpl = new AirlinesServiceImpl();
        serviceImpl.setAirlinesRepository(airlinesRepository);
        serviceImpl.deleteUserSpecificAirline(null, newAirlineBean);

        Mockito.verify(airlinesRepository, Mockito.never()).delete(Mockito.any(AirlineEntity.class));

    }

}
