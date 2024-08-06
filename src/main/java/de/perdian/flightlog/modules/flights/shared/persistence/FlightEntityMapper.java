package de.perdian.flightlog.modules.flights.shared.persistence;

import de.perdian.flightlog.modules.aircrafts.model.Aircraft;
import de.perdian.flightlog.modules.airlines.model.Airline;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;
import de.perdian.flightlog.modules.airports.model.Airport;
import de.perdian.flightlog.modules.airports.model.AirportContact;
import de.perdian.flightlog.modules.airports.persistence.AirportsRepository;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.support.FlightlogHelper;
import de.perdian.flightlog.support.types.FlightDistance;
import de.perdian.flightlog.support.types.FlightType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class FlightEntityMapper {

    private static final List<String> COUNTRY_CODES_EUROPE = Arrays.asList("AD", "AL", "AT", "BA", "BE", "BG", "BY", "CH", "CY", "CZ", "DE", "DK", "EE", "ES", "FI", "FR", "GB", "GG", "GI", "GR", "HR", "HU", "IE", "IM", "IS", "IT", "JE", "LI", "LT", "LU", "LV", "MC", "MD", "MK", "MT", "NL", "NO", "PL", "PT", "RO", "SE", "SI", "SJ", "SK", "SM", "UA", "UK", "VA");

    private AirlinesRepository airlinesRepository = null;
    private AirportsRepository airportsRepository = null;

    public Flight createModel(FlightEntity sourceEntity) {

        AirportContact departureContact = this.createFlightDepartureContact(sourceEntity);
        AirportContact arrivalContact = this.createFlightArrivalContact(sourceEntity);

        Flight flight = new Flight();
        flight.setAircraft(this.createFlightAircraft(sourceEntity));
        flight.setAirline(this.createFlightAirline(sourceEntity));
        flight.setArrivalContact(arrivalContact);
        flight.setAverageSpeed(this.createFlightAverageSpeed(sourceEntity));
        flight.setCabinClass(sourceEntity.getCabinClass());
        flight.setComment(sourceEntity.getComment());
        flight.setDepartureContact(departureContact);
        flight.setEntityId(sourceEntity.getId());
        flight.setFlightDistance(sourceEntity.getFlightDistance());
        flight.setFlightDistanceType(this.computeFlightDistance(sourceEntity.getFlightDistance()));
        flight.setFlightDuration(sourceEntity.getFlightDuration() == null ? null : Duration.ofMinutes(sourceEntity.getFlightDuration()));
        flight.setFlightNumber(sourceEntity.getFlightNumber());
        flight.setFlightReason(sourceEntity.getFlightReason());
        flight.setFlightType(this.computeFlightType(departureContact.getAirport(), arrivalContact.getAirport()));
        flight.setSeatNumber(sourceEntity.getSeatNumber());
        flight.setSeatType(sourceEntity.getSeatType());
        flight.setUser(sourceEntity.getUser());
        return flight;

    }

    private Aircraft createFlightAircraft(FlightEntity sourceEntity) {
        Aircraft aircraft = new Aircraft();
        aircraft.setName(sourceEntity.getAircraftName());
        aircraft.setRegistration(sourceEntity.getAircraftRegistration());
        aircraft.setType(sourceEntity.getAircraftType());
        return aircraft;
    }

    private Airline createFlightAirline(FlightEntity sourceEntity) {
        Airline airline = this.getAirlinesRepository().loadAirlineByCode(sourceEntity.getAirlineCode());
        if (airline == null) {
            airline = new Airline();
            airline.setCode(sourceEntity.getAirlineCode());
            airline.setName(sourceEntity.getAirlineName());
        }
        if (StringUtils.isNotEmpty(sourceEntity.getAirlineName()) && !StringUtils.equalsIgnoreCase(sourceEntity.getAirlineName(), airline.getName())) {
            airline = airline.clone();
            airline.setName(sourceEntity.getAirlineName());
        }
        return airline;
    }

    private AirportContact createFlightArrivalContact(FlightEntity sourceEntity) {
        Airport airport = this.createFlightAirport(sourceEntity.getArrivalAirportCode());
        AirportContact airportContact = new AirportContact();
        airportContact.setAirport(this.createFlightAirport(sourceEntity.getArrivalAirportCode()));
        airportContact.setDateLocal(sourceEntity.getArrivalDateLocal());
        airportContact.setDateOffset(FlightlogHelper.computeOffsetDays(sourceEntity.getDepartureDateLocal(), sourceEntity.getArrivalDateLocal()));
        airportContact.setDateTimeUtc(airport.computeInstant(sourceEntity.getArrivalDateLocal(), sourceEntity.getArrivalTimeLocal()));
        airportContact.setTimeLocal(sourceEntity.getArrivalTimeLocal());
        return airportContact;
    }

    private AirportContact createFlightDepartureContact(FlightEntity sourceEntity) {
        Airport airport = this.createFlightAirport(sourceEntity.getDepartureAirportCode());
        AirportContact airportContact = new AirportContact();
        airportContact.setAirport(this.createFlightAirport(sourceEntity.getDepartureAirportCode()));
        airportContact.setDateLocal(sourceEntity.getDepartureDateLocal());
        airportContact.setDateTimeUtc(airport.computeInstant(sourceEntity.getDepartureDateLocal(), sourceEntity.getDepartureTimeLocal()));
        airportContact.setTimeLocal(sourceEntity.getDepartureTimeLocal());
        return airportContact;
    }

    private Airport createFlightAirport(String airportCode) {
        Airport airport = this.getAirportsRepository().loadAirportByCode(airportCode);
        if (airport == null) {
            airport = new Airport();
            airport.setCode(airportCode);
        }
        return airport;
    }

    private Double createFlightAverageSpeed(FlightEntity sourceEntity) {
        if (sourceEntity.getFlightDistance() != null && sourceEntity.getFlightDuration() != null) {
            return sourceEntity.getFlightDistance().doubleValue() / (sourceEntity.getFlightDuration().doubleValue() / 60d);
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

    private FlightDistance computeFlightDistance(Integer flightDistanceValue) {
        if (flightDistanceValue != null) {
            for (FlightDistance flightDistance : FlightDistance.values()) {
                if (flightDistance.matches(flightDistanceValue)) {
                    return flightDistance;
                }
            }
        }
        return null;
    }

    public void applyModel(Flight flight, FlightEntity targetEntity) {
        targetEntity.setAircraftName(flight.getAircraft().getName());
        targetEntity.setAircraftRegistration(flight.getAircraft().getRegistration());
        targetEntity.setAircraftType(flight.getAircraft().getType());
        targetEntity.setAirlineCode(flight.getAirline().getCode());
        targetEntity.setAirlineName(flight.getAirline().getName());
        targetEntity.setArrivalAirportCode(flight.getArrivalContact().getAirport().getCode());
        targetEntity.setArrivalDateLocal(flight.getArrivalContact().getDateLocal());
        targetEntity.setArrivalTimeLocal(flight.getArrivalContact().getTimeLocal());
        targetEntity.setCabinClass(flight.getCabinClass());
        targetEntity.setComment(flight.getComment());
        targetEntity.setDepartureAirportCode(flight.getDepartureContact().getAirport().getCode());
        targetEntity.setDepartureDateLocal(flight.getDepartureContact().getDateLocal());
        targetEntity.setDepartureTimeLocal(flight.getDepartureContact().getTimeLocal());
        targetEntity.setFlightDistance(flight.getFlightDistance());
        targetEntity.setFlightDuration(flight.getFlightDuration () == null ? null : (int)flight.getFlightDuration().toMinutes());
        targetEntity.setFlightNumber(flight.getFlightNumber());
        targetEntity.setFlightReason(flight.getFlightReason());
        targetEntity.setSeatNumber(flight.getSeatNumber());
        targetEntity.setSeatType(flight.getSeatType());
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
