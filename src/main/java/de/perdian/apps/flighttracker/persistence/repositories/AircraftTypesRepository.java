package de.perdian.apps.flighttracker.persistence.repositories;

import de.perdian.apps.flighttracker.persistence.entities.AircraftTypeEntity;

public interface AircraftTypesRepository {

    AircraftTypeEntity loadAircraftTypeByCode(String code);
    AircraftTypeEntity loadAircraftTypeByIataCode(String code);
    AircraftTypeEntity loadAircraftTypeByIcaoCode(String code);

}
