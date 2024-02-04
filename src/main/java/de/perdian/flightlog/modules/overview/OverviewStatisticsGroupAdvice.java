package de.perdian.flightlog.modules.overview;

import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.support.types.CabinClass;
import de.perdian.flightlog.support.types.FlightDistance;
import de.perdian.flightlog.support.types.FlightReason;
import de.perdian.flightlog.support.types.SeatType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice(assignableTypes = OverviewController.class)
class OverviewStatisticsGroupAdvice {

    @ModelAttribute(name = "overviewStatisticsTotals", binding = false)
    OverviewStatisticsGroup statisticsTotals(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        return OverviewStatisticsGroupFactory.forEnum(OverviewStatisticsTotals.class, OverviewStatisticsItemHelpers.sumValues((flight, value) -> value.getResultFromMatchingFlightFunction().apply(flight)))
            .withTitle(OverviewString.forKey("flightTotals"))
            .createGroup(flights);
    }

    @ModelAttribute(name = "overviewStatisticsOthers", binding = false)
    OverviewStatisticsGroup statisticsOthers(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        return OverviewStatisticsGroupFactory.forEnum(OverviewStatisticsOthers.class, OverviewStatisticsItemHelpers.distinctValues((flight, value) -> value.getValuesFromFlightFunction().apply(flight)))
            .withTitle(OverviewString.forKey("otherStatistics"))
            .createGroup(flights);
    }

    @ModelAttribute(name = "overviewStatisticsByDistance", binding = false)
    OverviewStatisticsGroup statisticsByDistance(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        return OverviewStatisticsGroupFactory.forEnum(FlightDistance.class, OverviewStatisticsItemHelpers.countMatchingValues(Flight::getFlightDistanceType))
            .withTitle(OverviewString.forKey("distances"))
            .createGroup(flights);
    }

    @ModelAttribute(name = "overviewStatisticsByCabinClass", binding = false)
    OverviewStatisticsGroup statisticsByCabinClass(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        return OverviewStatisticsGroupFactory.forEnum(CabinClass.class, OverviewStatisticsItemHelpers.countMatchingValues(Flight::getCabinClass))
            .withItemPercentages(true)
            .withTitle(OverviewString.forKey("cabinClasses"))
            .createGroup(flights);
    }

    @ModelAttribute(name = "overviewStatisticsByFlightReason", binding = false)
    OverviewStatisticsGroup statisticsByFlightReason(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        return OverviewStatisticsGroupFactory.forEnum(FlightReason.class, OverviewStatisticsItemHelpers.countMatchingValues(Flight::getFlightReason))
            .withItemPercentages(true)
            .withTitle(OverviewString.forKey("flightReasons"))
            .createGroup(flights);
    }

    @ModelAttribute(name = "overviewStatisticsBySeatType", binding = false)
    OverviewStatisticsGroup statisticsBySeatType(@ModelAttribute(OverviewController.MODEL_ATTRIBUTE_FILTERED_FLIGHTS) List<Flight> flights) {
        return OverviewStatisticsGroupFactory.forEnum(SeatType.class, OverviewStatisticsItemHelpers.countMatchingValues(Flight::getSeatType))
            .withItemPercentages(true)
            .withTitle(OverviewString.forKey("seatTypes"))
            .createGroup(flights);
    }

}
