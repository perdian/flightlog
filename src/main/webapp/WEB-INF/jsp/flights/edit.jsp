<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ft" tagdir="/WEB-INF/tags/flighttracker" %>

<ft:html>
    <ft:head />
    <ft:body>

        <h2 class="ui header">
            <i class="edit icon"></i>
            <div class="content">
                <fmt:message key="editFlight" />
                <div class="sub header"><fmt:message key="enterDetailsForFlight" /></div>
            </div>
        </h2>

        <spring:form modelAttribute="flightEditor" servletRelativeAction="/flights/edit/${flightEditor.entityId}" cssClass="ui form">

            <jsp:include page="include/flight-data.jsp" />

            <div class="ui horizontal divider"><fmt:message key="actions" /></div>
            <div class="sixteen wide">
                <button class="ui primary button">
                    <i class="save icon"></i>
                    <fmt:message key="save" />
                </button>
            </div>

        </spring:form>

    </ft:body>
</ft:html>