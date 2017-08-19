<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>

    <fmt:setBundle basename="de.perdian.apps.flighttracker.i18n.messages" scope="request" />
    <c:url var="contextPath" scope="request" value="" />

    <jsp:doBody />

</html>