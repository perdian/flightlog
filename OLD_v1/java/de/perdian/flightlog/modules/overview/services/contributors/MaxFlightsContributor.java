package de.perdian.flightlog.modules.overview.services.contributors;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import de.perdian.flightlog.modules.flights.editor.FlightBean;
import de.perdian.flightlog.modules.overview.model.OverviewBean;
import de.perdian.flightlog.modules.overview.services.OverviewContributor;

@Component
class MaxFlightsContributor implements OverviewContributor {

    @Override
    public void contributeTo(OverviewBean overviewBean, List<FlightBean> flights) {
        Map<String, FlightBean> resultMap = new LinkedHashMap<>();
        resultMap.put("longestFlightByDuration", this.computeMaxFlight(flights, 1, FlightBean::getFlightDuration));
        resultMap.put("longestFlightByDistance", this.computeMaxFlight(flights, 1, FlightBean::getFlightDistance));
        resultMap.put("shortestFlightByDuration", this.computeMaxFlight(flights, -1, FlightBean::getFlightDuration));
        resultMap.put("shortestFlightByDistance", this.computeMaxFlight(flights, -1, FlightBean::getFlightDistance));
        resultMap.put("fastestFlight", this.computeMaxFlight(flights, 1, FlightBean::getAverageSpeed));
        resultMap.put("slowestFlight", this.computeMaxFlight(flights, -1, FlightBean::getAverageSpeed));
        overviewBean.setMaxFlights(resultMap);
    }

    private <T extends Comparable<T>> FlightBean computeMaxFlight(List<FlightBean> flights, int direction, Function<FlightBean, T> valueFunction) {
        FlightBean maxFlight = null;
        T maxValue = null;
        for (FlightBean currentFlight : flights) {
            T currentValue = valueFunction.apply(currentFlight);
            if (currentValue != null) {
                if (maxValue == null) {
                    maxFlight = currentFlight;
                    maxValue = currentValue;
                } else {
                    int compareValue = currentValue.compareTo(maxValue) * direction;
                    if (compareValue > 0) {
                        maxFlight = currentFlight;
                        maxValue = currentValue;
                    }
                }
            }
        }
        return maxFlight;
    }

}
