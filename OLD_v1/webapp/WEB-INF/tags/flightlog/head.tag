<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>

    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <title><fmt:message key="flightLog.pageTitle" /></title>

    <script src="${contextPath}webjars/jquery/jquery.min.js"></script>
    <script src="${contextPath}webjars/momentjs/min/moment.min.js"></script>

    <link rel="stylesheet" href="${contextPath}resources/fomanticui/semantic.min.css" />
    <script src="${contextPath}resources/fomanticui/semantic.min.js"></script>
    <script src="${contextPath}resources/fomanticui/components/popup.min.js"></script>

    <link rel="stylesheet" href="${contextPath}resources/flightlog/css/flightlog.css" />
    <link rel="icon" href="${contextPath}resources/flightlog/icons/plane.png" />

    <jsp:doBody />

</head>
