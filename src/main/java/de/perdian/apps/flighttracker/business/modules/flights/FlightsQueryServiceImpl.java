package de.perdian.apps.flighttracker.business.modules.flights;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import de.perdian.apps.flighttracker.business.modules.flights.model.AircraftBean;
import de.perdian.apps.flighttracker.business.modules.flights.model.AirlineBean;
import de.perdian.apps.flighttracker.business.modules.flights.model.AirportBean;
import de.perdian.apps.flighttracker.business.modules.flights.model.AirportContactBean;
import de.perdian.apps.flighttracker.business.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.persistence.entities.AirlineEntity;
import de.perdian.apps.flighttracker.persistence.entities.AirportEntity;
import de.perdian.apps.flighttracker.persistence.entities.FlightEntity;
import de.perdian.apps.flighttracker.persistence.repositories.AirlinesRepository;
import de.perdian.apps.flighttracker.persistence.repositories.AirportsRepository;
import de.perdian.apps.flighttracker.persistence.repositories.FlightsRepository;
import de.perdian.apps.flighttracker.persistence.support.PaginatedList;
import de.perdian.apps.flighttracker.persistence.support.PaginationData;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;

@Service
class FlightsQueryServiceImpl implements FlightsQueryService {

    private FlightsRepository flightsRepository = null;
    private AirportsRepository airportsRepository = null;
    private AirlinesRepository airlinesRepository = null;

    @Override
    public PaginatedList<FlightBean> loadFlights(FlightsQuery flightsQuery) {

        Sort sort = new Sort(new Sort.Order(Direction.DESC, "departureDateLocal"), new Sort.Order(Direction.DESC, "departureTimeLocal"), new Sort.Order(Direction.DESC, "id"));
        PageRequest pageRequest = new PageRequest(flightsQuery.getPage() == null ? 0 : flightsQuery.getPage(), flightsQuery.getPageSize() == null ? Integer.MAX_VALUE : flightsQuery.getPageSize(), sort);

        Specification<FlightEntity> flightEntitiesSpecification = (root, query, cb) -> flightsQuery.toPredicate(root, query, cb);
        Page<FlightEntity> flightEntities = this.getFlightsRepository().findAll(flightEntitiesSpecification, pageRequest);

        PaginatedList<FlightBean> resultList = new PaginatedList<>();
        resultList.setPagination(new PaginationData(flightEntities.getNumber(), flightEntities.getTotalPages()));
        resultList.setItems(flightEntities.getContent().stream().map(this::convertFlightEntitiy).collect(Collectors.toList()));
        return resultList;

    }

    @Override
    public FlightBean loadFlightById(Long id) {
        return this.convertFlightEntitiy(this.getFlightsRepository().findOne(id));
    }

