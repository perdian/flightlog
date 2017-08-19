<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ft" tagdir="/WEB-INF/tags/flighttracker" %>

<ft:html>
    <ft:head />
    <ft:body>

        <h1 class="ui header"><fmt:message key="importFile" /></h1>

        <spring:form modelAttribute="importEditor" servletRelativeAction="/import/file" enctype="multipart/form-data" cssClass="ui form">

            <h3 class="ui dividing header"><fmt:message key="source" /></h3>
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

            <h3 class="ui dividing header"><fmt:message key="actions" /></h3>
            <div class="sixteen wide">
                <button class="ui primary button"><fmt:message key="importFile" /></button>
            </div>

        </spring:form>

    </ft:body>
</ft:html>


