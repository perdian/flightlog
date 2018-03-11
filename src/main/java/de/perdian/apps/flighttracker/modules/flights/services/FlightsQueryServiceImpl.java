package de.perdian.apps.flighttracker.modules.flights.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.airlines.services.AirlinesService;
import de.perdian.apps.flighttracker.modules.airports.persistence.AirportEntity;
import de.perdian.apps.flighttracker.modules.airports.persistence.AirportsRepository;
import de.perdian.apps.flighttracker.modules.flights.model.AircraftBean;
import de.perdian.apps.flighttracker.modules.flights.model.AirportBean;
import de.perdian.apps.flighttracker.modules.flights.model.AirportContactBean;
import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.flights.persistence.FlightEntity;
import de.perdian.apps.flighttracker.modules.flights.persistence.FlightsRepository;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;
import de.perdian.apps.flighttracker.support.FlighttrackerHelper;
import de.perdian.apps.flighttracker.support.persistence.PaginatedList;
import de.perdian.apps.flighttracker.support.persistence.PaginationData;
import de.perdian.apps.flighttracker.support.types.FlightType;

@Service
class FlightsQueryServiceImpl implements FlightsQueryService {

    private static final List<String> COUNTRY_CODES_EUROPE = Arrays.asList("AD", "AL", "AT", "BA", "BE", "BG", "BY", "CH", "CY", "CZ", "DE", "DK", "EE", "ES", "FI", "FR", "GB", "GG", "GI", "GR", "HR", "HU", "IE", "IM", "IS", "IT", "JE", "LI", "LT", "LU", "LV", "MC", "MD", "MK", "MT", "NL", "NO", "PL", "PT", "RO", "SE", "SI", "SJ", "SK", "SM", "UA", "UK", "VA");

    private FlightsRepository flightsRepository = null;
    private AirportsRepository airportsRepository = null;
    private AirlinesService airlinesService = null;

    @Override
    public PaginatedList<FlightBean> loadFlights(FlightsQuery flightsQuery) {

        Sort sort = Sort.by(Sort.Order.desc("departureDateLocal"), Sort.Order.desc("departureTimeLocal"), Sort.Order.desc("id"));
        PageRequest pageRequest = PageRequest.of(flightsQuery.getPage() == null ? 0 : flightsQuery.getPage(), flightsQuery.getPageSize() == null ? Integer.MAX_VALUE : flightsQuery.getPageSize(), sort);

        Specification<FlightEntity> flightEntitiesSpecification = (root, query, cb) -> flightsQuery.toPredicate(root, query, cb);
        Page<FlightEntity> flightEntities = this.getFlightsRepository().findAll(flightEntitiesSpecification, pageRequest);

        PaginationData paginationData = flightEntities == null ? null : new PaginationData(flightEntities.getNumber(), flightEntities.getTotalPages());
        List<FlightBean> resultList = flightEntities == null ? null : flightEntities.getContent().stream()
            .map(flightEntity -> this.convertFlightEntity(flightEntity, flightsQuery.getRestrictUser()))
            .filter(bean -> this.validateListContainsEnumValue(bean.getFlightType(), flightsQuery.getRestrictFlightTypes()))
            .collect(Collectors.toList());

        return new PaginatedList<>(resultList, paginationData);

    }

    private <E extends Enum<E>> boolean validateListContainsEnumValue(E enumValue, Collection<E> enumValues) {
        return enumValues == null || enumValues.isEmpty() || enumValues.contains(enumValue);
    }

    @Override
    public FlightBean loadFlightById(UUID id, UserEntity user) {
        FlightsQuery flightsQuery = new FlightsQuery();
        flightsQuery.setRestrictIdentifiers(Collections.singleton(id));
        flightsQuery.setRestrictUser(user);
        flightsQuery.setPageSize(1);
        PaginatedList<FlightBean> flightsList = this.loadFlights(flightsQuery);
        return flightsList.getItem(0).orElse(null);
    }

    private FlightBean convertFlightEntity(FlightEntity flightEntitiy, UserEntity user) {

        AircraftBean aircraftBean = new AircraftBean();
        aircraftBean.setName(flightEntitiy.getAircraftName());
        aircraftBean.setRegistration(flightEntitiy.getAircraftRegistration());
        aircraftBean.setType(flightEntitiy.getAircraftType());

        AirlineBean airlineBean = StringUtils.isEmpty(flightEntitiy.getAirlineCode()) ? null : this.getAirlinesService().loadAirlineByCode(flightEntitiy.getAirlineCode(), user);
        if (airlineBean == null) {
            airlineBean = new AirlineBean();
            airlineBean.setCode(flightEntitiy.getAirlineCode());
            airlineBean.setName(flightEntitiy.getAirlineName());
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
        flightBean.setFlightType(this.computeFlightType(departureAirportBean, arrivalAirportBean));
        flightBean.setSeatNumber(flightEntitiy.getSeatNumber());
        flightBean.setSeatType(flightEntitiy.getSeatType());
        if (flightEntitiy.getFlightDistance() != null && flightEntitiy.getFlightDuration() != null) {
            flightBean.setAverageSpeed(flightEntitiy.getFlightDistance().doubleValue() / (flightEntitiy.getFlightDuration().doubleValue() / 60d));
        }
        return flightBean;

    }

    private FlightType computeFlightType(AirportBean departureAirportBean, AirportBean arrivalAirportBean) {
        if (departureAirportBean != null && StringUtils.isNotEmpty(departureAirportBean.getCountryCode()) && arrivalAirportBean != null && StringUtils.isNotEmpty(arrivalAirportBean.getCountryCode())) {
            if (Objects.equals(departureAirportBean.getCountryCode(), arrivalAirportBean.getCountryCode())) {
                return FlightType.DOMESTIC;
            } else if (COUNTRY_CODES_EUROPE.contains(departureAirportBean.getCountryCode()) && COUNTRY_CODES_EUROPE.contains(arrivalAirportBean.getCountryCode())) {
                return FlightType.EUROPE;
            } else {
                return FlightType.INTERNATIONAL;
            }
        } else {
            return null;
        }
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

    AirlinesService getAirlinesService() {
        return this.airlinesService;
    }
    @Autowired
    void setAirlinesService(AirlinesService airlinesService) {
        this.airlinesService = airlinesService;
    }

}
