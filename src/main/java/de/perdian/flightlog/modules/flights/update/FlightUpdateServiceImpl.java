package de.perdian.flightlog.modules.flights.update;

import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetails;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.persistence.FlightEntity;
import de.perdian.flightlog.modules.flights.shared.persistence.FlightEntityMapper;
import de.perdian.flightlog.modules.flights.shared.persistence.FlightRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
class FlightUpdateServiceImpl implements FlightUpdateService {

    private FlightEntityMapper flightEntityMapper = null;
    private FlightRepository flightRepository = null;

    @Override
    @Transactional
    public Flight saveFlight(Flight flight, FlightlogUserDetails user) {

        FlightEntity flightEntity = this.loadFlightEntity(flight, user);
        if (flightEntity == null) {
            flightEntity = new FlightEntity();
            flightEntity.setUser(user.getUserEntity());
        }

        this.getFlightEntityMapper().applyModel(flight, flightEntity);

        FlightEntity savedFlightEntity = this.getFlightRepository().save(flightEntity);
        return this.getFlightEntityMapper().createModel(savedFlightEntity);

    }

    @Override
    @Transactional
    public void deleteFlight(Flight flight, FlightlogUserDetails user) {
        FlightEntity flightEntity = this.loadFlightEntity(flight, user);
        if (flightEntity != null) {
            this.getFlightRepository().delete(flightEntity);
        }
    }

    private FlightEntity loadFlightEntity(Flight flight, FlightlogUserDetails user) {
        Specification<FlightEntity> flightEntitySpecification = (root, query, criteriaBuilder) -> criteriaBuilder.and(
            criteriaBuilder.equal(root.get("user"), user == null ? null : user.getUserEntity()),
            criteriaBuilder.equal(root.get("id"),flight.getEntityId())
        );
        return flight.getEntityId() == null ? null : this.getFlightRepository().findOne(flightEntitySpecification).orElseThrow(() -> new IllegalArgumentException("Cannot find flight for ID: " + flight.getEntityId()));
    }

    FlightEntityMapper getFlightEntityMapper() {
        return this.flightEntityMapper;
    }
    @Autowired
    void setFlightEntityMapper(FlightEntityMapper flightEntityMapper) {
        this.flightEntityMapper = flightEntityMapper;
    }

    FlightRepository getFlightRepository() {
        return this.flightRepository;
    }
    @Autowired
    void setFlightRepository(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

}
