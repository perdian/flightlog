package de.perdian.apps.flighttracker.modules.airlines.services.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.airlines.services.AirlinesLookup;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class StaticAirlinesLookup implements AirlinesLookup {

    private Map<UserEntity, Map<String, AirlineBean>> airlineBeansByCode = new LinkedHashMap<>();

    StaticAirlinesLookup() {
        this.setAirlineBeansByCode(new LinkedHashMap<>());
    }

    @Override
    public AirlineBean loadAirlineByCode(String airlineCode, UserEntity user) {
        return Optional.ofNullable(this.getAirlineBeansByCode().get(user))
            .map(beans -> beans.getOrDefault(airlineCode, null))
            .orElse(null);
    }

    @Override
    public AirlineBean loadAirlineByName(String airlineName, UserEntity user) {
        Map<String, AirlineBean> airlineBeansForUser = this.getAirlineBeansByCode().get(user);
        if (airlineBeansForUser == null) {
            return null;
        } else {
            return airlineBeansForUser.values().stream()
                .filter(bean -> airlineName.equalsIgnoreCase(bean.getName()))
                .findFirst()
                .orElse(null);
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
        }
    }

    Map<UserEntity, Map<String, AirlineBean>> getAirlineBeansByCode() {
        return this.airlineBeansByCode;
    }
    void setAirlineBeansByCode(Map<UserEntity, Map<String, AirlineBean>> airlineBeansByCode) {
        this.airlineBeansByCode = airlineBeansByCode;
    }

}
