package de.perdian.flightlog.modules.flights.shared.service;

import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.persistence.FlightEntity;
import de.perdian.flightlog.modules.flights.shared.persistence.FlightEntityMapper;
import de.perdian.flightlog.modules.flights.shared.persistence.FlightRepository;
import de.perdian.flightlog.support.pagination.PaginatedList;
import de.perdian.flightlog.support.pagination.PaginationData;
import de.perdian.flightlog.support.pagination.PaginationRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class FlightQueryServiceImpl implements FlightQueryService {

    private FlightRepository flightRepository = null;
    private FlightEntityMapper flightEntityMapper = null;

    @Override
    public PaginatedList<Flight> loadFlights(FlightQuery flightQuery, PaginationRequest paginationRequest) {

        Sort flightEntitiesSort = Sort.by(Sort.Order.desc("departureDateLocal"), Sort.Order.desc("departureTimeLocal"));
        Pageable pageable = paginationRequest == null ? Pageable.unpaged() : paginationRequest.toPageable(flightEntitiesSort);
        Specification<FlightEntity> flightEntitiesSpecification = this.createFlightsSpecification(flightQuery);
        Page<FlightEntity> flightEntitiesPage = this.getFlightRepository().findAll(flightEntitiesSpecification, pageable);

        List<Flight> flights = flightEntitiesPage.stream()
            .map(flightEntity -> this.getFlightEntityMapper().createModel(flightEntity))
            .filter(flightQuery)
            .sorted(flightQuery.getComparator())
            .toList();

        return new PaginatedList<>(flights, new PaginationData(flightEntitiesPage));

    }

    private Specification<FlightEntity> createFlightsSpecification(FlightQuery flightQuery) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> flightEntityPredicates = new ArrayList<>();
            flightEntityPredicates.add(criteriaBuilder.equal(root.get("user"), flightQuery.getUser() == null ? null : flightQuery.getUser().getEntity()));
            this.appendFlightQueryPredicates(flightEntityPredicates, flightQuery, root, criteriaBuilder);
            return criteriaBuilder.and(flightEntityPredicates.toArray(new Predicate[flightEntityPredicates.size()]));
        };
    }

    private void appendFlightQueryPredicates(List<Predicate> predicates, FlightQuery flightQuery, Root<FlightEntity> root, CriteriaBuilder criteriaBuilder) {
        if (flightQuery.getRestrictEntityIdentifiers() != null && !flightQuery.getRestrictEntityIdentifiers().isEmpty()) {
            predicates.add(root.get("id").in(flightQuery.getRestrictEntityIdentifiers()));
        }
        if (flightQuery.getExcludeEntityIdentifiers() != null && !flightQuery.getExcludeEntityIdentifiers().isEmpty()) {
            predicates.add(root.get("id").in(flightQuery.getExcludeEntityIdentifiers()).not());
        }
    }

    FlightRepository getFlightRepository () {
        return this.flightRepository;
    }
    @Autowired
    void setFlightRepository (FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }

    FlightEntityMapper getFlightEntityMapper () {
        return this.flightEntityMapper;
    }
    @Autowired
    void setFlightEntityMapper (FlightEntityMapper flightEntityMapper){
        this.flightEntityMapper = flightEntityMapper;
    }

}
