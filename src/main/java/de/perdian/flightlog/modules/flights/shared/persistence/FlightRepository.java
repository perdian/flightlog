package de.perdian.flightlog.modules.flights.shared.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, UUID>, JpaSpecificationExecutor<FlightEntity> {

}
