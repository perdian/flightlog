<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="titleKey" type="java.lang.String" required="true" %>
<%@ attribute name="valueKey" type="java.lang.String" required="true" %>
<%@ attribute name="localizationPrefix" type="java.lang.String" required="true" %>
<%@ attribute name="items" type="java.util.Collection" required="true" %>

<div class="card">
    <div class="content">
        <div class="header">
            <fmt:message key="${titleKey}" />
        </div>
        <div class="description">
            <table class="ui striped compact table">
                <thead>
                    <tr>
                        <th><fmt:message key="${valueKey}" /></th>
                        <th class="right aligned"><fmt:message key="value" /></th>
                        <th class="right aligned"><fmt:message key="percent" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${items}" var="item" varStatus="itemStatus">
                        <tr>
                            <td><fmt:message key="${localizationPrefix}.${item.title}" /></td>
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
        </div>
    </div>
</div>
