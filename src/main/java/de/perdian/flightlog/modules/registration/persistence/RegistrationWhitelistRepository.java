package de.perdian.flightlog.modules.registration.persistence;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationWhitelistRepository extends CrudRepository<RegistrationWhitelistEntity, Long>, JpaSpecificationExecutor<RegistrationWhitelistEntity> {

}
