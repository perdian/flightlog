package de.perdian.flightlog.modules.overview.services.contributors;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import de.perdian.flightlog.modules.flights.editor.FlightBean;
import de.perdian.flightlog.modules.overview.model.OverviewBean;
import de.perdian.flightlog.modules.overview.model.OverviewItem;
import de.perdian.flightlog.modules.overview.model.OverviewItemString;
import de.perdian.flightlog.modules.overview.services.OverviewContributor;
import de.perdian.flightlog.support.types.FlightDistance;

@Component
class DistancesContributor implements OverviewContributor {

    @Override
    public void contributeTo(OverviewBean overviewBean, List<FlightBean> flights) {

        Map<FlightDistance, Integer> resultMap = new LinkedHashMap<>();
        for (FlightDistance flightDistance : FlightDistance.values()) {
            resultMap.put(flightDistance, (int)flights.stream()
                .filter(flight -> flight.getFlightDistance() != null)
                .mapToInt(flight -> flight.getFlightDistance())
                .filter(value -> flightDistance.matches(value))
                .count()
            );
        }

        List<OverviewItem> resultList = new ArrayList<>(resultMap.size() + 1);
        for (Map.Entry<FlightDistance, Integer> resultEntry : resultMap.entrySet()) {
            OverviewItem overviewItem = new OverviewItem(OverviewItemString.forKey("flightDistance." + resultEntry.getKey().name()), null, resultEntry.getValue(), null, 100d / flights.size() * resultEntry.getValue().doubleValue());
            overviewItem.addToContext("minValue", resultEntry.getKey().getMinValue());
            overviewItem.addToContext("maxValue", resultEntry.getKey().getMaxValue());
            resultList.add(overviewItem);
        }
        overviewBean.setDistances(resultList);

    }

}
