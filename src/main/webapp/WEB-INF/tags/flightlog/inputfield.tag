<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fl" tagdir="/WEB-INF/tags/flightlog" %>
<%@ attribute name="cssClass" type="java.lang.String" required="false" %>
<%@ attribute name="inputFieldCssClass" type="java.lang.String" required="false" %>
<%@ attribute name="bean" type="java.lang.String" required="true" %>
<%@ attribute name="path" type="java.lang.String" required="true" %>
<%@ attribute name="labelKey" type="java.lang.String" required="false" %>
<%@ attribute name="placeholder" type="java.lang.String" required="false" %>

<fl:field cssClass="${cssClass}" bean="${bean}" path="${path}" labelKey="${labelKey}">
    <spring:input path="${path}" placeholder="${placeholder}" cssClass="${inputFieldCssClass}" />
</fl:field>
