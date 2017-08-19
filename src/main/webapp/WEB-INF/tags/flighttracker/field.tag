<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ attribute name="bean" type="java.lang.String" required="true" %>
<%@ attribute name="path" type="java.lang.String" required="true" %>
<%@ attribute name="cssClass" type="java.lang.String" required="false" %>
<%@ attribute name="labelKey" type="java.lang.String" required="false" %>
<%@ attribute name="readonly" type="java.lang.String" required="false" %>

<c:set var="consolidatedCssClasses" value="${cssClass}" />
<c:set var="beanValidationObjectName" value="org.springframework.validation.BindingResult.${bean}" />
<c:set var="beanValidationObject" value="${requestScope[beanValidationObjectName]}" />

<c:if test="${beanValidationObject.hasFieldErrors(path)}">
    <c:set var="consolidatedCssClasses" value="${consolidatedCssClasses} error" />
</c:if>

<div class="${consolidatedCssClasses}">
    <c:if test="${fn:length(labelKey) gt 0}">
        <label><fmt:message key="${labelKey}" /></label>
    </c:if>
    <jsp:doBody />
</div>
