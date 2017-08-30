<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ft" tagdir="/WEB-INF/tags/flighttracker" %>

<ft:html>
    <ft:head />
    <ft:body>

        <h2 class="ui header">
            <i class="cube icon"></i>
            <div class="content">
                <fmt:message key="import" />
                <div class="sub header"><fmt:message key="importCompleted" /></div>
            </div>
        </h2>

        <div class="ui steps">
            <div class="step">
                <i class="file outline icon"></i>
                <div class="content">
                    <div class="title"><fmt:message key="selectSource" /></div>
                </div>
            </div>
            <div class="step">
                <i class="search icon"></i>
                <div class="content">
                    <div class="title"><fmt:message key="verify" /></div>
                    <div class="description"><fmt:message key="verifyImportedFlights" /></div>
                </div>
            </div>
            <div class="active step">
                <i class="checkmark icon"></i>
                <div class="content">
                    <div class="title"><fmt:message key="completed" /></div>
                </div>
            </div>
        </div>


        <div class="ui divider"></div>
        <div><fmt:message key="importedFlights" />: <c:out value="${fn:length(importEditor.items)}" /></div>

        <div class="ui horizontal divider"><fmt:message key="actions" /></div>
        <a class="ui primary button" href="${contextPath}flights/list"><fmt:message key="listFlights" /></a>

    </ft:body>
</ft:html>


