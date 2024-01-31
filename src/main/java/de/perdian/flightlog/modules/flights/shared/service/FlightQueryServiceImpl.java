package de.perdian.flightlog.modules.flights.shared.service;

import de.perdian.flightlog.modules.aircrafts.model.Aircraft;
import de.perdian.flightlog.modules.airlines.model.Airline;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;
import de.perdian.flightlog.modules.airports.model.Airport;
import de.perdian.flightlog.modules.airports.model.AirportContact;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.persistence.FlightEntity;
import de.perdian.flightlog.modules.flights.shared.persistence.FlightRepository;
import de.perdian.flightlog.support.FlightlogHelper;
import de.perdian.flightlog.support.types.FlightType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
class FlightQueryServiceImpl implements FlightQueryService {

    private static final List<String> COUNTRY_CODES_EUROPE = Arrays.asList("AD", "AL", "AT", "BA", "BE", "BG", "BY", "CH", "CY", "CZ", "DE", "DK", "EE", "ES", "FI", "FR", "GB", "GG", "GI", "GR", "HR", "HU", "IE", "IM", "IS", "IT", "JE", "LI", "LT", "LU", "LV", "MC", "MD", "MK", "MT", "NL", "NO", "PL", "PT", "RO", "SE", "SI", "SJ", "SK", "SM", "UA", "UK", "VA");

    private FlightRepository flightRepository = null;
    private AirlinesRepository airlinesRepository = null;
    private AirportsRepository airportsRepository = null;

    @Override
    public List<Flight> loadFlights(FlightQuery flightQuery) {
        return this.getFlightRepository().findAll(this.createFlightsSpecification(flightQuery)).stream()
            .map(this::createFlight)
            .filter(flightQuery)
            .sorted(flightQuery.getComparator())
            .toList();
    }

    private Specification<FlightEntity> createFlightsSpecification(FlightQuery flightQuery) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), flightQuery.getUser() == null ? null : flightQuery.getUser().getEntity());
    }

    private Flight createFlight(FlightEntity flightEntity) {

        AirportContact departureContact = this.createFlightDepartureContact(flightEntity);
        AirportContact arrivalContact = this.createFlightArrivalContact(flightEntity);

        Flight flight = new Flight();
        flight.setAircraft(this.createFlightAircraft(flightEntity));
        flight.setAirline(this.createFlightAirline(flightEntity));
        flight.setArrivalContact(arrivalContact);
        flight.setAverageSpeed(this.createFlightAverageSpeed(flightEntity));
        flight.setCabinClass(flightEntity.getCabinClass());
        flight.setComment(flightEntity.getComment());
        flight.setDepartureContact(departureContact);
        flight.setEntityId(flightEntity.getId());
        flight.setFlightDistance(flightEntity.getFlightDistance());
        flight.setFlightDuration(flightEntity.getFlightDuration() == null ? null : Duration.ofMinutes(flightEntity.getFlightDuration()));
        flight.setFlightNumber(flightEntity.getFlightNumber());
        flight.setFlightReason(flightEntity.getFlightReason());
        flight.setFlightType(this.computeFlightType(departureContact.getAirport(), arrivalContact.getAirport()));
        flight.setSeatNumber(flightEntity.getSeatNumber());
        flight.setSeatType(flightEntity.getSeatType());
        flight.setUser(flightEntity.getUser());
        return flight;

    }

    private Aircraft createFlightAircraft(FlightEntity flightEntity) {
        Aircraft aircraft = new Aircraft();
        aircraft.setName(flightEntity.getAircraftName());
        aircraft.setRegistration(flightEntity.getAircraftRegistration());
        aircraft.setType(flightEntity.getAircraftType());
        return aircraft;
    }

    private Airline createFlightAirline(FlightEntity flightEntity) {
        Airline airline = this.getAirlinesRepository().loadAirlineByCode(flightEntity.getAirlineCode());
        if (airline == null) {
            airline = new Airline();
            airline.setCode(flightEntity.getAirlineCode());
            airline.setName(flightEntity.getAirlineName());
        }
        if (StringUtils.isNotEmpty(flightEntity.getAirlineName()) && !StringUtils.equalsIgnoreCase(flightEntity.getAirlineName(), airline.getName())) {
            airline = airline.clone();
            airline.setName(flightEntity.getAirlineName());
        }
        return airline;
    }

    private AirportContact createFlightArrivalContact(FlightEntity flightEntity) {
        Airport airport = this.createFlightAirport(flightEntity.getArrivalAirportCode());
        AirportContact airportContact = new AirportContact();
        airportContact.setAirport(this.createFlightAirport(flightEntity.getArrivalAirportCode()));
        airportContact.setDateLocal(flightEntity.getArrivalDateLocal());
        airportContact.setDateOffset(FlightlogHelper.computeOffsetDays(flightEntity.getDepartureDateLocal(), flightEntity.getArrivalDateLocal()));
        airportContact.setDateTimeUtc(airport.computeInstant(flightEntity.getArrivalDateLocal(), flightEntity.getArrivalTimeLocal()));
        airportContact.setTimeLocal(flightEntity.getArrivalTimeLocal());
        return airportContact;
    }

    private AirportContact createFlightDepartureContact(FlightEntity flightEntity) {
        Airport airport = this.createFlightAirport(flightEntity.getDepartureAirportCode());
        AirportContact airportContact = new AirportContact();
        airportContact.setAirport(this.createFlightAirport(flightEntity.getDepartureAirportCode()));
        airportContact.setDateLocal(flightEntity.getDepartureDateLocal());
        airportContact.setDateTimeUtc(airport.computeInstant(flightEntity.getDepartureDateLocal(), flightEntity.getDepartureTimeLocal()));
        airportContact.setTimeLocal(flightEntity.getDepartureTimeLocal());
        return airportContact;
    }

    private Airport createFlightAirport(String airportCode) {
        Airport airport = this.getAirportsRepository().loadAirportByIataCode(airportCode);
        if (airport == null) {
            airport = new Airport();
            airport.setCode(airportCode);
        }
        return airport;
    }

    private Double createFlightAverageSpeed(FlightEntity flightEntity) {
        if (flightEntity.getFlightDistance() != null && flightEntity.getFlightDuration() != null) {
            return flightEntity.getFlightDistance().doubleValue() / (flightEntity.getFlightDuration().doubleValue() / 60d);
        } else {
            return null;
        }
    }

    private FlightType computeFlightType(Airport departureAirport, Airport arrivalAirport) {
        if (StringUtils.isNotEmpty(departureAirport.getCountryCode()) && StringUtils.isNotEmpty(arrivalAirport.getCountryCode())) {
            if (Objects.equals(departureAirport.getCountryCode(), arrivalAirport.getCountryCode())) {
                return FlightType.DOMESTIC;
            } else if (COUNTRY_CODES_EUROPE.contains(departureAirport.getCountryCode()) && COUNTRY_CODES_EUROPE.contains(arrivalAirport.getCountryCode())) {
                return FlightType.EUROPE;
            } else {
                return FlightType.INTERNATIONAL;
            }
        } else {
            return null;
        }
    }

    FlightRepository getFlightRepository() {
        return this.flightRepository;
    }
    @Autowired
    void setFlightRepository(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

}
