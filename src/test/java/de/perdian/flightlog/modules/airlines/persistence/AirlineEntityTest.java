package de.perdian.flightlog.modules.airlines.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AirlineEntityTest {

    @Test
    public void equals() {

        AirlineEntity bean1 = this.createAirlineBean("LH", "DE", "Lufthansa");
        Assertions.assertEquals(bean1, this.createAirlineBean("LH", "DE", "Lufthansa"));
        Assertions.assertNotEquals(bean1, this.createAirlineBean("XX", "DE", "Lufthansa"));
        Assertions.assertNotEquals(bean1, this.createAirlineBean("LH", "XX", "Lufthansa"));
        Assertions.assertNotEquals(bean1, this.createAirlineBean("LH", "DE", "XX"));

    }

    private AirlineEntity createAirlineBean(String code, String countryCode, String name) {
        AirlineEntity bean = new AirlineEntity();
        bean.setCode(code);
        bean.setCountryCode(countryCode);
        bean.setName(name);
        return bean;
    }

}
