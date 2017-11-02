<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="titleKey" type="java.lang.String" required="true" %>
<%@ attribute name="flight" type="de.perdian.apps.flighttracker.modules.flights.model.FlightBean" required="true" %>
<tr>
    <td><fmt:message key="${titleKey}" /></td>
    <td>
        <c:if test="${fn:length(flight.departureContact.airport.countryCode) gt 0}">
            <i class="${fn:toLowerCase(flight.departureContact.airport.countryCode)} flag"></i>
        </c:if>
        <c:out value="${flight.departureContact.airport.code}" />
        <c:if test="${flight.departureContact.airport.name ne null}">
            <br><small><c:out value="${flight.departureContact.airport.name}" /></small>
        </c:if>
    </td>
    <td nowrap="nowrap">
        <c:out value="${flight.departureContact.dateLocal}" />
        <c:if test="${flight.departureContact.timeLocal ne null}">
            <c:out value=" ${flight.departureContact.timeLocal}" />
        </c:if>
        <c:if test="${flight.departureContact.airport.timezoneId ne null}">
            <br><small><c:out value="${flight.departureContact.airport.timezoneId}" /></small>
        </c:if>
     </td>
    <td>
        <c:if test="${fn:length(flight.arrivalContact.airport.countryCode) gt 0}">
            <i class="${fn:toLowerCase(flight.arrivalContact.airport.countryCode)} flag"></i>
        </c:if>
        <c:out value="${flight.arrivalContact.airport.code}" />
        <c:if test="${flight.arrivalContact.airport.name ne null}">
            <br><small><c:out value="${flight.arrivalContact.airport.name}" /></small>
        </c:if>
    </td>
    <td nowrap="nowrap">
        <c:out value="${flight.arrivalContact.dateLocal}" />
        <c:if test="${flight.arrivalContact.timeLocal ne null}">
            <c:out value=" ${flight.arrivalContact.timeLocal}" />
            <c:if test="${flight.arrivalContact.airport.timezoneId ne null}">
                <br><small><c:out value="${flight.arrivalContact.airport.timezoneId}" /></small>
            </c:if>
        </c:if>
    </td>
    <td nowrap="nowrap" class="right aligned">
        <c:if test="${fn:length(flight.flightDurationString) gt 0}">
            <c:out value="${flight.flightDurationString}" /><c:out value=" " /><fmt:message key="hoursUnitSign" />
        </c:if>
    </td>
    <td nowrap="nowrap" class="right aligned">
        <c:if test="${flight.flightDistance ne null}">
            <fmt:formatNumber value="${flight.flightDistance}" pattern="#,##0" /><c:out value=" " /><fmt:message key="km" />
        </c:if>
    </td>
    <td nowrap="nowrap" class="right aligned">
        <c:if test="${flight.averageSpeed ne null}">
            <fmt:formatNumber value="${flight.averageSpeed}" pattern="#,##0" /><c:out value=" " /><fmt:message key="kmh" />
        </c:if>
    </td>
</tr>