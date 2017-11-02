package de.perdian.apps.flighttracker.modules.flights.persistence;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightsRepository extends CrudRepository<FlightEntity, Long>, JpaSpecificationExecutor<FlightEntity> {

}
