package de.perdian.apps.flighttracker.modules.overview.services.contributors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewBean;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewItem;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewItemString;
import de.perdian.apps.flighttracker.modules.overview.services.OverviewContributor;

@Component
class DifferentsContributor implements OverviewContributor {

    @Override
    public void contributeTo(OverviewBean overviewBean, List<FlightBean> flights) {
        List<OverviewItem> overviewItems = new ArrayList<>();
        overviewItems.add(new OverviewItem(OverviewItemString.forKey("numberOfDifferentAircraftTypes"), null, this.countDifferentValues(flights, flight -> Arrays.asList(flight.getAircraft().getType())), null, null));
        overviewItems.add(new OverviewItem(OverviewItemString.forKey("numberOfDifferentAirports"), null, this.countDifferentValues(flights, flight -> Arrays.asList(flight.getDepartureContact().getAirport().getCode(), flight.getArrivalContact().getAirport().getCode())), null, null));
        overviewItems.add(new OverviewItem(OverviewItemString.forKey("numberOfDifferentCountries"), null, this.countDifferentValues(flights, flight -> Arrays.asList(flight.getDepartureContact().getAirport().getCountryCode(), flight.getArrivalContact().getAirport().getCountryCode())), null, null));
        overviewItems.add(new OverviewItem(OverviewItemString.forKey("numberOfDifferentRoutes"), null, this.countDifferentValues(flights, flight -> Arrays.asList(flight.getDepartureContact().getAirport().getCode() + "-" + flight.getArrivalContact().getAirport().getCode())), null, null));
        overviewItems.add(new OverviewItem(OverviewItemString.forKey("numberOfDifferentAirlines"), null, this.countDifferentValues(flights, flight -> Arrays.asList(flight.getAirline().getCode())), null, null));
        overviewBean.setDifferents(overviewItems);
    }

    private Integer countDifferentValues(List<FlightBean> flights, Function<FlightBean, Collection<String>> valueFunction) {
        return (int)flights.stream()
            .map(valueFunction)
            .flatMap(Collection::stream)
            .distinct()
            .filter(value -> !StringUtils.isEmpty(value))
            .count();
    }

}
