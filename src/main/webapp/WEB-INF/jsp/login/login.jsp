<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ft" tagdir="/WEB-INF/tags/flighttracker" %>

<ft:html>
    <ft:head />
    <ft:body>

        <h2><fmt:message key="login" /></h2>

        <form action="login" method="post" class="ui form">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <h3 class="ui dividing header"><fmt:message key="source" /></h3>
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
                    <input class="ui button" type="submit" value="<fmt:message key="login" />" />
                </div>
            </div>
        </form>

    </ft:body>
</ft:html>