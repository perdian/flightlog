<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="titleKey" type="java.lang.String" required="true" %>
<%@ attribute name="valueKey" type="java.lang.String" required="true" %>
<%@ attribute name="items" type="java.util.Collection" required="true" %>
<%@ attribute name="showFlag" type="java.lang.Boolean" required="false" %>

<h3><fmt:message key="${titleKey}" /></h3>
<table class="ui striped compact table">
    <thead>
        <tr>
            <th>&nbsp;</th>
            <th><fmt:message key="${valueKey}" /></th>
            <th class="right aligned"><fmt:message key="value" /></th>
            <th class="right aligned"><fmt:message key="percent" /></th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${items}" var="item" varStatus="itemStatus">
            <tr>
                <td class="right aligned"><c:out value="${itemStatus.index + 1}" /></td>
                <td>
                    <c:if test="${fn:length(item.context['countryCode']) gt 0}">
                        <i class="${fn:toLowerCase(item.context['countryCode'])} flag"></i>
                    </c:if>
                    <c:out value="${item.title}" />
                    <c:if test="${fn:length(item.description) gt 0}">
                        <small><br /><c:out value="${item.description}" /></small>
                    </c:if>
                </td>
                <td class="right aligned"><fmt:formatNumber value="${item.value}" pattern="#,##0" /></td>
                <td class="right aligned">
                    <c:if test="${item.percentage ne null}">
                        <fmt:formatNumber value="${item.percentage}" pattern="#,##0" />&nbsp;%
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
