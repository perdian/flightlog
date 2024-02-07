package de.perdian.flightlog.modules.backup.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BackupRepository extends JpaRepository<BackupEntity, UUID>, JpaSpecificationExecutor<BackupEntity> {

}
