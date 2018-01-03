package de.perdian.apps.flighttracker.modules.airlines.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AirlineBeanTest {

    @Test
    public void equals() {

        AirlineBean bean1 = this.createAirlineBean("LH", "DE", "Lufthansa");
        Assertions.assertEquals(bean1, this.createAirlineBean("LH", "DE", "Lufthansa"));
        Assertions.assertNotEquals(bean1, this.createAirlineBean("XX", "DE", "Lufthansa"));
        Assertions.assertNotEquals(bean1, this.createAirlineBean("LH", "XX", "Lufthansa"));
        Assertions.assertNotEquals(bean1, this.createAirlineBean("LH", "DE", "XX"));

    }

    private AirlineBean createAirlineBean(String code, String countryCode, String name) {
        AirlineBean bean = new AirlineBean();
        bean.setCode(code);
        bean.setCountryCode(countryCode);
        bean.setName(name);
        return bean;
    }

}
