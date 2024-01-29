package de.perdian.flightlog.modules.flights.update;

import de.perdian.flightlog.modules.authentication.User;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
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
