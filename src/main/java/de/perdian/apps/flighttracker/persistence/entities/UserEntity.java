package de.perdian.apps.flighttracker.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity implements Serializable {

    static final long serialVersionUID = 1L;

    private Long userId = null;
    private String username = null;
    private String authenticationSource = null;
    private String authenticationSourceId = null;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(length = 64)
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(length = 64)
    public String getAuthenticationSource() {
        return this.authenticationSource;
    }
    public void setAuthenticationSource(String authenticationSource) {
        this.authenticationSource = authenticationSource;
    }

    @Column(length = 64)
    public String getAuthenticationSourceId() {
        return this.authenticationSourceId;
    }
    public void setAuthenticationSourceId(String authenticationSourceId) {
        this.authenticationSourceId = authenticationSourceId;
    }

}
