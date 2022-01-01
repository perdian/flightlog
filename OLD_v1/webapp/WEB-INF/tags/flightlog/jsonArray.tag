<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="value" type="java.util.Collection" required="true" %>
<c:out value="[" />
<c:forEach items="${value}" var="v" varStatus="vStatus">
    <c:if test="${vStatus.index gt 0}"><c:out value=", " /></c:if>
    <c:out escapeXml="false" value="'" /><c:out value="${v}" /><c:out escapeXml="false" value="'" />
</c:forEach>
<c:out value="]" />