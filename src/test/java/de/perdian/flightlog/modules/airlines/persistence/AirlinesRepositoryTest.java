package de.perdian.flightlog.modules.airlines.persistence;

import de.perdian.flightlog.modules.airlines.model.Airline;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

public class AirlinesRepositoryTest {

    @Test
    public void test() throws Exception {
        AirlinesRepositoryImpl airlinesRepository = new AirlinesRepositoryImpl();
        airlinesRepository.setResourceLoader(new DefaultResourceLoader());
        airlinesRepository.initialize();
        Airline airline = airlinesRepository.loadAirlineByCode("XJ");
        MatcherAssert.assertThat(airline.getName(), IsEqual.equalTo("Thai AirAsia"));
    }

}
