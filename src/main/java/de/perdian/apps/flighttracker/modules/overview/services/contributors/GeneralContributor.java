package de.perdian.apps.flighttracker.modules.overview.services.contributors;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewBean;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewItem;
import de.perdian.apps.flighttracker.modules.overview.model.OverviewItemString;
import de.perdian.apps.flighttracker.modules.overview.services.OverviewContributor;

@Component
class GeneralContributor implements OverviewContributor {

    @Override
    public void contributeTo(OverviewBean overviewBean, List<FlightBean> flights) {

        int distanceInKilometers = flights.stream()
            .filter(flight -> flight.getFlightDistance() != null)
            .mapToInt(flight -> flight.getFlightDistance())
            .sum();
        int distanceInMiles = (int)(distanceInKilometers * 0.621371d);

        Duration duration = Duration.ofMinutes(flights.stream()
            .filter(flight -> flight.getFlightDuration() != null)
            .mapToInt(flight -> (int)flight.getFlightDuration().toMinutes())
            .sum());
        double durationInHours = duration.toMinutes() / 60d;
        double durationInDays = duration.toMinutes() / (60d * 24d);

        List<OverviewItem> generalItems = new ArrayList<>();
        generalItems.add(new OverviewItem(OverviewItemString.forKey("totalNumber"), null, flights.size(), null));
        generalItems.add(new OverviewItem(OverviewItemString.forKey("distanceInKm"), null, distanceInKilometers, null));
        generalItems.add(new OverviewItem(OverviewItemString.forKey("distanceInMiles"), null, distanceInMiles, null));
        generalItems.add(new OverviewItem(OverviewItemString.forKey("durationInHours"), null, durationInHours, null));
        generalItems.add(new OverviewItem(OverviewItemString.forKey("durationInDays"), null, durationInDays, null));
        overviewBean.setGeneral(generalItems);

    }

}
