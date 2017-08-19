<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ft" tagdir="/WEB-INF/tags/flighttracker" %>

<ft:html>
    <ft:head />
    <ft:body>

        <h1 class="ui header"><fmt:message key="verifyImportedFlights" /></h1>

        <spring:form modelAttribute="importEditor" servletRelativeAction="/importexport/import/execute" cssClass="ui form">

            <h3 class="ui dividing header"><fmt:message key="actions" /></h3>
            <div class="sixteen wide">
                <button class="ui primary button"><fmt:message key="importFlights" /></button>
            </div>

            <h3 class="ui dividing header"><fmt:message key="flights" /></h3>
            <table class="ui celled striped compact table">
                <thead>
                    <tr>
                        <th><fmt:message key="use" /></th>
                        <th><fmt:message key="flight" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${importEditor.items}" var="item" varStatus="itemStatus">
                        <tr>
                            <td>
                                <div class="ui fitted checkbox">
                                    <spring:checkbox path="items[${itemStatus.index}].active"/> <label></label>
                                </div>
                            </td>
                            <td>
                                <div class="fields">
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.departureAirportCode" labelKey="airportCode" cssClass="two wide field" />
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.departureDateLocal" labelKey="departureDate" cssClass="two wide field" />
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.departureTimeLocal" labelKey="departureTime" cssClass="two wide field" />
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.arrivalAirportCode" labelKey="airportCode" cssClass="two wide field" />
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.arrivalDateLocal" labelKey="arrivalDate" cssClass="two wide field" />
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.arrivalTimeLocal" labelKey="arrivalTime" cssClass="two wide field" />
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.flightDuration" labelKey="durationInIso8601" cssClass="two wide field" />
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.flightDistance" labelKey="distanceInKm" cssClass="two wide field" />
                                </div>
                                <div class="fields">
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.airlineName" labelKey="airlineName" cssClass="four wide field" />
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.airlineCode" labelKey="airlineCode" cssClass="two wide field" />
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.flightNumber" labelKey="flightNumber" cssClass="two wide field" />
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.aircraftType" labelKey="aircraftType" cssClass="four wide field" />
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.aircraftRegistration" labelKey="aircraftRegistration" cssClass="two wide field" />
                                    <ft:inputfield bean="importEditor" path="items[${itemStatus.index}].flight.aircraftName" labelKey="aircraftName" cssClass="two wide field" />
                                </div>
                                <div class="fields">
                                    <ft:inputfield cssClass="two wide field" bean="importEditor" path="items[${itemStatus.index}].flight.seatNumber" labelKey="seat" />
                                    <ft:select cssClass="two wide field" bean="importEditor" path="items[${itemStatus.index}].flight.seatType" labelKey="type">
                                        <option></option>
                                        <c:forEach items="${flightEditorHelper.seatTypeValues}" var="seatTypeValue">
                                            <option ${item.flight.seatType eq seatTypeValue ? 'selected=\"selected\"' : ''} value="${seatTypeValue}"><fmt:message key="seatType.${seatTypeValue}" /></option>
                                        </c:forEach>
                                    </ft:select>
                                    <ft:select cssClass="two wide field" bean="flightEditor" path="items[${itemStatus.index}].flight.cabinClass" labelKey="cabinClass">
                                        <option></option>
                                        <c:forEach items="${flightEditorHelper.cabinClassValues}" var="cabinClassValue">
                                            <option ${item.flight.cabinClass eq cabinClassValue ? 'selected=\"selected\"' : ''} value="${cabinClassValue}"><fmt:message key="cabinClass.${cabinClassValue}" /></option>
                                        </c:forEach>
                                    </ft:select>
                                    <ft:select cssClass="two wide field" bean="flightEditor" path="items[${itemStatus.index}].flight.flightReason" labelKey="flightReason">
                                        <option></option>
                                        <c:forEach items="${flightEditorHelper.flightReasonValues}" var="flightReasonValue">
                                            <option ${item.flight.flightReason eq flightReasonValue ? 'selected=\"selected\"' : ''} value="${flightReasonValue}"><fmt:message key="flightReason.${flightReasonValue}" /></option>
                                        </c:forEach>
                                    </ft:select>
                                    <ft:inputfield cssClass="eight wide field" bean="importEditor" path="items[${itemStatus.index}].flight.comment" labelKey="comment" />
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <h3 class="ui dividing header"><fmt:message key="actions" /></h3>
            <div class="sixteen wide">
                <button class="ui primary button"><fmt:message key="importFlights" /></button>
            </div>

        </spring:form>

    </ft:body>
</ft:html>


