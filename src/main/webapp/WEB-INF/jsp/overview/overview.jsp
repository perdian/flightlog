<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ft" tagdir="/WEB-INF/tags/flighttracker" %>

<ft:html>
	<ft:head>
        <link rel="stylesheet" href="https://openlayers.org/en/v4.2.0/css/ol.css" type="text/css" />
        <script src="https://openlayers.org/en/v4.2.0/build/ol.js"></script>
        <script src="https://api.mapbox.com/mapbox.js/plugins/arc.js/v0.1.0/arc.js"></script>
    </ft:head>
	<ft:body>

        <h2><fmt:message key="overview" /></h2>
        <div class="ui four stackable cards">
            <div class="card">
                <div class="content">
                    <div class="header">
                        <fmt:message key="distanceFlown" />
                    </div>
                    <div class="description">
                        <table class="ui very basic compact table">
                            <tbody>
                                <tr>
                                    <td><fmt:message key="inKilometers" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.distanceInKilometers}" pattern="#,##0" /></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="inMiles" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.distanceInMiles}" pattern="#,##0" /></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="earthOrbits" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.earthOrbits}" pattern="#,##0.0#" /> &times;</td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="earthToMoon" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.earthToMoon}" pattern="#,##0.0##" /> &times;</td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="earthToSun" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.earthToSun}" pattern="#,##0.0###" /> &times;</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="card">
                <div class="content">
                    <div class="header">
                        <fmt:message key="flightDuration" />
                    </div>
                    <div class="description">
                        <table class="ui very basic compact table">
                            <tbody>
                                <tr>
                                    <td><fmt:message key="hours" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.durationInHours}" pattern="#,##0" /></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="days" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.durationInDays}" pattern="#,##0.#" /></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="weeks" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.durationInWeeks}" pattern="#,##0.0#" /></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="months" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.durationInMonths}" pattern="#,##0.00" /></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="years" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.durationInYears}" pattern="#,##0.0#" /></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="card">
                <div class="content">
                    <div class="header">
                        <fmt:message key="flights" />
                    </div>
                    <div class="description">
                        <table class="ui very basic compact table">
                            <tbody>
                                <tr>
                                    <td><fmt:message key="total" /></td>
                                    <td>&nbsp;</td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfFlights}" pattern="#,##0" /></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="shortHaul" /></td>
                                    <td><fmt:message key="upTo" /><c:out value=" " /><fmt:formatNumber value="1500" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfFlightsShort}" pattern="#,##0" /></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="mediumHaul" /></td>
                                    <td><fmt:formatNumber value="1500" pattern="#,##0" /><c:out value=" - " /><fmt:formatNumber value="3500" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfFlightsMedium}" pattern="#,##0" /></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="longHaul" /></td>
                                    <td><fmt:formatNumber value="3500" pattern="#,##0" /><c:out value=" - " /><fmt:formatNumber value="10000" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfFlightsLong}" pattern="#,##0" /></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="ultraLongHaul" /></td>
                                    <td><fmt:message key="moreThan" /><c:out value=" " /><fmt:formatNumber value="10000" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfFlightsUltraLong}" pattern="#,##0" /></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="card">
                <div class="content">
                    <div class="header">
                        <fmt:message key="otherStatistics" />
                    </div>
                    <div class="description">
                        <table class="ui very basic compact table">
                            <tbody>
                                <tr>
                                    <td><fmt:message key="numberOfDifferentAirports" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfDifferentAirports}" pattern="#,##0" /></td>
                                </tr>
                                <c:if test="${overview.statistics.numberOfDifferentCountries gt 0}">
                                    <tr>
                                        <td><fmt:message key="numberOfDifferentCountries" /></td>
                                        <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfDifferentCountries}" pattern="#,##0" /></td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td><fmt:message key="numberOfDifferentRoutes" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfDifferentRoutes}" pattern="#,##0" /></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="numberOfDifferentAirlines" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfDifferentAirlines}" pattern="#,##0" /></td>
                                </tr>
                                <tr>
                                    <td><fmt:message key="numberOfDifferentAircraftTypes" /></td>
                                    <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfDifferentAircraftTypes}" pattern="#,##0" /></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <p>
            <spring:form modelAttribute="overviewQuery" servletRelativeAction="/">
                <spring:select path="year">
                    <option value=""><fmt:message key="allYears" /></option>
                    <spring:options items="${overviewQueryHelper.availableYears}" />
                </spring:select>
                <button type="submit" class="btn btn-s btn-primary"><fmt:message key="filterFlights" /></button>
            </spring:form>
        </p>

        <ft:map />

        <div>
            <spring:form modelAttribute="overviewQuery" servletRelativeAction="/">
                <spring:select path="year">
                    <option value=""><fmt:message key="allYears" /></option>
                    <spring:options items="${overviewQueryHelper.availableYears}" />
                </spring:select>
                <button type="submit" class="btn btn-s btn-primary"><fmt:message key="filterFlights" /></button>
            </spring:form>
        </div>

        <c:if test="${overview.statistics.numberOfFlights gt 0}">
            <div class="ui one stackable cards">
                <div class="card">
                    <div class="content">
                        <div class="header">
                            <fmt:message key="flightStatistics" />
                        </div>
                        <div class="description">
                            <table class="ui compact table">
                                <thead>
                                    <tr>
                                        <th>&nbsp;</th>
                                        <th><fmt:message key="departureAirport" /></th>
                                        <th><fmt:message key="departureDate" /></th>
                                        <th><fmt:message key="arrivalAirport" /></th>
                                        <th><fmt:message key="arrivalDate" /></th>
                                        <th class="right aligned"><fmt:message key="duration" /></th>
                                        <th class="right aligned"><fmt:message key="distance" /></th>
                                        <th class="right aligned"><fmt:message key="averageSpeed" /></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <ft:flightInfoRow titleKey="longestFlightByDuration" flight="${overview.statistics.longestFlightByDuration}" />
                                    <ft:flightInfoRow titleKey="shortestFlightByDuration" flight="${overview.statistics.shortestFlightByDuration}" />
                                    <ft:flightInfoRow titleKey="longestFlightByDistance" flight="${overview.statistics.longestFlightByDistance}" />
                                    <ft:flightInfoRow titleKey="shortestFlightByDistance" flight="${overview.statistics.shortestFlightByDistance}" />
                                    <ft:flightInfoRow titleKey="fastestFlight" flight="${overview.statistics.fastestFlight}" />
                                    <ft:flightInfoRow titleKey="slowestFlight" flight="${overview.statistics.slowestFlight}" />
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="ui two stackable cards">
            <ft:statisticsTopItemsCard titleKey="topTenAirports" valueKey="airport" items="${overview.statistics.topAirports}" />
            <ft:statisticsTopItemsCard titleKey="topTenAirlines" valueKey="airline" items="${overview.statistics.topAirlines}" />
        </div>
        <div class="ui two stackable cards">
            <ft:statisticsTopItemsCard titleKey="topTenRoutes" valueKey="route" items="${overview.statistics.topRoutes}" />
            <ft:statisticsTopItemsCard titleKey="topTenAircraftTypes" valueKey="aircraftType" items="${overview.statistics.topAircraftTypes}" />
        </div>

        <div class="ui three stackable cards">
            <ft:statisticsDistributionCard titleKey="cabinClasses" valueKey="cabinClass" localizationPrefix="cabinClass" items="${overview.statistics.distributionOfCabinClasses}" />
            <ft:statisticsDistributionCard titleKey="flightReasons" valueKey="flightReason" localizationPrefix="flightReason" items="${overview.statistics.distributionOfFlightReasons}" />
            <ft:statisticsDistributionCard titleKey="seatTypes" valueKey="seatType" localizationPrefix="seatType" items="${overview.statistics.distributionOfSeatTypes}" />
        </div>

	</ft:body>
</ft:html>