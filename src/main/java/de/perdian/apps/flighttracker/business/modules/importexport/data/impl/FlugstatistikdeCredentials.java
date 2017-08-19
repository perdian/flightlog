package de.perdian.apps.flighttracker.business.modules.importexport.data.impl;

import java.io.Serializable;

public class FlugstatistikdeCredentials implements Serializable {

    static final long serialVersionUID = 1L;

    private String username = null;
    private String password = null;

    public FlugstatistikdeCredentials(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'username' must not be empty!");
        } else if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'password' must not be empty!");
        } else {
            this.setUsername(username);
            this.setPassword(password);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getClass().getSimpleName());
        result.append("[username=").append(this.getUsername());
        result.append(",password=***");
        return result.append("]").toString();
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
