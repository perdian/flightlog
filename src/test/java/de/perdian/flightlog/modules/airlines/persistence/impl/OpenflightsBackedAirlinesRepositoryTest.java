package de.perdian.flightlog.modules.airlines.persistence.impl;

import de.perdian.flightlog.modules.airlines.model.Airline;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

public class OpenflightsBackedAirlinesRepositoryTest {

    @Test
    public void test() throws Exception {
        OpenflightsBackedAirlinesRepository airlinesRepository = new OpenflightsBackedAirlinesRepository();
        airlinesRepository.setResourceLoader(new DefaultResourceLoader());
        airlinesRepository.initialize();
        Airline airline = airlinesRepository.loadAirlineByCode("XJ");
        MatcherAssert.assertThat(airline.getName(), IsEqual.equalTo("Thai AirAsia"));
    }

}
