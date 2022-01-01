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
        <div class="ui divider"></div>

        <c:choose>
            <c:when test="${registrationRestrictedException ne null}">
                <div class="ui negative message">
                    <div class="header"><fmt:message key="loginNotSuccessful" /></div>
                    <p><fmt:message key="youDoNotHaveAnAccountOnThisSystemAndRegistrationIsRestricted" /></p>
                    <p><fmt:message key="ifYouThinkThisIsAMistakePleaseContactTheAdministrator" /></p>
                </div>
            </c:when>
            <c:when test="${authenticationException ne null}">
                <div class="ui negative message">
                    <div class="header"><fmt:message key="loginNotSuccessful" /></div>
                    <p><fmt:message key="theCredentialsYouEnteredAreInvalidTheLoginWasNotSuccessful" /></p>
                </div>
            </c:when>
        </c:choose>

        <div class="ui cards">
            <form class="ui card form" action="${contextPath}login" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <div class="content">
                    <div class="header"><fmt:message key="flightLogLogin" /></div>
                    <div class="meta"><span class="category"><fmt:message key="loginWithYourFlightLogCredentials" /></span></div>
                    <div class="description">
                        <div class="field">
                            <label><fmt:message key="username" /></label>
                            <input name="username" value="<c:out value="${param.username}" />" />
                        </div>
                        <div class="field">
                            <label><fmt:message key="password" /></label>
                            <input name="password" type="password" value="<c:out value="${param.password}" />" />
                        </div>
                    </div>
                </div>
                <div class="extra content">
                    <div class="ui two buttons">
                    <input class="ui primary button" type="submit" value="<fmt:message key="login" />" />
                    </div>
                </div>
            </form>
            <form class="ui card form" action="${contextPath}oauth2/authorization/google" method="post" >
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <div class="content">
                    <div class="header"><fmt:message key="googleLogin" /></div>
                    <div class="meta"><span class="category"><fmt:message key="loginWithYourGoogleCredentials" /></span></div>
                    <div class="description"><fmt:message key="youWillBeForwardedToGoogleForAuthentication" /></div>
                </div>
                <div class="extra content">
                    <div class="ui two buttons">
                    <input class="ui primary button" type="submit" value="<fmt:message key="login" />" />
                    </div>
                </div>
            </form>
        </div>

    </fl:body>
</fl:html>
