<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fl" tagdir="/WEB-INF/tags/flightlog" %>

<fl:html>
    <fl:head />
    <fl:body>

        <h2 class="ui header">
            <i class="user icon"></i>
            <div class="content">
                <fmt:message key="logout" />
                <div class="sub header"><fmt:message key="youHaveBeenLoggedOut" /></div>
            </div>
        </h2>

        <div class="ui divider"></div>

        <p>
            <fmt:message key="youHaveBeenLoggedOut" />
        </p>

    </fl:body>
</fl:html>
