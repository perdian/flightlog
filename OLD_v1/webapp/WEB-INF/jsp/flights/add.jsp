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
                <div class="header">
                    <i class="warning icon"></i>
                    <fmt:message key="moreThanOneFlightWasFoundForTheDataYouEnteredIntoTheWizardSelectTheFlightYouWishToUse" />
                </div>
            </div>
            <div class="ui attached segment">
                <div class="ui cards">
                    <c:forEach items="${wizardFlightEditors}" var="wizardFlightEditor">
                        <spring:form servletRelativeAction="/flights/add" cssClass="card">

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

                            <div class="content">
                                <div class="header">
                                    <span class="right floated"><c:out value="${wizardFlightEditor.airlineCode} ${wizardFlightEditor.flightNumber}" /></span>
                                    <c:out value="${wizardFlightEditor.departureAirportCode}" />
                                    <c:out value=" " /><small>-</small><c:out value=" " />
                                    <c:out value="${wizardFlightEditor.arrivalAirportCode}" />
                                </div>
                                <div class="description">
                                    <table class="ui very basic compact collapsing  table">
                                        <tr>
                                            <td><fmt:message key="departure" /></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${fn:length(wizardFlightEditor.departureTimeLocal) gt 0}"><c:out value="${wizardFlightEditor.departureTimeLocal}" /></c:when>
                                                    <c:otherwise>&nbsp;</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${fn:length(wizardFlightEditor.departureTimeLocal) gt 0 and fn:length(wizardFlightEditor.departureDateLocal) gt 0}"><c:out value="${wizardFlightEditor.departureDateLocal}" /></c:when>
                                                    <c:otherwise>&nbsp;</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:if test="${fn:length(wizardFlightEditor.departureAirportCountryCode) gt 0}">
                                                    <c:out value=" " /><i class="${fn:toLowerCase(wizardFlightEditor.departureAirportCountryCode)} flag"></i>
                                                </c:if>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><fmt:message key="arrival" /></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${fn:length(wizardFlightEditor.arrivalTimeLocal) gt 0}"><c:out value="${wizardFlightEditor.arrivalTimeLocal}" /></c:when>
                                                    <c:otherwise>&nbsp;</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${fn:length(wizardFlightEditor.arrivalTimeLocal) gt 0 and fn:length(wizardFlightEditor.arrivalDateLocal) gt 0}"><c:out value="${wizardFlightEditor.arrivalDateLocal}" /></c:when>
                                                    <c:otherwise>&nbsp;</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:if test="${fn:length(wizardFlightEditor.arrivalAirportCountryCode) gt 0}">
                                                    <c:out value=" " /><i class="${fn:toLowerCase(wizardFlightEditor.arrivalAirportCountryCode)} flag"></i>
                                                </c:if>
                                            </td>
                                         </tr>
                                    </table>
                                </div>
                            </div>
                            <button class="ui bottom attached primary button" type="submit">
                                <i class="check icon"></i>
                                <fmt:message key="selectThisFlight" />
                            </button>

                        </spring:form>
                    </c:forEach>
                </div>
            </div>

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
