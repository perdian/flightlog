package de.perdian.flightlog.modules.aircrafts.persistence;

public interface AircraftTypesRepository {

    AircraftTypeEntity loadAircraftTypeByCode(String code);
    AircraftTypeEntity loadAircraftTypeByIataCode(String code);
    AircraftTypeEntity loadAircraftTypeByIcaoCode(String code);

}
