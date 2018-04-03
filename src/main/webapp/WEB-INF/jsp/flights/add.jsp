<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fl" tagdir="/WEB-INF/tags/flightlog" %>

<fl:html>
    <fl:head />
    <fl:body>

        <h2 class="ui header">
            <i class="add square icon"></i>
            <div class="content">
                <fmt:message key="addFlight" />
                <div class="sub header"><fmt:message key="enterDetailsForNewFlight" /></div>
            </div>
        </h2>

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
