package de.perdian.apps.flighttracker.persistence.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.perdian.apps.flighttracker.persistence.entities.UserEntity;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

}
