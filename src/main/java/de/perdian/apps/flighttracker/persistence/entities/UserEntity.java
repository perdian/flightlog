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
    private String password = null;
    private String authenticationSource = null;

    @Override
    public int hashCode() {
        return this.getUserId() == null ? 0 : this.getUserId().hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else {
            return this.getUserId() != null && (that instanceof UserEntity) && this.getUserId().equals(((UserEntity)that).getUserId());
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[userId=").append(this.getUserId());
        result.append(",username=").append(this.getUsername());
        result.append(",authenticationSource=").append(this.getAuthenticationSource());
        return result.append("]").toString();
    }

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
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(length = 64)
    public String getAuthenticationSource() {
        return this.authenticationSource;
    }
    public void setAuthenticationSource(String authenticationSource) {
        this.authenticationSource = authenticationSource;
    }

}
