package de.perdian.flightlog.modules.flights.service;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.flights.service.model.Flight;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
class FlightUpdateServiceImpl implements FlightUpdateService {

    @Override
    @Transactional
    public Flight saveFlight(Flight flight, User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public void deleteFlight(Flight flight, User user) {
        throw new UnsupportedOperationException();
    }

}
