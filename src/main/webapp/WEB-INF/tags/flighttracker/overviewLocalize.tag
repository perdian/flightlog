<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="value" type="de.perdian.apps.flighttracker.modules.overview.model.OverviewItemString" required="true" %>

<c:choose>
    <c:when test="${fn:length(value.key) gt 0}"><fmt:message key="${value.key}" /></c:when>
    <c:otherwise><c:out value="${value.text}" /></c:otherwise>
</c:choose>
