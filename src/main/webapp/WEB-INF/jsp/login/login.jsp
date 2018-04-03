<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fl" tagdir="/WEB-INF/tags/flightlog" %>

<fl:html>
    <fl:head />
    <fl:body>

        <div class="ui header">
            <i class="user icon"></i>
            <div class="content">
                <fmt:message key="login" />
                <div class="sub header"><fmt:message key="enterYourCredentials" /></div>
            </div>
        </div>

        <form action="login" method="post" class="ui form">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <div class="ui divider"></div>
            <div class="fields">
                <div class="six wide field">
                    <label><fmt:message key="username" /></label>
                    <input name="username" value="<c:out value="${param.username}" />" />
                </div>
                <div class="six wide field">
                    <label><fmt:message key="password" /></label>
                    <input name="password" type="password" value="<c:out value="${param.password}" />" />
                </div>
                <div class="one wide field">
                    <label>&nbsp;</label>
                    <input class="ui primary button" type="submit" value="<fmt:message key="login" />" />
                </div>
            </div>
        </form>

    </fl:body>
</fl:html>
