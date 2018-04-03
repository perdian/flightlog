package de.perdian.flightlog.modules.airlines.persistence;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlinesRepository extends CrudRepository<AirlineEntity, String>, JpaSpecificationExecutor<AirlineEntity> {

}
