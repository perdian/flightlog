<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<body>

    <nav class="ui inverted menu">

        <div class="active item"><strong><fmt:message key="flightTracker.pageTitle" /></strong></div>

        <a class="item" href="${contextPath}"><fmt:message key="overview" /></a>
        <a class="item" href="${contextPath}flights/list"><fmt:message key="listFlights" /></a>
        <a class="item" href="${contextPath}flights/add"><fmt:message key="addFlight" /></a>
        <a class="item" href="${contextPath}" onclick="JavaScript:$('#add-flight-wizard').modal('show');return false;"><fmt:message key="wizard" /></a>

        <div class="ui simple dropdown item">
            <fmt:message key="tools" /><c:out value=" " /><i class="dropdown icon"></i><c:out value=" " />
            <div class="menu">
                <div class="ui dropdown item">
                    <i class="dropdown icon"></i><c:out value=" " /><fmt:message key="import" /><c:out value=" " />
                    <div class="menu">
                        <a class="item" href="${contextPath}import/file"><fmt:message key="file" /></a>
                        <a class="item" href="${contextPath}import/flugstatistikde"><fmt:message key="flugstatistikde" /></a>
                    </div>
                </div>
                <div class="ui dropdown item">
                    <i class="dropdown icon"></i><c:out value=" " /><fmt:message key="export" /><c:out value=" " />
                    <div class="menu">
                        <a class="item" href="${contextPath}export/xml"><fmt:message key="fileType.XML" /></a>
                        <a class="item" href="${contextPath}export/json"><fmt:message key="fileType.JSON" /></a>
                        <a class="item" href="${contextPath}export/openflightscsv"><fmt:message key="fileType.OPENFLIGHTSCSV" /></a>
                    </div>
                </div>
            </div>
        </div>
        <sec:authorize access="isAuthenticated()">
            <div class="ui simple dropdown item">
                <fmt:message key="user" /><c:out value=" " /><i class="dropdown icon"></i><c:out value=" " />
                <div class="ui menu">
                    <a class="item" href="${contextPath}logout"><fmt:message key="logout" /></a>
                </div>
            </div>
        </sec:authorize>

    </nav>

    <main>

        <c:if test="${fn:length(messages.messagesBySeverity) gt 0}">
            <div class="messages">
                <c:forEach items="${messages.messagesBySeverity}" var="messagesEntry">
                    <c:forEach items="${messagesEntry.value}" var="message">
                        <div class="ui ${messagesEntry.key.color} message">
                            <div class="header"><c:out value="${message.title}" /></div>
                            <c:if test="${fn:length(message.text) gt 0}">
                                <p><c:out value="${message.text}" /></p>
                            </c:if>
                        </div>
                    </c:forEach>
                </c:forEach>
            </div>
        </c:if>

        <jsp:doBody />

    </main>

    <div class="ui tiny inverted menu">
        <div class="active item"><strong><fmt:message key="flightTracker.pageTitle" /></strong></div>
        <a class="right aligned item" href="https://github.com/perdian/flighttracker" target="_blank">https://github.com/perdian/flighttracker</a>
    </div>

    <script type="text/javascript">
	    	$('select.dropdown').dropdown();
    </script>

    <form id="add-flight-wizard" class="ui tiny modal" method="post" action="${contextPath}flights/add/wizard">
        <i class="close icon"></i>

        <div class="ui icon header">
            <i class="wizard icon"></i>
            <fmt:message key="addFlightWizard" />
        </div>

        <div class="content">
            <div class="description">
                <p><fmt:message key="addFlightWizard.description" /></p>
            </div>
            <div class="ui divider"></div>
            <div class="ui form">
                <div class="fields">
                    <div class="three wide field">
                        <label><fmt:message key="departureDate" /></label>
                        <input name="wizDepartureDateLocal" placeholder="yyyy-MM-dd" />
                    </div>
                    <div class="three wide field">
                        <label><fmt:message key="departureTime" /></label>
                        <input name="wizDepartureTimeLocal" placeholder="HH:mm" />
                    </div>
                    <div class="three wide field">
                        <label><fmt:message key="departureAirportCode" /></label>
                        <input name="wizDepartureAirportCode" />
                    </div>
                    <div class="three wide field">
                        <label><fmt:message key="arrivalAirportCode" /></label>
                        <input name="wizArrivalAirportCode" />
                    </div>
                    <div class="two wide field">
                        <label><fmt:message key="airlineCode" /></label>
                        <input name="wizAirlineCode" />
                    </div>
                    <div class="two wide field">
                        <label><fmt:message key="flightNumber" /></label>
                        <input name="wizFlightNumber" />
                    </div>
                </div>
            </div>
        </div>
        <div class="actions">
            <div class="ui black deny icon button">
                <i class="x icon"></i>
                <fmt:message key="cancel" />
            </div>
            <button type="submit" class="ui positive icon button">
                <i class="wizard icon"></i>
                <fmt:message key="proceed" />
            </button>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

    </form>

</body>