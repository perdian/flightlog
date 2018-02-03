package de.perdian.apps.flighttracker.modules.importexport.services;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.perdian.apps.flighttracker.modules.flights.persistence.FlightEntity;
import de.perdian.apps.flighttracker.modules.flights.persistence.FlightsRepository;
import de.perdian.apps.flighttracker.modules.importexport.data.DataItem;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

@Service
class ImportExportServiceImpl implements ImportExportService {

    private FlightsRepository flightsRepository = null;

    @Override
    @Transactional
    public void importDataItems(List<DataItem> dataItems, UserEntity user) {
        for (DataItem dataItem : dataItems) {

            FlightEntity targetEntitiy = this.getFlightsRepository().findAll((root, query, cb) -> {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(cb.equal(root.get("departureAirportCode"), dataItem.getDepartureAirportCode()));
                predicateList.add(cb.equal(root.get("departureDateLocal"), dataItem.getDepartureDateLocal()));
                predicateList.add(cb.equal(root.get("arrivalAirportCode"), dataItem.getArrivalAirportCode()));
                predicateList.add(cb.equal(root.get("arrivalDateLocal"), dataItem.getArrivalDateLocal()));
                predicateList.add(cb.equal(root.get("airlineCode"), dataItem.getAirlineCode()));
                predicateList.add(cb.equal(root.get("flightNumber"), dataItem.getFlightNumber()));
                predicateList.add(user == null ? cb.isNull(root.get("user")) : cb.equal(root.get("user"), user));
                return cb.and(predicateList.toArray(new Predicate[0]));
            }).stream().findFirst().orElseGet(() -> {
                FlightEntity flightEntity = new FlightEntity();
                flightEntity.setUser(user);
                return flightEntity;
            });

            this.copyValues(dataItem, targetEntitiy);
            this.getFlightsRepository().save(targetEntitiy);

        }
    }

    private FlightEntity copyValues(DataItem sourceItem, FlightEntity targetEntitiy) {
        Optional.ofNullable(sourceItem.getAircraftName()).map(String::trim).ifPresent(targetEntitiy::setAircraftName);
        Optional.ofNullable(sourceItem.getAircraftRegistration()).map(String::trim).ifPresent(targetEntitiy::setAircraftRegistration);
        Optional.ofNullable(sourceItem.getAircraftType()).map(String::trim).ifPresent(targetEntitiy::setAircraftType);
        Optional.ofNullable(sourceItem.getAirlineCode()).map(String::trim).ifPresent(targetEntitiy::setAirlineCode);
        Optional.ofNullable(sourceItem.getAirlineName()).map(String::trim).ifPresent(targetEntitiy::setAirlineName);
        Optional.ofNullable(sourceItem.getArrivalAirportCode()).map(String::trim).ifPresent(targetEntitiy::setArrivalAirportCode);
        Optional.ofNullable(sourceItem.getArrivalDateLocal()).ifPresent(targetEntitiy::setArrivalDateLocal);
        Optional.ofNullable(sourceItem.getArrivalTimeLocal()).ifPresent(targetEntitiy::setArrivalTimeLocal);
        Optional.ofNullable(sourceItem.getCabinClass()).ifPresent(targetEntitiy::setCabinClass);
        Optional.ofNullable(sourceItem.getComment()).map(String::trim).ifPresent(targetEntitiy::setComment);
        Optional.ofNullable(sourceItem.getDepartureAirportCode()).map(String::trim).ifPresent(targetEntitiy::setDepartureAirportCode);
        Optional.ofNullable(sourceItem.getDepartureDateLocal()).ifPresent(targetEntitiy::setDepartureDateLocal);
        Optional.ofNullable(sourceItem.getDepartureTimeLocal()).ifPresent(targetEntitiy::setDepartureTimeLocal);
        Optional.ofNullable(sourceItem.getFlightDistance()).ifPresent(targetEntitiy::setFlightDistance);
        Optional.ofNullable(sourceItem.getFlightDuration()).ifPresent(duration -> targetEntitiy.setFlightDuration((int)duration.toMinutes()));
        Optional.ofNullable(sourceItem.getFlightNumber()).map(String::trim).ifPresent(targetEntitiy::setFlightNumber);
        Optional.ofNullable(sourceItem.getFlightReason()).ifPresent(targetEntitiy::setFlightReason);
        Optional.ofNullable(sourceItem.getSeatNumber()).map(String::trim).ifPresent(targetEntitiy::setSeatNumber);
        Optional.ofNullable(sourceItem.getSeatType()).ifPresent(targetEntitiy::setSeatType);
        return targetEntitiy;
    }

    @Override
    public List<DataItem> exportDataItems(UserEntity user) {

        List<FlightEntity> flightEntities = this.getFlightsRepository().findAll((root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(user == null ? cb.isNull(root.get("user")) : cb.equal(root.get("user"), user));
            return cb.and(predicateList.toArray(new Predicate[0]));
        });

        return flightEntities.stream()
            .map(entity -> this.copyValues(entity, new DataItem()))
            .sorted(new DataItem.DepartureTimeComparator())
            .collect(Collectors.toList());
    }

    private DataItem copyValues(FlightEntity sourceEntity, DataItem targetItem) {
        targetItem.setAircraftName(sourceEntity.getAircraftName());
        targetItem.setAircraftRegistration(sourceEntity.getAircraftRegistration());
        targetItem.setAircraftType(sourceEntity.getAircraftType());
        targetItem.setAirlineCode(sourceEntity.getAirlineCode());
        targetItem.setAirlineName(sourceEntity.getAirlineName());
        targetItem.setArrivalAirportCode(sourceEntity.getArrivalAirportCode());
        targetItem.setArrivalDateLocal(sourceEntity.getArrivalDateLocal());
        targetItem.setArrivalTimeLocal(sourceEntity.getArrivalTimeLocal());
        targetItem.setCabinClass(sourceEntity.getCabinClass());
        targetItem.setComment(sourceEntity.getComment());
        targetItem.setDepartureAirportCode(sourceEntity.getDepartureAirportCode());
        targetItem.setDepartureDateLocal(sourceEntity.getDepartureDateLocal());
        targetItem.setDepartureTimeLocal(sourceEntity.getDepartureTimeLocal());
        targetItem.setFlightDistance(sourceEntity.getFlightDistance());
        targetItem.setFlightDuration(sourceEntity.getFlightDuration() == null ? null : Duration.ofMinutes(sourceEntity.getFlightDuration()));
        targetItem.setFlightNumber(sourceEntity.getFlightNumber());
        targetItem.setFlightReason(sourceEntity.getFlightReason());
        targetItem.setSeatNumber(sourceEntity.getSeatNumber());
        targetItem.setSeatType(sourceEntity.getSeatType());
        return targetItem;
    }

    FlightsRepository getFlightsRepository() {
        return this.flightsRepository;
    }
    @Autowired
    void setFlightsRepository(FlightsRepository flightsRepository) {
        this.flightsRepository = flightsRepository;
    }

}
