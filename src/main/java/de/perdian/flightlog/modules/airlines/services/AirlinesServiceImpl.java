package de.perdian.flightlog.modules.airlines.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import de.perdian.flightlog.modules.airlines.model.AirlineBean;
import de.perdian.flightlog.modules.airlines.persistence.AirlineEntity;
import de.perdian.flightlog.modules.airlines.persistence.AirlinesRepository;
import de.perdian.flightlog.modules.users.persistence.UserEntity;

@Service
class AirlinesServiceImpl implements AirlinesService {

    private List<AirlinesLookup> airlinesLookups = null;
    private AirlinesRepository airlinesRepository = null;

    @Override
    public AirlineBean loadAirlineByCode(String airlineCode, UserEntity user) {
        return this.getAirlinesLookups().stream()
            .map(lookup -> lookup.loadAirlineByCode(airlineCode, user))
            .filter(airline -> airline != null)
            .findFirst()
            .orElse(null);
    }

    @Override
    public AirlineBean loadAirlineByName(String airlineName, UserEntity user) {
        return this.getAirlinesLookups().stream()
            .map(lookup -> lookup.loadAirlineByName(airlineName, user))
            .filter(airline -> airline != null)
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<AirlineBean> loadUserSpecificAirlines(UserEntity user) {
        return this.getAirlinesRepository().findAll((root, query, cb) -> user == null ? cb.isNull(root.get("user")) : cb.equal(root.get("user"), user)).stream()
            .map(this::createAirlineBean)
            .sorted(Comparator.comparing(AirlineBean::getCode))
            .collect(Collectors.toList());
    }

    @Override
    public AirlineBean updateUserSpecificAirline(UserEntity user, AirlineBean airlineBean) {

        Specification<AirlineEntity> airlineSpecification = this.createAirlineSpecification(user, airlineBean.getCode());
        AirlineEntity airlineEntity = this.getAirlinesRepository().findOne(airlineSpecification).orElse(null);
        if (airlineEntity == null) {
            airlineEntity = new AirlineEntity();
            airlineEntity.setUser(user);
        }
        airlineEntity.setCode(airlineBean.getCode());
        airlineEntity.setCountryCode(airlineBean.getCountryCode());
        airlineEntity.setName(airlineBean.getName());
        this.getAirlinesRepository().save(airlineEntity);

        return airlineBean;

    }

    @Override
    public void deleteUserSpecificAirline(UserEntity user, AirlineBean airlineBean) {
        Specification<AirlineEntity> airlineSpecification = this.createAirlineSpecification(user, airlineBean.getCode());
        AirlineEntity airlineEntity = this.getAirlinesRepository().findOne(airlineSpecification).orElse(null);
        if (airlineEntity != null) {
            this.getAirlinesRepository().delete(airlineEntity);
        }
    }

    private Specification<AirlineEntity> createAirlineSpecification(UserEntity user, String airlineCode) {
        return (root, query, cb) -> cb.and(
            user == null ? cb.isNull(root.get("user")) : cb.equal(root.get("user"), user),
            cb.equal(root.get("code"), airlineCode)
        );
    }

    private AirlineBean createAirlineBean(AirlineEntity airlineEntity) {
        AirlineBean airlineBean = new AirlineBean();
        airlineBean.setCode(airlineEntity.getCode());
        airlineBean.setCountryCode(airlineEntity.getCountryCode());
        airlineBean.setName(airlineEntity.getName());
        return airlineBean;
    }

    List<AirlinesLookup> getAirlinesLookups() {
        return this.airlinesLookups;
    }
    @Autowired
    void setAirlinesLookups(List<AirlinesLookup> airlinesLookups) {
        this.airlinesLookups = airlinesLookups;
    }

    AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

}
