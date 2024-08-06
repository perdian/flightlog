package de.perdian.flightlog.modules.aircrafts.persistence;

import de.perdian.flightlog.modules.aircrafts.model.AircraftType;

public interface AircraftTypesRepository {

    AircraftType loadAircraftTypeByCode(String code);

}
