package de.perdian.flightlog.modules.authentication.service.userdetails;

public interface FlightlogUserDetailsResolver {

    FlightlogUserDetails resolveUserDetails(String username, String authenticationSource);

}
