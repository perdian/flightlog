package de.perdian.flightlog.modules.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
class FlightlogAuthenticationControllerAdvice {

    private FlightlogAuthenticationHelper authenticationHelper = null;

    @ModelAttribute("currentUser")
    FlightlogUser currentUser(FlightlogUser user) {
        return user;
    }

    @ModelAttribute("authenticationHelper")
    FlightlogAuthenticationHelper authenticationHelper() {
        return this.getAuthenticationHelper();
    }

    FlightlogAuthenticationHelper getAuthenticationHelper() {
        return this.authenticationHelper;
    }
    @Autowired
    void setAuthenticationHelper(FlightlogAuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

}
