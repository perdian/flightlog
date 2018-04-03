<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fl" tagdir="/WEB-INF/tags/flightlog" %>

<fl:html>
    <fl:head />
    <fl:body>

        <div class="ui header">
            <i class="plane icon"></i>
            <div class="content">
                <fmt:message key="listAirlines" />
                <div class="sub header"><fmt:message key="allUserSpecificAirlines" /></div>
            </div>
        </div>

        <div class="ui divider"></div>

        <spring:form servletRelativeAction="/airlines/list" modelAttribute="airlines" cssClass="ui form">

            <table class="ui celled striped compact table">
                <thead>
                    <tr>
                        <th width="125"><fmt:message key="code" /></th>
                        <th width="125"><fmt:message key="countryCode" /></th>
                        <th><fmt:message key="name" /></th>
                        <th width="150"><fmt:message key="action" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty airlines.items}">
                            <tr>
                                <td colspan="4"><i><fmt:message key="noUserSpecificAirlinesDefinedYet" /></i></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${airlines.items}" var="airline" varStatus="airlineStatus">
                                <tr>
                                    <td>
                                        <spring:input disabled="true" cssClass="field" path="items[${airlineStatus.index}].airlineBean.code" />
                                        <spring:hidden path="items[${airlineStatus.index}].airlineBean.code" />
                                    </td>
                                    <td>
                                        <fl:inputfield cssClass="field" bean="airlines" path="items[${airlineStatus.index}].airlineBean.countryCode" />
                                    </td>
                                    <td>
                                        <fl:inputfield cssClass="field" bean="airlines" path="items[${airlineStatus.index}].airlineBean.name" />
                                    </td>
                                    <td>
                                        <div class="ui checkbox">
                                            <spring:checkbox path="items[${airlineStatus.index}].delete" />
                                            <label><fmt:message key="delete" /></label>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>

            <div class="ui horizontal divider"><fmt:message key="createNewUserSpecificAirline" /></div>

            <table class="ui celled striped compact table">
                <tbody>
                    <tr>
                        <td width="125">
                            <fl:inputfield cssClass="field" bean="airlines" path="newItem.airlineBean.code" labelKey="code" />
                        </td>
                        <td width="125">
                            <fl:inputfield cssClass="field" bean="airlines" path="newItem.airlineBean.countryCode" labelKey="countryCode" />
                        </td>
                        <td>
                            <fl:inputfield cssClass="field" bean="airlines" path="newItem.airlineBean.name" labelKey="name" />
                        </td>
                    </tr>
                </tbody>
            </table>

            <div class="ui horizontal divider"><fmt:message key="actions" /></div>
            <div class="sixteen wide">
                <button class="ui primary button">
                    <i class="save icon"></i>
                    <fmt:message key="save" />
                </button>
            </div>

        </spring:form>

    </fl:body>
</fl:html>
