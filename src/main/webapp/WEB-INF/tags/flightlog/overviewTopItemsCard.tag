<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fl" tagdir="/WEB-INF/tags/flightlog" %>
<%@ attribute name="titleKey" type="java.lang.String" required="true" %>
<%@ attribute name="items" type="java.util.Collection" required="true" %>
<%@ attribute name="showIndex" type="java.lang.Boolean" required="false" %>
<%@ attribute name="showPercentages" type="java.lang.Boolean" required="false" %>

<h3><fmt:message key="${titleKey}" /></h3>
<table class="ui very basic compact table">
    <tbody>
        <c:forEach items="${items}" var="item" varStatus="itemStatus">
            <tr>
                <c:if test="${showIndex}">
                    <td width="25" class="right aligned"><c:out value="${itemStatus.index + 1}" /></td>
                </c:if>
                <td>
                    <c:if test="${fn:length(item.context['countryCode']) gt 0}">
                        <i class="${fn:toLowerCase(item.context['countryCode'])} flag"></i>
                    </c:if>
                    <fl:overviewLocalize value="${item.title}" />
                    <c:if test="${item.description.available}">
                        <small><br /><fl:overviewLocalize value="${item.description}" /></small>
                    </c:if>
                </td>
                <td class="right aligned">
                    <fmt:formatNumber value="${item.value}" pattern="${item.valueFormat}" />
                </td>
                <c:if test="${showPercentages}">
                    <td class="right aligned" width="15">
                        <c:if test="${item.percentage ne null}">
                            (<fmt:formatNumber value="${item.percentage}" pattern="0" />&nbsp;%)
                        </c:if>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
    </tbody>
</table>
