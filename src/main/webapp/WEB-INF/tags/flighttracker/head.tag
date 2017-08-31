<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>

    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <title><fmt:message key="flightTracker.pageTitle" /></title>

    <link rel="stylesheet" href="${contextPath}resources/semantic/semantic.min.css" />
    <link rel="stylesheet" href="${contextPath}resources/flighttracker/css/flighttracker.css" />

    <script src="${contextPath}webjars/jquery/3.2.1/jquery.min.js"></script>
    <script src="${contextPath}resources/semantic/semantic.min.js"></script>
    <script src="${contextPath}resources/semantic/components/popup.min.js"></script>

    <jsp:doBody />

</head>