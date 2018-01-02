package de.perdian.apps.flighttracker.modules.airlines.services.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.airlines.persistence.AirlineEntity;
import de.perdian.apps.flighttracker.modules.airlines.persistence.AirlinesRepository;
import de.perdian.apps.flighttracker.modules.airlines.services.AirlinesLookup;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class DatabaseAirlinesLookup implements AirlinesLookup {

    private Map<UserEntity, Map<String, AirlineBean>> airlineBeansByCode = new LinkedHashMap<>();
    private AirlinesRepository airlinesRepository = null;

    DatabaseAirlinesLookup() {
        this.setAirlineBeansByCode(new LinkedHashMap<>());
    }

    @Override
    public AirlineBean loadAirlineByCode(String airlineCode, UserEntity user) {
        return Optional.ofNullable(this.getAirlineBeansByCode().get(user))
            .map(beans -> beans.getOrDefault(airlineCode, null))
            .orElseGet(() -> this.loadAirlineByCodeFromDatabase(airlineCode, user));
    }

    private AirlineBean loadAirlineByCodeFromDatabase(String airlineCode, UserEntity user) {
        AirlineEntity airlineEntity = this.getAirlinesRepository().findOne((root, query, cb) -> cb.and(cb.equal(root.get("code"), airlineCode), user == null ? cb.isNull(root.get("user")) : cb.equal(root.get("user"), user)));
        if (airlineEntity == null) {
            return null;
        } else {
            AirlineBean airlineBean = new AirlineBean();
            airlineBean.setCode(airlineEntity.getCode());
            airlineBean.setCountryCode(airlineEntity.getCountryCode());
            airlineBean.setName(airlineEntity.getName());
            this.getAirlineBeansByCode().compute(user, (k, v) -> v == null ? new LinkedHashMap<>() : v).put(airlineCode, airlineBean);
            return airlineBean;
        }
    }

    @Override
    public AirlineBean loadAirlineByName(String airlineName, UserEntity user) {
        Map<String, AirlineBean> airlineBeansForUser = this.getAirlineBeansByCode().get(user);
        if (airlineBeansForUser == null) {
            return this.loadAirlineByNameFromDatabase(airlineName, user);
        } else {
            return airlineBeansForUser.values().stream()
                .filter(bean -> airlineName.equalsIgnoreCase(bean.getName()))
                .findFirst()
                .orElseGet(() -> this.loadAirlineByNameFromDatabase(airlineName, user));
        }
    }

    private AirlineBean loadAirlineByNameFromDatabase(String airlineName, UserEntity user) {
        AirlineEntity airlineEntity = this.getAirlinesRepository().findOne((root, query, cb) -> cb.and(cb.equal(root.get("name"), airlineName), user == null ? cb.isNull(root.get("user")) : cb.equal(root.get("user"), user)));
        if (airlineEntity == null) {
            return null;
        } else {
            AirlineBean airlineBean = new AirlineBean();
            airlineBean.setCode(airlineEntity.getCode());
            airlineBean.setCountryCode(airlineEntity.getCountryCode());
            airlineBean.setName(airlineEntity.getName());
            this.getAirlineBeansByCode().compute(user, (k, v) -> v == null ? new LinkedHashMap<>() : v).put(airlineEntity.getCode(), airlineBean);
            return airlineBean;
        }
    }

    @Override
    public void updateAirlineByCode(String airlineCode, AirlineBean airlineBean, UserEntity user) {
        this.getAirlineBeansByCode().compute(user, (k, v) -> v == null ? new LinkedHashMap<>() : v).put(airlineCode, airlineBean);
    }

    @Override
    public void removeAirlineByCode(String airlineCode, UserEntity user) {
        Map<String, AirlineBean> airlineBeansForUser = this.getAirlineBeansByCode().get(user);
        if (airlineBeansForUser != null) {
            airlineBeansForUser.remove(airlineCode);
            if (airlineBeansForUser.isEmpty()) {
                this.getAirlineBeansByCode().remove(user);
            }
        }
    }

    Map<UserEntity, Map<String, AirlineBean>> getAirlineBeansByCode() {
        return this.airlineBeansByCode;
    }
    void setAirlineBeansByCode(Map<UserEntity, Map<String, AirlineBean>> airlineBeansByCode) {
        this.airlineBeansByCode = airlineBeansByCode;
    }

    public AirlinesRepository getAirlinesRepository() {
        return this.airlinesRepository;
    }
    @Autowired
    void setAirlinesRepository(AirlinesRepository airlinesRepository) {
        this.airlinesRepository = airlinesRepository;
    }

}
