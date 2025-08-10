package de.perdian.flightlog.modules.flights.exchange;

import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetails;
import de.perdian.flightlog.modules.flights.shared.persistence.FlightEntity;
import de.perdian.flightlog.modules.flights.shared.persistence.FlightRepository;
import de.perdian.flightlog.support.FlightlogHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
class FlightsExchangeServiceImpl implements FlightsExchangeService {

    private static final Logger log = LoggerFactory.getLogger(FlightsExchangeServiceImpl.class);

    private FlightRepository flightRepository = null;

    @Override
    @Transactional
    public List<FlightsExchangePackageFlight> importPackage(FlightsExchangePackage exchangePackage, FlightlogUserDetails targetUser) {

        List<FlightsExchangePackageFlight> importedFlights = exchangePackage.getFlights()
            .stream()
            .filter(flight -> Boolean.TRUE.equals(flight.getInclude()))
            .toList();

        log.debug("Loading all currently available flights for user: {}", targetUser);
        List<FlightEntity> allFlightEntities = this.loadAllFlightEntities(targetUser);
        log.debug("Synchronizing {} imported flights with {} currently available flights for user: {}", importedFlights.size(), allFlightEntities.size(), targetUser);

        for (FlightsExchangePackageFlight importedFlight : importedFlights) {
            log.trace("Importing flight for user '{}' from: {}", targetUser, importedFlight);
            this.importPackageFlight(importedFlight, targetUser, allFlightEntities);
        }
        return importedFlights;

    }

    private void importPackageFlight(FlightsExchangePackageFlight importedFlight, FlightlogUserDetails targetUser, List<FlightEntity> allFlightEntities) {
        FlightEntity flightEntity = this.findFlightEntityForPackageFlight(importedFlight, allFlightEntities);
        if (flightEntity == null) {
            log.debug("Creating new FlightEntity for package flight: {}", importedFlight);
            flightEntity = new FlightEntity();
            flightEntity.setUser(targetUser.getUserEntity());
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

    @Override
    public FlightsExchangePackage createPackage(FlightlogUserDetails sourceUser) {

        List<FlightEntity> flightEntities = this.loadAllFlightEntities(sourceUser);

        List<FlightsExchangePackageFlight> exchangePackageFlights = flightEntities.stream()
            .map(flightEntity -> this.createPackageFlight(flightEntity))
            .toList();

        Instant latestUpdateTime = flightEntities.stream()
            .map(flightEntity -> flightEntity.getLastUpdatedAt())
            .filter(updatedAt -> updatedAt != null)
            .max(Comparator.naturalOrder())
            .orElseGet(() -> Instant.now());

        log.debug("Exporting {} flights for user: {}", flightEntities.size(), sourceUser);
        FlightsExchangePackage exchangePackage = new FlightsExchangePackage();
        exchangePackage.setFlights(exchangePackageFlights);
        exchangePackage.setCreationTime(latestUpdateTime);
        return exchangePackage;

    }

    private FlightsExchangePackageFlight createPackageFlight(FlightEntity flightEntity) {
        FlightsExchangePackageFlight flight = new FlightsExchangePackageFlight();
        flight.setAircraftName(flightEntity.getAircraftName());
        flight.setAircraftRegistration(flightEntity.getAircraftRegistration());
        flight.setAircraftType(flightEntity.getAircraftType());
        flight.setAirlineCode(flightEntity.getAirlineCode());
        flight.setAirlineName(flightEntity.getAirlineName());
        flight.setArrivalAirportCode(flightEntity.getArrivalAirportCode());
        flight.setArrivalDateLocal(flightEntity.getArrivalDateLocal());
        flight.setArrivalTimeLocal(flightEntity.getArrivalTimeLocal());
        flight.setCabinClass(flightEntity.getCabinClass());
        flight.setComment(flightEntity.getComment());
        flight.setDepartureAirportCode(flightEntity.getDepartureAirportCode());
        flight.setDepartureDateLocal(flightEntity.getDepartureDateLocal());
        flight.setDepartureTimeLocal(flightEntity.getDepartureTimeLocal());
        flight.setFlightDistance(flightEntity.getFlightDistance());
        flight.setFlightDuration(flightEntity.getFlightDuration() == null ? null : FlightlogHelper.formatDuration(Duration.ofMinutes(flightEntity.getFlightDuration())));
        flight.setFlightNumber(flightEntity.getFlightNumber());
        flight.setFlightReason(flightEntity.getFlightReason());
        flight.setLastUpdatedAt(flightEntity.getLastUpdatedAt());
        flight.setSeatNumber(flightEntity.getSeatNumber());
        flight.setSeatType(flightEntity.getSeatType());
        return flight;
    }

    private List<FlightEntity> loadAllFlightEntities(FlightlogUserDetails user) {
        Specification<FlightEntity> allFlightEntitiesSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user.getUserEntity());
        Sort allFlightEntitiesSort = Sort.by(Sort.Order.asc("departureDateLocal"), Sort.Order.asc("departureTimeLocal"));
        return this.getFlightRepository().findAll(allFlightEntitiesSpecification);
    }

    FlightRepository getFlightRepository() {
        return this.flightRepository;
    }
    @Autowired
    void setFlightRepository(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

}
