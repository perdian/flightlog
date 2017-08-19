<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ft" tagdir="/WEB-INF/tags/flighttracker" %>

<ft:html>
    <ft:head />
    <ft:body>

        <h1 class="ui header"><fmt:message key="import" />: <fmt:message key="flugstatistikde" /></h1>

        <spring:form modelAttribute="importEditor" servletRelativeAction="/importexport/import/flugstatistikde" cssClass="ui form">

            <h3 class="ui dividing header"><fmt:message key="source" /></h3>
            <div class="fields">
                <div class="field six wide field">
                    <label><fmt:message key="username" /></label>
                    <input name="username" value="<c:out value="${param.username}" />" />
                </div>
                <div class="field six wide field">
                    <label><fmt:message key="password" /></label>
                    <input name="password" type="password" value="<c:out value="${param.password}" />" />
                </div>
            </div>

            <h3 class="ui dividing header"><fmt:message key="actions" /></h3>
            <div class="sixteen wide">
                <button class="ui primary button"><fmt:message key="importData" /></button>
            </div>

        </spring:form>

    </ft:body>
</ft:html>


