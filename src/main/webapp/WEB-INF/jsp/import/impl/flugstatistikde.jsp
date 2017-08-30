<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ft" tagdir="/WEB-INF/tags/flighttracker" %>

<ft:html>
    <ft:head />
    <ft:body>

        <h2 class="ui header">
            <i class="cube icon"></i>
            <div class="content">
                <fmt:message key="import" />
                <div class="sub header"><fmt:message key="importDataFromFlugstatistikde" /></div>
            </div>
        </h2>

        <div class="ui steps">
            <div class="active step">
                <i class="file outline icon"></i>
                <div class="content">
                    <div class="title"><fmt:message key="selectSource" /></div>
                    <div class="description"><fmt:message key="enterSourceCredentials" /></div>
                </div>
            </div>
            <div class="disabled step">
                <i class="search icon"></i>
                <div class="content">
                    <div class="title"><fmt:message key="verify" /></div>
                    <div class="description"><fmt:message key="verifyImportedFlights" /></div>
                </div>
            </div>
            <div class="disabled step">
                <i class="checkmark icon"></i>
                <div class="content">
                    <div class="title"><fmt:message key="completed" /></div>
                </div>
            </div>
        </div>

        <spring:form modelAttribute="importEditor" servletRelativeAction="/import/flugstatistikde" cssClass="ui form">

            <div class="ui horizontal divider"><fmt:message key="sourceCredentials" /></div>
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

            <div class="ui horizontal divider"><fmt:message key="actions" /></div>
            <div class="sixteen wide">
                <button class="ui primary button">
                    <i class="cube icon"></i>
                    <fmt:message key="loadData" />
                </button>
            </div>

        </spring:form>

    </ft:body>
</ft:html>


