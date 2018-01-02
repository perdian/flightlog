package de.perdian.apps.flighttracker.modules.airlines.services.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.airlines.persistence.AirlineEntity;
import de.perdian.apps.flighttracker.modules.airlines.persistence.AirlinesRepository;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

public class DatabaseAirlinesLookupTest {

    @Test
    public void loadAirlineByCode() {

        AirlineEntity airlineEntity = new AirlineEntity();
        airlineEntity.setCode("LH");
        airlineEntity.setCountryCode("DE");
        airlineEntity.setName("Lufthansa");

        AirlinesRepository repository = Mockito.mock(AirlinesRepository.class);
        Mockito.when(repository.findOne(Mockito.any(Specification.class))).thenReturn(airlineEntity);

        DatabaseAirlinesLookup lookup = new DatabaseAirlinesLookup();
        lookup.setAirlinesRepository(repository);

        Assertions.assertEquals(0, lookup.getAirlineBeansByCode().size());

        AirlineBean airlineBean = lookup.loadAirlineByCode("LH", null);

        Assertions.assertNotNull(airlineBean);
        Assertions.assertEquals("LH", airlineBean.getCode());
        Assertions.assertEquals("DE", airlineBean.getCountryCode());
        Assertions.assertEquals("Lufthansa", airlineBean.getName());
        Assertions.assertEquals(1, lookup.getAirlineBeansByCode().size());
        Assertions.assertEquals(1, lookup.getAirlineBeansByCode().get(null).size());
        Assertions.assertEquals(airlineBean, lookup.getAirlineBeansByCode().get(null).get("LH"));

        AirlineBean secondCallResult = lookup.loadAirlineByCode("LH", null);
        Assertions.assertEquals("LH", secondCallResult.getCode());
        Assertions.assertEquals("DE", secondCallResult.getCountryCode());
        Assertions.assertEquals("Lufthansa", secondCallResult.getName());

        Mockito.verify(repository, Mockito.times(1)).findOne(Mockito.any(Specification.class));

    }

    @Test
    public void loadAirlineByCodeNotFound() {

        AirlinesRepository repository = Mockito.mock(AirlinesRepository.class);
        DatabaseAirlinesLookup lookup = new DatabaseAirlinesLookup();
        lookup.setAirlinesRepository(repository);
        Assertions.assertEquals(0, lookup.getAirlineBeansByCode().size());

        AirlineBean airlineBean1 = lookup.loadAirlineByCode("LH", null);
        Assertions.assertNull(airlineBean1);
        AirlineBean airlineBean2 = lookup.loadAirlineByCode("LH", null);
        Assertions.assertNull(airlineBean2);

        Assertions.assertEquals(0, lookup.getAirlineBeansByCode().size());

        Mockito.verify(repository, Mockito.times(2)).findOne(Mockito.any(Specification.class));

    }

    @Test
    public void loadAirlineByName() {

        AirlineEntity airlineEntity = new AirlineEntity();
        airlineEntity.setCode("LH");
        airlineEntity.setCountryCode("DE");
        airlineEntity.setName("Lufthansa");

        AirlinesRepository repository = Mockito.mock(AirlinesRepository.class);
        Mockito.when(repository.findOne(Mockito.any(Specification.class))).thenReturn(airlineEntity);

        DatabaseAirlinesLookup lookup = new DatabaseAirlinesLookup();
        lookup.setAirlinesRepository(repository);

        Assertions.assertEquals(0, lookup.getAirlineBeansByCode().size());

        AirlineBean airlineBean = lookup.loadAirlineByName("Lufthansa", null);

        Assertions.assertNotNull(airlineBean);
        Assertions.assertEquals("LH", airlineBean.getCode());
        Assertions.assertEquals("DE", airlineBean.getCountryCode());
        Assertions.assertEquals("Lufthansa", airlineBean.getName());
        Assertions.assertEquals(1, lookup.getAirlineBeansByCode().size());
        Assertions.assertEquals(1, lookup.getAirlineBeansByCode().get(null).size());
        Assertions.assertEquals(airlineBean, lookup.getAirlineBeansByCode().get(null).get("LH"));

        AirlineBean secondCallResult = lookup.loadAirlineByName("Lufthansa", null);
        Assertions.assertEquals("LH", secondCallResult.getCode());
        Assertions.assertEquals("DE", secondCallResult.getCountryCode());
        Assertions.assertEquals("Lufthansa", secondCallResult.getName());

        Mockito.verify(repository, Mockito.times(1)).findOne(Mockito.any(Specification.class));

    }

