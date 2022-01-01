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
                <fmt:message key="listFlights" />
                <div class="sub header"><fmt:message key="allAvailableFlights" /></div>
            </div>
        </div>

        <div class="ui divider"></div>

        <c:if test="${flights.pagination.totalPages gt 1}">
            <div class="ui centered grid">
                <div class="center aligned column">
                    <div class="ui pagination menu">
                        <div class="item"><fmt:message key="page" /></div>
                        <c:forEach begin="0" end="${flights.pagination.totalPages - 1}" varStatus="pageStatus">
                            <a class="${pageStatus.index eq flights.pagination.page ? 'active ' : ''}item" href="<c:url value="/flights/list/${pageStatus.index}" />">
                                <c:out value="${pageStatus.index + 1}" />
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:if>

        <jsp:include page="list-embedded.jsp" />

        <c:if test="${flights.pagination.totalPages gt 1}">
            <div class="ui centered grid">
                <div class="center aligned column">
                    <div class="ui pagination menu">
                        <div class="item"><fmt:message key="page" /></div>
                        <c:forEach begin="0" end="${flights.pagination.totalPages - 1}" varStatus="pageStatus">
                            <a class="${pageStatus.index eq flights.pagination.page ? 'active ' : ''}item" href="<c:url value="/flights/list/${pageStatus.index}" />">
                                <c:out value="${pageStatus.index + 1}" />
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:if>

    </fl:body>
</fl:html>
