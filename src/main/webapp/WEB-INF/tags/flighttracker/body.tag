<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<body>

    <nav class="ui inverted menu">

        <div class="active item"><strong><fmt:message key="flightTracker.pageTitle" /></strong></div>

        <sec:authorize access="isAuthenticated()">

            <a class="item" href="${contextPath}"><fmt:message key="overview" /></a>
            <a class="item" href="${contextPath}flights/list"><fmt:message key="listFlights" /></a>
            <a class="item" href="${contextPath}flights/add"><fmt:message key="addFlight" /></a>

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

    <script type="text/javascript">
	    	$('select.dropdown').dropdown();
    </script>

</body>