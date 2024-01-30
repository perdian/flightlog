package de.perdian.flightlog.modules.flights.exchange;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.flights.shared.persistence.FlightEntity;
import de.perdian.flightlog.modules.flights.shared.persistence.FlightRepository;
import de.perdian.flightlog.support.FlightlogHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
class FlightsExchangeServiceImpl implements FlightsExchangeService {

    private static final Logger log = LoggerFactory.getLogger(FlightsExchangeServiceImpl.class);

    private FlightRepository flightRepository = null;

    @Override
    @Transactional
    public List<FlightsExchangePackageFlight> importPackage(FlightsExchangePackage exchangePackage, User targetUser) {

        List<FlightsExchangePackageFlight> importedFlights = exchangePackage.getFlights()
            .stream()
            .filter(flight -> Boolean.TRUE.equals(flight.getInclude()))
            .toList();

        log.debug("Loading all currently available flights for user: {}", targetUser);
        Specification<FlightEntity> allFlightEntitiesSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), targetUser.getEntity());
        List<FlightEntity> allFlightEntities = this.getFlightRepository().findAll(allFlightEntitiesSpecification);
        log.debug("Synchronizing {} imported flights with {} currently available flights for user: {}", importedFlights.size(), allFlightEntities.size(), targetUser);

        for (FlightsExchangePackageFlight importedFlight : importedFlights) {
            log.trace("Importing flight for user '{}' from: {}", targetUser, importedFlight);
            this.importPackageFlight(importedFlight, targetUser, allFlightEntities);
        }
        return importedFlights;

    }

    private void importPackageFlight(FlightsExchangePackageFlight importedFlight, User targetUser, List<FlightEntity> allFlightEntities) {
        FlightEntity flightEntity = this.findFlightEntityForPackageFlight(importedFlight, allFlightEntities);
        if (flightEntity == null) {
            log.debug("Creating new FlightEntity for package flight: {}", importedFlight);
            flightEntity = new FlightEntity();
            flightEntity.setUser(targetUser.getEntity());
        }
        this.updateFlightEntityFromPackageFlight(flightEntity, importedFlight);
        this.getFlightRepository().save(flightEntity);
    }

    private FlightEntity findFlightEntityForPackageFlight(FlightsExchangePackageFlight packageFlight, List<FlightEntity> flightEntities) {
        for (FlightEntity flightEntity : flightEntities) {
            boolean flightEntityMatching = true;
            flightEntityMatching = flightEntityMatching && Objects.equals(flightEntity.getDepartureAirportCode(), packageFlight.getDepartureAirportCode());
            flightEntityMatching = flightEntityMatching && Objects.equals(flightEntity.getDepartureDateLocal(), packageFlight.getDepartureDateLocal());
            flightEntityMatching = flightEntityMatching && Objects.equals(flightEntity.getDepartureTimeLocal(), packageFlight.getDepartureTimeLocal());
            flightEntityMatching = flightEntityMatching && Objects.equals(flightEntity.getArrivalAirportCode(), packageFlight.getArrivalAirportCode());
            flightEntityMatching = flightEntityMatching && Objects.equals(flightEntity.getArrivalDateLocal(), packageFlight.getArrivalDateLocal());
            flightEntityMatching = flightEntityMatching && Objects.equals(flightEntity.getArrivalTimeLocal(), packageFlight.getArrivalTimeLocal());
            flightEntityMatching = flightEntityMatching && Objects.equals(flightEntity.getAirlineCode(), packageFlight.getAirlineCode());
            flightEntityMatching = flightEntityMatching && Objects.equals(flightEntity.getFlightNumber(), packageFlight.getFlightNumber());
            if (flightEntityMatching) {
                return flightEntity;
            }
        }
        return null;
    }

    private void updateFlightEntityFromPackageFlight(FlightEntity flightEntity, FlightsExchangePackageFlight packageFlight) {
        flightEntity.setAircraftName(packageFlight.getAircraftName());
        flightEntity.setAircraftRegistration(packageFlight.getAircraftRegistration());
        flightEntity.setAircraftType(packageFlight.getAircraftType());
        flightEntity.setAirlineCode(packageFlight.getAirlineCode());
        flightEntity.setAirlineName(packageFlight.getAirlineName());
        flightEntity.setArrivalAirportCode(packageFlight.getArrivalAirportCode());
        flightEntity.setArrivalDateLocal(packageFlight.getArrivalDateLocal());
        flightEntity.setArrivalTimeLocal(packageFlight.getArrivalTimeLocal());
        flightEntity.setCabinClass(packageFlight.getCabinClass());
        flightEntity.setComment(packageFlight.getComment());
        flightEntity.setDepartureAirportCode(packageFlight.getDepartureAirportCode());
        flightEntity.setDepartureDateLocal(packageFlight.getDepartureDateLocal());
        flightEntity.setDepartureTimeLocal(packageFlight.getDepartureTimeLocal());
        flightEntity.setFlightDistance(packageFlight.getFlightDistance());
        flightEntity.setFlightDuration(StringUtils.isEmpty(packageFlight.getFlightDuration()) ? null : (int)FlightlogHelper.parseDuration(packageFlight.getFlightDuration()).toMinutes());
        flightEntity.setFlightNumber(packageFlight.getFlightNumber());
        flightEntity.setFlightReason(packageFlight.getFlightReason());
        flightEntity.setSeatNumber(packageFlight.getSeatNumber());
        flightEntity.setSeatType(packageFlight.getSeatType());
    }

    FlightRepository getFlightRepository() {
        return this.flightRepository;
    }
    @Autowired
    void setFlightRepository(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

}
