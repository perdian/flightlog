<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ft" tagdir="/WEB-INF/tags/flighttracker" %>

<ft:html>
    <ft:head />
    <ft:body>

        <h1 class="ui header"><fmt:message key="deleteFlight" /></h1>

        <form method="post" action="<c:url value="/flights/delete/${flight.entityId}" />">

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <h3 class="ui dividing header"><fmt:message key="flightData" /></h3>
            <div class="ui grid">
                <div class="four wide column">
                    <h4><fmt:message key="flightData" /></h4>
                    <div>
                        <c:if test="${fn:length(flight.airline.code) gt 0}">
                            <c:out value="${flight.airline.code} " />
                        </c:if>
                        <c:out value="${flight.flightNumber}" />
                        <c:if test="${fn:length(flight.airline.name) gt 0}">
                            <small><br /><c:out value="${flight.airline.name} " /></small>
                        </c:if>
                    </div>
                </div>
                <div class="four wide column">
                    <h4><fmt:message key="departure" /></h4>
                    <div>
                        <c:out value="${flight.departureContact.airport.code}" />
                        <c:if test="${fn:length(flight.departureContact.airport.name) gt 0}">
                            <small><br /><c:out value="${flight.departureContact.airport.name}" /></small>
                        </c:if>
                    </div>
                    <div>
                        <c:out value="${flight.departureContact.dateLocal}" />
                        <c:if test="${flight.departureContact.timeLocal ne null}">
                            <c:out value=" ${flight.departureContact.timeLocal}" />
                        </c:if>
                    </div>
                </div>
                <div class="four wide column">
                    <h4><fmt:message key="arrival" /></h4>
                    <div>
                        <c:out value="${flight.arrivalContact.airport.code}" />
                        <c:if test="${fn:length(flight.arrivalContact.airport.name) gt 0}">
                            <small><br /><c:out value="${flight.arrivalContact.airport.name}" /></small>
                        </c:if>
                    </div>
                    <div>
                        <c:out value="${flight.arrivalContact.dateLocal}" />
                        <c:if test="${flight.arrivalContact.timeLocal ne null}">
                            <c:out value=" ${flight.arrivalContact.timeLocal}" />
                        </c:if>
                    </div>
                </div>
                <div class="four wide column">
                </div>
            </div>



            <h3 class="ui dividing header"><fmt:message key="actions" /></h3>
            <div class="sixteen wide">
                <button class="ui negative button"><fmt:message key="delete" /></button>
            </div>

        </form>

    </ft:body>
</ft:html>