    @Test
    public void loadAirlineByNameNotFound() {

        AirlinesRepository repository = Mockito.mock(AirlinesRepository.class);
        DatabaseAirlinesLookup lookup = new DatabaseAirlinesLookup();
        lookup.setAirlinesRepository(repository);
        Assertions.assertEquals(0, lookup.getAirlineBeansByCode().size());

        AirlineBean airlineBean1 = lookup.loadAirlineByName("Lufthansa", null);
        Assertions.assertNull(airlineBean1);
        AirlineBean airlineBean2 = lookup.loadAirlineByName("Lufthansa", null);
        Assertions.assertNull(airlineBean2);

        Assertions.assertEquals(0, lookup.getAirlineBeansByCode().size());

        Mockito.verify(repository, Mockito.times(2)).findOne(Mockito.any(Specification.class));

    }

    @Test
    public void removeAirlineByCode() {

        AirlineBean airlineBean = new AirlineBean();
        Map<String, AirlineBean> airlineBeansByCode = new LinkedHashMap<>();
        airlineBeansByCode.put("LH", airlineBean);
        Map<UserEntity, Map<String, AirlineBean>> airlineBeansByUser = new LinkedHashMap<>();
        airlineBeansByUser.put(null, airlineBeansByCode);

        DatabaseAirlinesLookup lookup = new DatabaseAirlinesLookup();
        lookup.setAirlineBeansByCode(airlineBeansByUser);
        lookup.removeAirlineByCode("LH", null);

        Assertions.assertEquals(0, airlineBeansByCode.size());
        Assertions.assertEquals(0, airlineBeansByUser.size());

    }

    @Test
    public void removeAirlineByCodeNotFound() {

        AirlineBean airlineBean = new AirlineBean();
        Map<String, AirlineBean> airlineBeansByCode = new LinkedHashMap<>();
        airlineBeansByCode.put("LH", airlineBean);
        Map<UserEntity, Map<String, AirlineBean>> airlineBeansByUser = new LinkedHashMap<>();
        airlineBeansByUser.put(null, airlineBeansByCode);

        DatabaseAirlinesLookup lookup = new DatabaseAirlinesLookup();
        lookup.setAirlineBeansByCode(airlineBeansByUser);
        lookup.removeAirlineByCode("XX", null);

        Assertions.assertEquals(1, airlineBeansByCode.size());
        Assertions.assertEquals(1, airlineBeansByUser.size());

    }

    @Test
    public void updateAirlineByCodeWithOldEntry() {

        AirlineBean airlineBean = new AirlineBean();
        Map<String, AirlineBean> airlineBeansByCode = new LinkedHashMap<>();
        airlineBeansByCode.put("LH", airlineBean);
        Map<UserEntity, Map<String, AirlineBean>> airlineBeansByUser = new LinkedHashMap<>();
        airlineBeansByUser.put(null, airlineBeansByCode);

        AirlineBean newBean = new AirlineBean();
        DatabaseAirlinesLookup lookup = new DatabaseAirlinesLookup();
        lookup.setAirlineBeansByCode(airlineBeansByUser);
        lookup.updateAirlineByCode("LH", newBean, null);

        Assertions.assertEquals(1, airlineBeansByUser.size());
        Assertions.assertEquals(1, airlineBeansByCode.size());
        Assertions.assertEquals(newBean, airlineBeansByCode.get("LH"));

    }

    @Test
    public void updateAirlineByCodeWithNoOldEntry() {

        AirlineBean airlineBean = new AirlineBean();
        Map<String, AirlineBean> airlineBeansByCode = new LinkedHashMap<>();
        airlineBeansByCode.put("LH", airlineBean);
        Map<UserEntity, Map<String, AirlineBean>> airlineBeansByUser = new LinkedHashMap<>();
        airlineBeansByUser.put(null, airlineBeansByCode);

        AirlineBean newBean = new AirlineBean();
        DatabaseAirlinesLookup lookup = new DatabaseAirlinesLookup();
        lookup.setAirlineBeansByCode(airlineBeansByUser);
        lookup.updateAirlineByCode("XX", newBean, null);

        Assertions.assertEquals(1, airlineBeansByUser.size());
        Assertions.assertEquals(2, airlineBeansByCode.size());
        Assertions.assertEquals(airlineBean, airlineBeansByCode.get("LH"));
        Assertions.assertEquals(newBean, airlineBeansByCode.get("XX"));

    }

}
