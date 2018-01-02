package de.perdian.apps.flighttracker.modules.airlines.persistence;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineRepository extends CrudRepository<AirlineEntity, Long>, JpaSpecificationExecutor<AirlineEntity> {

}
