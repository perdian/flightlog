package de.perdian.flightlog.modules.users.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {

}
