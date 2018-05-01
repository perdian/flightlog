<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fl" tagdir="/WEB-INF/tags/flightlog" %>

<fl:html>
    <fl:head />
    <fl:body>

        <div class="ui header">
            <i class="add square icon"></i>
            <div class="content">
                <fmt:message key="addFlight" />
                <div class="sub header"><fmt:message key="enterDetailsForNewFlight" /></div>
            </div>
        </div>

        <c:if test="${wizardFlightEditors ne null and fn:length(wizardFlightEditors) gt 0}">
            <div class="ui top attached information message">
                <i class="warning icon"></i>
                <fmt:message key="moreThanOneFlightWasFoundForTheDataYouEnteredIntoTheWizardSelectTheFlightYouWishToUse" />
            </div>
            <c:forEach items="${wizardFlightEditors}" var="wizardFlightEditor">
                <div class="ui attached segment">
                    <spring:form modelAttribute="flightEditor" servletRelativeAction="/flights/add" cssClass="ui form">

                        <input name="departureDateLocal" type="hidden" value="${wizardFlightEditor.departureDateLocal}" />
                        <input name="departureTimeLocal" type="hidden" value="${wizardFlightEditor.departureTimeLocal}" />
                        <input name="departureAirportCode" type="hidden" value="${wizardFlightEditor.departureAirportCode}" />
                        <input name="departureAirportName" type="hidden" value="${wizardFlightEditor.departureAirportName}" />
                        <input name="arrivalDateLocal" type="hidden" value="${wizardFlightEditor.arrivalDateLocal}" />
                        <input name="arrivalTimeLocal" type="hidden" value="${wizardFlightEditor.arrivalTimeLocal}" />
                        <input name="arrivalAirportCode" type="hidden" value="${wizardFlightEditor.arrivalAirportCode}" />
                        <input name="arrivalAirportName" type="hidden" value="${wizardFlightEditor.arrivalAirportName}" />
                        <input name="flightNumber" type="hidden" value="${wizardFlightEditor.flightNumber}" />
                        <input name="flightDuration" type="hidden" value="${wizardFlightEditor.flightDuration}" />
                        <input name="flightDistance" type="hidden" value="${wizardFlightEditor.flightDistance}" />
                        <input name="airlineCode" type="hidden" value="${wizardFlightEditor.airlineCode}" />
                        <input name="airlineName" type="hidden" value="${wizardFlightEditor.airlineName}" />
                        <input name="aircraftType" type="hidden" value="${wizardFlightEditor.aircraftType}" />
                        <input name="aircraftRegistration" type="hidden" value="${wizardFlightEditor.aircraftRegistration}" />
                        <input name="aircraftName" type="hidden" value="${wizardFlightEditor.aircraftName}" />
                        <input name="seatNumber" type="hidden" value="${wizardFlightEditor.seatNumber}" />
                        <input name="seatType" type="hidden" value="${wizardFlightEditor.seatType}" />
                        <input name="cabinClass" type="hidden" value="${wizardFlightEditor.cabinClass}" />
                        <input name="flightReason" type="hidden" value="${wizardFlightEditor.flightReason}" />

                        <div class="fields">
                            <div class="four wide field">
                                <button class="ui tiny compact primary button" type="submit">
                                    <i class="check icon"></i>
                                    <fmt:message key="selectThisFlight" />
                                </button>
                            </div>
                            <div class="three wide field">
                                <label><fmt:message key="departure" /></label>
                                <strong><c:out value="${wizardFlightEditor.departureAirportCode}" /></strong>
                                <c:if test="${fn:length(wizardFlightEditor.departureAirportCountryCode) gt 0}">
                                    &nbsp;<c:out value=" " /><i class="${fn:toLowerCase(wizardFlightEditor.departureAirportCountryCode)} flag"></i>
                                </c:if>
                                <br /><c:out value="${wizardFlightEditor.departureDateLocal}" />
                                <c:if test="${fn:length(wizardFlightEditor.departureTimeLocal) gt 0}">
                                    <br /><c:out value="${wizardFlightEditor.departureTimeLocal}" />
                                </c:if>
                            </div>
                            <div class="three wide field">
                                <label><fmt:message key="arrival" /></label>
                                <strong><c:out value="${wizardFlightEditor.arrivalAirportCode}" /></strong>
                                <c:if test="${fn:length(wizardFlightEditor.arrivalAirportCountryCode) gt 0}">
                                    &nbsp;<c:out value=" " /><i class="${fn:toLowerCase(wizardFlightEditor.arrivalAirportCountryCode)} flag"></i>
                                </c:if>
                                <br /><c:out value="${wizardFlightEditor.arrivalDateLocal}" />
                                <c:if test="${fn:length(wizardFlightEditor.arrivalTimeLocal) gt 0}">
                                    <br /><c:out value="${wizardFlightEditor.arrivalTimeLocal}" />
                                </c:if>
                            </div>
                        </div>

                    </spring:form>
                </div>
            </c:forEach>
        </c:if>

        <spring:form modelAttribute="flightEditor" servletRelativeAction="/flights/add" cssClass="ui form">

            <jsp:include page="include/flight-data.jsp" />

            <div class="ui horizontal divider"><fmt:message key="actions" /></div>

            <div class="sixteen wide">
                <button class="ui primary button">
                    <i class="save icon"></i>
                    <fmt:message key="save" />
                </button>
            </div>

        </spring:form>

    </fl:body>
</fl:html>
