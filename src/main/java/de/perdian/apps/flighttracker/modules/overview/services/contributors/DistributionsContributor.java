package de.perdian.apps.flighttracker.modules.overview.services.contributors;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewBean;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewItem;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewItemString;
import de.perdian.apps.flighttracker.modules.overview.services.OverviewContributor;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;
import de.perdian.apps.flighttracker.support.types.CabinClass;
import de.perdian.apps.flighttracker.support.types.FlightReason;
import de.perdian.apps.flighttracker.support.types.SeatType;

@Component
class DistributionsContributor implements OverviewContributor {

    @Override
    public void contributeTo(OverviewBean overviewBean, List<FlightBean> flights, UserEntity user) {
        Map<String, List<OverviewItem>> resultMap = new LinkedHashMap<>();
        resultMap.put("cabinClasses", this.createDistribution(flights, FlightBean::getCabinClass, CabinClass.class, "cabinClass"));
        resultMap.put("flightReasons", this.createDistribution(flights, FlightBean::getFlightReason, FlightReason.class, "flightReason"));
        resultMap.put("seatTypes", this.createDistribution(flights, FlightBean::getSeatType, SeatType.class, "seatType"));
        overviewBean.setDistributions(resultMap);
    }

    private <E extends Enum<E>> List<OverviewItem> createDistribution(List<FlightBean> flights, Function<FlightBean, E> enumValueFunction, Class<E> enumClass, String enumValueLocalizationPrefix) {

        EnumMap<E, AtomicInteger> valueMap = new EnumMap<>(enumClass);
        for (E enumValue : enumClass.getEnumConstants()) {
            valueMap.put(enumValue, new AtomicInteger(0));
        }

        flights.stream()
            .map(enumValueFunction)
            .filter(Objects::nonNull)
            .forEach(value -> valueMap.get(value).incrementAndGet());

        List<OverviewItem> resultList = new ArrayList<>();
        for (Map.Entry<E, AtomicInteger> valueEntry : valueMap.entrySet()) {

            OverviewItemString title = OverviewItemString.forKey(enumValueLocalizationPrefix + "." + valueEntry.getKey().name());
            Number value = valueEntry.getValue();
            Number percentage = valueEntry.getValue().intValue() <= 0 ? null : (100d / flights.size()) * valueEntry.getValue().intValue();

            resultList.add(new OverviewItem(title, null, value, null, percentage));

        }
        return resultList;

    }

}
