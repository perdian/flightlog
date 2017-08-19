<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ft" tagdir="/WEB-INF/tags/flighttracker" %>

<ft:html>
    <ft:head />
    <ft:body>

        <h1 class="ui header"><fmt:message key="importCompleted" /></h1>

        <h3 class="ui dividing header"><fmt:message key="importedFlights" />: <c:out value="${fn:length(importEditor.items)}" /></h3>
        <a class="ui primary button" href="${contextPath}/flights/list"><fmt:message key="listFlights" /></a>

    </ft:body>
</ft:html>


