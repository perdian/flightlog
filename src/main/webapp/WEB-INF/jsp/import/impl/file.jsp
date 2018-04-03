<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fl" tagdir="/WEB-INF/tags/flightlog" %>

<fl:html>
    <fl:head />
    <fl:body>

        <h2 class="ui header">
            <i class="cube icon"></i>
            <div class="content">
                <fmt:message key="import" />
                <div class="sub header"><fmt:message key="importDataFromExternalFile" /></div>
            </div>
        </h2>

        <div class="ui steps">
            <div class="active step">
                <i class="file outline icon"></i>
                <div class="content">
                    <div class="title"><fmt:message key="selectSource" /></div>
                    <div class="description"><fmt:message key="selectSourceFile" /></div>
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

        <spring:form modelAttribute="importEditor" servletRelativeAction="/import/file" enctype="multipart/form-data" cssClass="ui form">

            <div class="ui horizontal divider"><fmt:message key="source" /></div>
            <div class="fields">
                <div class="six wide field">
                    <label><fmt:message key="sourceFile" /></label>
                    <input type="file" name="file" />
                </div>
                <div class="six wide field">
                    <label><fmt:message key="fileType" /></label>
                    <select name="fileType">
                        <c:forEach items="${fileTypes}" var="fileType">
                            <option ${param.fileType eq fileType.name() ? 'selected="selected"' : ''} value="${fileType.name()}"><fmt:message key="fileType.${fileType.name()}" /></option>
                        </c:forEach>
                    </select>
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

    </fl:body>
</fl:html>


