<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fl" tagdir="/WEB-INF/tags/flightlog" %>

<div class="ui grid">
    <div class="one column mobile only row">
        <div class="column">
            <table class="ui celled striped compact table">
                <thead>
                    <tr>
                        <th><fmt:message key="date" /><br /><fmt:message key="flight" /></th>
                        <th><fmt:message key="departure" /></th>
                        <th><fmt:message key="arrival" /></th>
                        <th><fmt:message key="flightNumber" /><br /><fmt:message key="airline" /></th>
                        <th><fmt:message key="action" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${flights.items}" var="flight" varStatus="flightStatus">
                        <tr>
                            <td nowrap="nowrap">
                                <strong><c:out value="${flight.departureContact.dateLocal}" /></strong>
                                <c:if test="${flight.departureContact.timeLocal ne null and flight.arrivalContact.timeLocal ne null}">
                                    <br /><c:out value="${flight.departureContact.timeLocal}" /><c:out value=" - " /><c:out value="${flight.arrivalContact.timeLocal}" />
                                    <c:if test="${flight.arrivalContact.dateOffset ne null}">
                                        <c:out value=" ${flight.arrivalContact.dateOffset}" />
                                    </c:if>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${fn:length(flight.departureContact.airport.countryCode) gt 0}">
                                    <i class="${fn:toLowerCase(flight.departureContact.airport.countryCode)} flag"></i>
                                </c:if>
                                <strong><c:out value="${flight.departureContact.airport.code}" /></strong>
                                <c:if test="${fn:length(flight.departureContact.airport.name) gt 0}">
                                    <small><br /><c:out value="${flight.departureContact.airport.name}" /></small>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${fn:length(flight.arrivalContact.airport.countryCode) gt 0}">
                                    <i class="${fn:toLowerCase(flight.arrivalContact.airport.countryCode)} flag"></i>
                                </c:if>
                                <strong><c:out value="${flight.arrivalContact.airport.code}" /></strong>
                                <c:if test="${fn:length(flight.arrivalContact.airport.name) gt 0}">
                                    <small><br /><c:out value="${flight.arrivalContact.airport.name}" /></small>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${fn:length(flight.flightNumber) gt 0}">
                                    <div><strong><c:out value="${flight.airline.code}" />&nbsp;<c:out value="${flight.flightNumber}" /></strong></div>
                                </c:if>
                                <c:if test="${fn:length(flight.airline.name) gt 0}">
                                    <div><c:out value="${flight.airline.name}" /></div>
                                </c:if>
                            </td>
                            <td nowrap="nowrap">
                                <div class="ui vertical mini buttons">
                                    <a class="ui primary button" href="${contextPath}flights/edit/${flight.entityId}"><i class="edit icon"></i><fmt:message key="edit" /></a>
                                    <a class="ui negative button" href="${contextPath}flights/delete/${flight.entityId}"><i class="remove icon"></i><fmt:message key="delete" /></a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div class="one column tablet computer only row">
        <div class="column">
            <table class="ui celled striped compact table">
                <thead>
                    <tr>
                        <th><fmt:message key="date" /><br /><fmt:message key="flight" /></th>
                        <th><fmt:message key="departure" /></th>
                        <th><fmt:message key="arrival" /></th>
                        <th><fmt:message key="distance" /><br /><fmt:message key="duration" /></th>
                        <th><fmt:message key="flightNumber" /><br /><fmt:message key="airline" /></th>
                        <th><fmt:message key="aircraft" /></th>
                        <th><fmt:message key="seatCabinClass" /></th>
                        <th><fmt:message key="action" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${flights.items}" var="flight" varStatus="flightStatus">
                        <tr>
                            <td nowrap="nowrap">
                                <strong><c:out value="${flight.departureContact.dateLocal}" /></strong>
                                <c:if test="${flight.departureContact.timeLocal ne null and flight.arrivalContact.timeLocal ne null}">
                                    <br /><c:out value="${flight.departureContact.timeLocal}" /><c:out value=" - " /><c:out value="${flight.arrivalContact.timeLocal}" />
                                    <c:if test="${flight.arrivalContact.dateOffset ne null}">
                                        <c:out value=" ${flight.arrivalContact.dateOffset}" />
                                    </c:if>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${fn:length(flight.departureContact.airport.countryCode) gt 0}">
                                    <i class="${fn:toLowerCase(flight.departureContact.airport.countryCode)} flag"></i>
                                </c:if>
                                <strong><c:out value="${flight.departureContact.airport.code}" /></strong>
                                <c:if test="${fn:length(flight.departureContact.airport.name) gt 0}">
                                    <small><br /><c:out value="${flight.departureContact.airport.name}" /></small>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${fn:length(flight.arrivalContact.airport.countryCode) gt 0}">
                                    <i class="${fn:toLowerCase(flight.arrivalContact.airport.countryCode)} flag"></i>
                                </c:if>
                                <strong><c:out value="${flight.arrivalContact.airport.code}" /></strong>
                                <c:if test="${fn:length(flight.arrivalContact.airport.name) gt 0}">
                                    <small><br /><c:out value="${flight.arrivalContact.airport.name}" /></small>
                                </c:if>
                            </td>
                            <td class="right aligned" nowrap="nowrap">
                                <c:if test="${flight.flightDistance ne null}">
                                    <div><fmt:formatNumber value="${flight.flightDistance}" pattern="#,##0" />&nbsp;<fmt:message key="km" /></div>
                                </c:if>
                                <c:if test="${flight.flightDurationString ne null}">
                                    <div><c:out value="${flight.flightDurationString}" />&nbsp;<fmt:message key="hoursUnitSign" /></div>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${fn:length(flight.flightNumber) gt 0}">
                                    <div><strong><c:out value="${flight.airline.code}" />&nbsp;<c:out value="${flight.flightNumber}" /></strong></div>
                                </c:if>
                                <c:if test="${fn:length(flight.airline.name) gt 0}">
                                    <div><c:out value="${flight.airline.name}" /></div>
                                </c:if>
                            </td>
                            <td>
                                <div><strong><c:out value="${flight.aircraft.type}" /></strong></div>
                                <div><c:out value="${flight.aircraft.registration}" /></div>
                                <c:if test="${fn:length(flight.aircraft.name) gt 0}">
                                    <div><c:out value="${flight.aircraft.name}" /></div>
                                </c:if>
                            </td>
                            <td>
                                <div><strong><c:out value="${flight.seatNumber}" /></strong></div>
                                <div>
                                    <c:choose>
                                        <c:when test="${flight.seatType ne null}"><fmt:message key="seatType.${flight.seatType.name()}" /></c:when>
                                        <c:otherwise>&nbsp;</c:otherwise>
                                    </c:choose>
                                </div>
                                <div>
                                    <c:choose>
                                        <c:when test="${flight.cabinClass ne null}"><fmt:message key="cabinClass.${flight.cabinClass.name()}" /></c:when>
                                        <c:otherwise>&nbsp;</c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                            <td nowrap="nowrap">
                                <div class="ui vertical mini buttons">
                                    <a class="ui primary button" href="${contextPath}flights/edit/${flight.entityId}"><i class="edit icon"></i><fmt:message key="edit" /></a>
                                    <a class="ui negative button" href="${contextPath}flights/delete/${flight.entityId}"><i class="remove icon"></i><fmt:message key="delete" /></a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
