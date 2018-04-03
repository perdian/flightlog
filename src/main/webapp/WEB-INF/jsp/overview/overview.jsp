<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fl" tagdir="/WEB-INF/tags/flightlog" %>

<fl:html>
    <fl:head>
        <link rel="stylesheet" href="https://openlayers.org/en/v4.2.0/css/ol.css" type="text/css" />
        <script src="https://openlayers.org/en/v4.2.0/build/ol.js"></script>
        <script src="https://api.mapbox.com/mapbox.js/plugins/arc.js/v0.1.0/arc.js"></script>
    </fl:head>
    <fl:body>

        <div class="ui header">
            <i class="plane icon"></i>
            <div class="content">
                <fmt:message key="overview" />
                <div class="sub header"><fmt:message key="yourPersonalFlightStatistics" /></div>
            </div>
        </div>

        <div class="ui divider"></div>

        <div class="ui stackable grid">
            <div class="twelve wide column">
                <div class="ui grid">
                    <div class="three column row">
                        <div class="column">
                            <fl:overviewTopItemsCard titleKey="flightTotals" items="${overview.general}" />
                        </div>
                        <div class="column">
                            <h3><fmt:message key="flightsByDistance" /></h3>
                            <table class="ui very basic compact table">
                                <tbody>
                                    <c:forEach items="${overview.distances}" var="distance">
                                        <tr>
                                            <td><fl:overviewLocalize value="${distance.title}" /></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${distance.context.minValue eq null and distance.context.maxValue ne null}">
                                                        <small><c:out escapeXml="false" value="&lt; " /><fmt:formatNumber value="${distance.context.maxValue}" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" /></small>
                                                    </c:when>
                                                    <c:when test="${distance.context.minValue ne null and distance.context.maxValue eq null}">
                                                        <small><c:out escapeXml="false" value="&gt; " /><fmt:formatNumber value="${distance.context.minValue}" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" /></small>
                                                    </c:when>
                                                    <c:when test="${distance.context.minValue ne null and distance.context.maxValue ne null}">
                                                        <small><fmt:formatNumber value="${distance.context.minValue}" pattern="#,##0" /><c:out value=" - " /><fmt:formatNumber value="${distance.context.maxValue}" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" /></small>
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td class="right aligned">
                                                <fmt:formatNumber value="${distance.value}" pattern="${distance.valueFormat}" />
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <div class="column">
                            <fl:overviewTopItemsCard titleKey="otherStatistics" items="${overview.differents}" />
                        </div>
                    </div>
                </div>

                <br />
                <fl:map />

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
                    <div class="field">
                        <label><fmt:message key="flightDistance" /></label>
                        <spring:select path="flightDistance" cssClass="ui dropdown" multiple="multiple">
                            <option value=""><fmt:message key="allFlightDistances" /></option>
                            <c:forEach items="${overviewQueryHelper.availableFlightDistances}" var="flightDistance">
                                <spring:option value="${flightDistance.name()}"><fmt:message key="flightDistance.${flightDistance.name()}" /></spring:option>
                            </c:forEach>
                        </spring:select>
                    </div>
                    <div class="field">
                        <label><fmt:message key="flightType" /></label>
                        <spring:select path="flightType" cssClass="ui dropdown" multiple="multiple">
                            <option value=""><fmt:message key="allFlightTypes" /></option>
                            <c:forEach items="${overviewQueryHelper.availableFlightTypes}" var="flightType">
                                <spring:option value="${flightType.name()}"><fmt:message key="flightType.${flightType.name()}" /></spring:option>
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

        <div class="ui divider"></div>

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
                <c:forEach items="${overview.maxFlights}" var="maxFlightEntry">
                    <tr>
                        <td><fmt:message key="${maxFlightEntry.key}" /></td>
                        <td>
                            <c:if test="${fn:length(maxFlightEntry.value.departureContact.airport.countryCode) gt 0}">
                                <i class="${fn:toLowerCase(maxFlightEntry.value.departureContact.airport.countryCode)} flag"></i>
                            </c:if>
                            <c:out value="${maxFlightEntry.value.departureContact.airport.code}" />
                            <c:if test="${maxFlightEntry.value.departureContact.airport.name ne null}">
                                <br><small><c:out value="${maxFlightEntry.value.departureContact.airport.name}" /></small>
                            </c:if>
                        </td>
                        <td nowrap="nowrap">
                            <c:out value="${maxFlightEntry.value.departureContact.dateLocal}" />
                            <c:if test="${maxFlightEntry.value.departureContact.timeLocal ne null}">
                                <c:out value=" ${maxFlightEntry.value.departureContact.timeLocal}" />
                            </c:if>
                            <c:if test="${maxFlightEntry.value.departureContact.airport.timezoneId ne null}">
                                <br><small><c:out value="${maxFlightEntry.value.departureContact.airport.timezoneId}" /></small>
                            </c:if>
                         </td>
                        <td>
                            <c:if test="${fn:length(maxFlightEntry.value.arrivalContact.airport.countryCode) gt 0}">
                                <i class="${fn:toLowerCase(maxFlightEntry.value.arrivalContact.airport.countryCode)} flag"></i>
                            </c:if>
                            <c:out value="${maxFlightEntry.value.arrivalContact.airport.code}" />
                            <c:if test="${maxFlightEntry.value.arrivalContact.airport.name ne null}">
                                <br><small><c:out value="${maxFlightEntry.value.arrivalContact.airport.name}" /></small>
                            </c:if>
                        </td>
                        <td nowrap="nowrap">
                            <c:out value="${maxFlightEntry.value.arrivalContact.dateLocal}" />
                            <c:if test="${maxFlightEntry.value.arrivalContact.timeLocal ne null}">
                                <c:out value=" ${maxFlightEntry.value.arrivalContact.timeLocal}" />
                                <c:if test="${maxFlightEntry.value.arrivalContact.airport.timezoneId ne null}">
                                    <br><small><c:out value="${maxFlightEntry.value.arrivalContact.airport.timezoneId}" /></small>
                                </c:if>
                            </c:if>
                        </td>
                        <td nowrap="nowrap" class="right aligned">
                            <c:if test="${fn:length(maxFlightEntry.value.flightDurationString) gt 0}">
                                <c:out value="${maxFlightEntry.value.flightDurationString}" /><c:out value=" " /><fmt:message key="hoursUnitSign" />
                            </c:if>
                        </td>
                        <td nowrap="nowrap" class="right aligned">
                            <c:if test="${maxFlightEntry.value.flightDistance ne null}">
                                <fmt:formatNumber value="${maxFlightEntry.value.flightDistance}" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" />
                            </c:if>
                        </td>
                        <td nowrap="nowrap" class="right aligned">
                            <c:if test="${maxFlightEntry.value.averageSpeed ne null}">
                                <fmt:formatNumber value="${maxFlightEntry.value.averageSpeed}" pattern="#,##0" /><c:out value=" " /><fmt:message key="kmh" />
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="ui divider"></div>

        <div class="ui stackable grid">
            <c:forEach items="${overview.topFlights}" var="topEntry">
                <div class="eight wide column">
                    <fl:overviewTopItemsCard titleKey="${topEntry.key}" items="${topEntry.value}" showIndex="true" showPercentages="true" />
                </div>
            </c:forEach>
        </div>

        <div class="ui stackable grid">
            <c:forEach items="${overview.distributions}" var="distributionEntry">
                <div class="four wide column">
                    <fl:overviewTopItemsCard titleKey="${distributionEntry.key}" items="${distributionEntry.value}" showIndex="false" showPercentages="true" />
                </div>
            </c:forEach>
        </div>

    </fl:body>
</fl:html>