    private FlightBean convertFlightEntitiy(FlightEntity flightEntitiy) {

        AircraftBean aircraftBean = new AircraftBean();
        aircraftBean.setName(flightEntitiy.getAircraftName());
        aircraftBean.setRegistration(flightEntitiy.getAircraftRegistration());
        aircraftBean.setType(flightEntitiy.getAircraftType());

        AirlineBean airlineBean = new AirlineBean();
        airlineBean.setCode(flightEntitiy.getAirlineCode());
        airlineBean.setName(flightEntitiy.getAirlineName());
        AirlineEntity airlineEntity = StringUtils.isEmpty(flightEntitiy.getAirlineCode()) ? null : this.getAirlinesRepository().loadAirlineByIataCode(flightEntitiy.getAirlineCode());
        if (airlineEntity != null && StringUtils.isEmpty(airlineBean.getName())) {
            airlineBean.setName(airlineEntity.getName());
        }

        Instant departureDateTimeUtc = null;
        AirportBean departureAirportBean = new AirportBean();
        departureAirportBean.setCode(flightEntitiy.getDepartureAirportCode());
        AirportEntity departureAirportEntity = this.getAirportsRepository().loadAirportByIataCode(flightEntitiy.getDepartureAirportCode());
        if (departureAirportEntity != null) {
            departureAirportBean.setCountryCode(departureAirportEntity.getCountryCode());
            departureAirportBean.setName(departureAirportEntity.getName());
            departureAirportBean.setLatitude(departureAirportEntity.getLatitude());
            departureAirportBean.setLongitude(departureAirportEntity.getLongitude());
            departureAirportBean.setTimezoneId(departureAirportEntity.getTimezoneId());
            departureAirportBean.setTimezoneOffset(departureAirportEntity.getTimezoneOffset());
            if (StringUtils.isEmpty(departureAirportBean.getCountryCode())) {
                departureAirportBean.setCountryCode(departureAirportEntity.getCountryCode());
            }
            if (StringUtils.isEmpty(departureAirportBean.getName())) {
                departureAirportBean.setName(departureAirportEntity.getName());
            }
            if (departureAirportEntity.getTimezoneId() != null && flightEntitiy.getDepartureDateLocal() != null && flightEntitiy.getDepartureTimeLocal() != null) {
                departureDateTimeUtc = flightEntitiy.getDepartureTimeLocal().atDate(flightEntitiy.getDepartureDateLocal()).atZone(departureAirportEntity.getTimezoneId()).toInstant();
            }
        }

        AirportContactBean departureContactBean = new AirportContactBean();
        departureContactBean.setAirport(departureAirportBean);
        departureContactBean.setDateLocal(flightEntitiy.getDepartureDateLocal());
        departureContactBean.setDateTimeUtc(departureDateTimeUtc);
        departureContactBean.setTimeLocal(flightEntitiy.getDepartureTimeLocal());

        Instant arrivalDateTimeUtc = null;
        AirportBean arrivalAirportBean = new AirportBean();
        arrivalAirportBean.setCode(flightEntitiy.getArrivalAirportCode());
        AirportEntity arrivalAirportEntity = this.getAirportsRepository().loadAirportByIataCode(flightEntitiy.getArrivalAirportCode());
        if (arrivalAirportEntity != null) {
            arrivalAirportBean.setCountryCode(arrivalAirportEntity.getCountryCode());
            arrivalAirportBean.setName(arrivalAirportEntity.getName());
            arrivalAirportBean.setLatitude(arrivalAirportEntity.getLatitude());
            arrivalAirportBean.setLongitude(arrivalAirportEntity.getLongitude());
            arrivalAirportBean.setTimezoneId(arrivalAirportEntity.getTimezoneId());
            arrivalAirportBean.setTimezoneOffset(arrivalAirportEntity.getTimezoneOffset());
            if (StringUtils.isEmpty(arrivalAirportBean.getCountryCode())) {
                arrivalAirportBean.setCountryCode(arrivalAirportEntity.getCountryCode());
            }
            if (StringUtils.isEmpty(arrivalAirportBean.getName())) {
                arrivalAirportBean.setName(arrivalAirportEntity.getName());
            }
            if (arrivalAirportEntity.getTimezoneId() != null && flightEntitiy.getArrivalDateLocal() != null && flightEntitiy.getArrivalTimeLocal() != null) {
                arrivalDateTimeUtc = flightEntitiy.getArrivalTimeLocal().atDate(flightEntitiy.getArrivalDateLocal()).atZone(arrivalAirportEntity.getTimezoneId()).toInstant();
            }
        }

        AirportContactBean arrivalContactBean = new AirportContactBean();
        arrivalContactBean.setAirport(arrivalAirportBean);
        arrivalContactBean.setDateLocal(flightEntitiy.getArrivalDateLocal());
        arrivalContactBean.setDateOffset(FlighttrackerHelper.computeOffsetDays(departureContactBean.getDateLocal(), arrivalContactBean.getDateLocal()));
        arrivalContactBean.setDateTimeUtc(arrivalDateTimeUtc);
        arrivalContactBean.setTimeLocal(flightEntitiy.getArrivalTimeLocal());

        FlightBean flightBean = new FlightBean();
        flightBean.setEntityId(flightEntitiy.getId());
        flightBean.setAircraft(aircraftBean);
        flightBean.setAirline(airlineBean);
        flightBean.setArrivalContact(arrivalContactBean);
        flightBean.setCabinClass(flightEntitiy.getCabinClass());
        flightBean.setComment(flightEntitiy.getComment());
        flightBean.setDepartureContact(departureContactBean);
        flightBean.setFlightDistance(flightEntitiy.getFlightDistance());
        flightBean.setFlightDuration(flightEntitiy.getFlightDuration() == null ? null : Duration.ofMinutes(flightEntitiy.getFlightDuration()));
        flightBean.setFlightDurationString(this.formatFlightDuration(flightEntitiy.getFlightDuration()));
        flightBean.setFlightNumber(flightEntitiy.getFlightNumber());
        flightBean.setFlightReason(flightEntitiy.getFlightReason());
        flightBean.setSeatNumber(flightEntitiy.getSeatNumber());
        flightBean.setSeatType(flightEntitiy.getSeatType());
        if (flightEntitiy.getFlightDistance() != null && flightEntitiy.getFlightDuration() != null) {
            flightBean.setAverageSpeed(flightEntitiy.getFlightDistance().doubleValue() / (flightEntitiy.getFlightDuration().doubleValue() / 60d));
        }
        return flightBean;

    }

    private String formatFlightDuration(Integer flightDuration) {
        if (flightDuration == null) {
            return null;
        } else {

            int hours = (int)Math.floor(flightDuration.intValue() / 60d);
            int hoursInMinutes = hours * 60;
            int minutes = flightDuration.intValue() - hoursInMinutes;

            StringBuilder result = new StringBuilder();
            result.append(hours);
            result.append(":");
            result.append(minutes < 10 ? "0" : "");
            result.append(minutes);
            return result.toString();

        }
    }

    @Override
    public Collection<Integer> loadActiveYears() {
        return StreamSupport.stream(this.getFlightsRepository().findAll().spliterator(), false)
            .flatMap(flight -> Arrays.asList(flight.getDepartureDateLocal() == null ? null : flight.getDepartureDateLocal().getYear(), flight.getArrivalDateLocal() == null ? null : flight.getArrivalDateLocal().getYear()).stream())
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    FlightsRepository getFlightsRepository() {
        return this.flightsRepository;
    }
    @Autowired
    void setFlightsRepository(FlightsRepository flightsRepository) {
        this.flightsRepository = flightsRepository;
    }

    AirportsRepository getAirportsRepository() {
        return this.airportsRepository;
    }
    @Autowired
    void setAirportsRepository(AirportsRepository airportsRepository) {
        this.airportsRepository = airportsRepository;
    }

    AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

}
