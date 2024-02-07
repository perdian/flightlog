package de.perdian.flightlog.modules.backup.persistence;

import de.perdian.flightlog.modules.authentication.persistence.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "flightlog_backup")
public class BackupEntity implements Serializable {

    static final long serialVersionUID = 1L;

    private UUID backupId = null;
    private UserEntity user = null;
    private Instant backupInstant = null;
    private Instant latestFlightUpdateInstant = null;

    @Override
    public int hashCode() {
        return this.getBackupId() == null ? 0 : this.getBackupId().hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else {
            return this.getBackupId() != null && (that instanceof BackupEntity) && this.getBackupId().equals(((BackupEntity)that).getBackupId());
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[backupId=").append(this.getBackupId());
        result.append("user=").append(this.getUser());
        result.append(",backupInstant=").append(this.getBackupInstant());
        result.append(",latestFlightUpdateInstant=").append(this.getLatestFlightUpdateInstant());
        return result.append("]").toString();
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    public UUID getBackupId() {
        return this.backupId;
    }
    public void setBackupId(UUID backupId) {
        this.backupId = backupId;
    }

    @ManyToOne(optional = true)
    public UserEntity getUser() {
        return this.user;
    }
    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Instant getBackupInstant() {
        return this.backupInstant;
    }
    public void setBackupInstant(Instant backupInstant) {
        this.backupInstant = backupInstant;
    }

    public Instant getLatestFlightUpdateInstant() {
        return this.latestFlightUpdateInstant;
    }
    public void setLatestFlightUpdateInstant(Instant latestFlightUpdateInstant) {
        this.latestFlightUpdateInstant = latestFlightUpdateInstant;
    }

}
