package de.perdian.apps.flighttracker.modules.flights.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightsRepository extends CrudRepository<FlightEntity, UUID>, JpaSpecificationExecutor<FlightEntity> {

}
