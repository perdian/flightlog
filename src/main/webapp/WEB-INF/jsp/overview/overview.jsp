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


        <h2 class="ui header">
            <i class="plane icon"></i>
            <div class="content">
                <fmt:message key="overview" />
                <div class="sub header"><fmt:message key="yourPersonalFlightStatistics" /></div>
            </div>
        </h2>

        <div class="ui divider"></div>

        <div class="ui stackable grid">
            <div class="four wide column">
                <h3><fmt:message key="distanceFlown" /></h3>
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
            <div class="four wide column">
                <h3><fmt:message key="flightDuration" /></h3>
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
            <div class="four wide column">
                <h3><fmt:message key="flights" /></h3>
                <table class="ui very basic compact table">
                    <tbody>
                        <tr>
                            <td><fmt:message key="total" /></td>
                            <td>&nbsp;</td>
                            <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfFlights}" pattern="#,##0" /></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="shortHaul" /></td>
                            <td><small><fmt:message key="upTo" /><c:out value=" " /><fmt:formatNumber value="1500" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" /></small></td>
                            <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfFlightsShort}" pattern="#,##0" /></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="mediumHaul" /></td>
                            <td><small><fmt:formatNumber value="1500" pattern="#,##0" /><c:out value=" - " /><fmt:formatNumber value="3500" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" /></small></td>
                            <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfFlightsMedium}" pattern="#,##0" /></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="longHaul" /></td>
                            <td><small><fmt:formatNumber value="3500" pattern="#,##0" /><c:out value=" - " /><fmt:formatNumber value="10000" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" /></small></td>
                            <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfFlightsLong}" pattern="#,##0" /></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="ultraLongHaul" /></td>
                            <td><small><fmt:message key="moreThan" /><c:out value=" " /><fmt:formatNumber value="10000" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" /></small></td>
                            <td class="right aligned"><fmt:formatNumber value="${overview.statistics.numberOfFlightsUltraLong}" pattern="#,##0" /></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="four wide column">
                <h3><fmt:message key="otherStatistics" /></h3>
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

        <div class="ui divider"></div>

        <div class="ui stackable grid">
            <div class="twelve wide column">
                <ft:map />
            </div>
            <div class="four wide column">
                <spring:form modelAttribute="overviewQuery" servletRelativeAction="/" cssClass="ui form">
                    <h3><fmt:message key="filterFlights" /></h3>
                    <div class="field">
                        <label><fmt:message key="year" /></label>
                        <spring:select path="year" cssClass="ui dropdown" multiple="multiple">
                            <option value=""><fmt:message key="allYears" /></option>
                            <spring:options items="${overviewQueryHelper.availableYears}" />
                        </spring:select>
                    </div>
                    <div class="field">
                        <label><fmt:message key="airline" /></label>
                        <spring:select path="airlineCode" cssClass="ui dropdown" multiple="multiple">
                            <option value=""><fmt:message key="allAirlines" /></option>
                            <c:forEach items="${overviewQueryHelper.availableAirlines}" var="airline">
                                <spring:option value="${airline.value}"><c:out value="${airline.title}" /></spring:option>
                            </c:forEach>
                        </spring:select>
                    </div>
                    <div class="field">
                        <label><fmt:message key="airport" /></label>
                        <spring:select path="airportCode" cssClass="ui dropdown" multiple="multiple">
                            <option value=""><fmt:message key="allAirports" /></option>
                            <c:forEach items="${overviewQueryHelper.availableAirports}" var="airport">
                                <spring:option value="${airport.value}"><c:out value="${airport.title}" /></spring:option>
                            </c:forEach>
                        </spring:select>
                    </div>
                    <div class="field">
                        <label><fmt:message key="aircraftType" /></label>
                        <spring:select path="aircraftType" cssClass="ui dropdown" multiple="multiple">
                            <option value=""><fmt:message key="allAircraftTypes" /></option>
                            <c:forEach items="${overviewQueryHelper.availableAircraftTypes}" var="aircraftType">
                                <spring:option value="${aircraftType.value}"><c:out value="${aircraftType.title}" /></spring:option>
                            </c:forEach>
                        </spring:select>
                    </div>
                    <div class="field">
                        <label><fmt:message key="cabinClass" /></label>
                        <spring:select path="cabinClass" cssClass="ui dropdown" multiple="multiple">
                            <option value=""><fmt:message key="allCabinClasses" /></option>
                            <c:forEach items="${overviewQueryHelper.availableCabinClasses}" var="cabinClass">
                                <spring:option value="${cabinClass.name()}"><fmt:message key="cabinClass.${cabinClass.name()}" /></spring:option>
                            </c:forEach>
                        </spring:select>
                    </div>
                    <div class="field">
                        <label><fmt:message key="flightReason" /></label>
                        <spring:select path="flightReason" cssClass="ui dropdown" multiple="multiple">
                            <option value=""><fmt:message key="allFlightReasons" /></option>
                            <c:forEach items="${overviewQueryHelper.availableFlightReasons}" var="flightReason">
                                <spring:option value="${flightReason.name()}"><fmt:message key="flightReason.${flightReason.name()}" /></spring:option>
                            </c:forEach>
                        </spring:select>
                    </div>
                    <div class="ui tiny buttons">
                        <button type="submit" class="ui tiny primary button">
                            <i class="search icon"></i>
                            <fmt:message key="filterFlights" />
                        </button>
                        <c:if test="${overviewQuery.filterActive}">
                            <a href="<c:url value="/" />" class="ui tiny button">
                                <i class="remove icon"></i>
                                <fmt:message key="resetFilter" />
                            </a>
                        </c:if>
                    </div>
                </spring:form>
            </div>
        </div>

        <c:if test="${overview.statistics.numberOfFlights gt 0}">
            <div class="ui divider"></div>
            <h3><fmt:message key="flightStatistics" /></h3>
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
                    <ft:flightInfoRow titleKey="longestFlightByDistance" flight="${overview.statistics.longestFlightByDistance}" />
                    <ft:flightInfoRow titleKey="shortestFlightByDuration" flight="${overview.statistics.shortestFlightByDuration}" />
                    <ft:flightInfoRow titleKey="shortestFlightByDistance" flight="${overview.statistics.shortestFlightByDistance}" />
                    <ft:flightInfoRow titleKey="fastestFlight" flight="${overview.statistics.fastestFlight}" />
                    <ft:flightInfoRow titleKey="slowestFlight" flight="${overview.statistics.slowestFlight}" />
                </tbody>
            </table>
        </c:if>

        <div class="ui divider"></div>

        <div class="ui stackable grid">
            <div class="eight wide column">
                <ft:statisticsTopItemsCard titleKey="topTenAirports" valueKey="airport" items="${overview.statistics.topAirports}" />
            </div>
            <div class="eight wide column">
                <ft:statisticsTopItemsCard titleKey="topTenAirlines" valueKey="airline" items="${overview.statistics.topAirlines}" />
            </div>
            <div class="eight wide column">
                <ft:statisticsTopItemsCard titleKey="topTenRoutes" valueKey="route" items="${overview.statistics.topRoutes}" />
            </div>
            <div class="eight wide column">
                <ft:statisticsTopItemsCard titleKey="topTenAircraftTypes" valueKey="aircraftType" items="${overview.statistics.topAircraftTypes}" />
            </div>
            <div class="four wide column">
                <ft:statisticsDistributionCard titleKey="cabinClasses" valueKey="cabinClass" localizationPrefix="cabinClass" items="${overview.statistics.distributionOfCabinClasses}" />
            </div>
            <div class="four wide column">
                <ft:statisticsDistributionCard titleKey="flightReasons" valueKey="flightReason" localizationPrefix="flightReason" items="${overview.statistics.distributionOfFlightReasons}" />
            </div>
            <div class="four wide column">
                <ft:statisticsDistributionCard titleKey="seatTypes" valueKey="seatType" localizationPrefix="seatType" items="${overview.statistics.distributionOfSeatTypes}" />
            </div>
        </div>

	</ft:body>
</ft:html>