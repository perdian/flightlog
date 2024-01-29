package de.perdian.flightlog.modules.flights.service;

import de.perdian.flightlog.FlightlogApplication;
import de.perdian.flightlog.modules.flights.lookup.FlightLookupService;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.model.FlightLookupRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class FlightLookupServiceExample {

    private static final Logger log = LoggerFactory.getLogger(FlightLookupServiceExample.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(FlightlogApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        try (ConfigurableApplicationContext applicationContext = springApplication.run(args)) {

            FlightLookupRequest flightLookupRequest = new FlightLookupRequest();
            flightLookupRequest.setAirlineCode("WN");
            flightLookupRequest.setFlightNumber("310");
            flightLookupRequest.setDepartureDate(LocalDate.of(2023, 11, 03));

            FlightLookupService flightLookupService = applicationContext.getBean(FlightLookupService.class);
            List<Flight> flights = flightLookupService.lookupFlights(flightLookupRequest);

            log.info("Found {} flights", flights.size());
            for (int i=0; i < flights.size(); i++) {
                log.info(ToStringBuilder.reflectionToString(flights.get(i), ToStringStyle.MULTI_LINE_STYLE));
            }

        }
    }

}
