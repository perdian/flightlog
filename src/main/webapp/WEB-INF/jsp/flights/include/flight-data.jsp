<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ft" tagdir="/WEB-INF/tags/flighttracker" %>

<h3 class="ui dividing header"><fmt:message key="departure" /> / <fmt:message key="arrival" /></h3>
<div class="fields">
    <ft:inputfield cssClass="two wide field" bean="flightEditor" path="departureAirportCode" labelKey="departureAirportCode" />
    <ft:inputfield cssClass="two wide field" bean="flightEditor" path="departureDateLocal" labelKey="departureDate" placeholder="yyyy-MM-dd" />
    <ft:inputfield cssClass="two wide field" bean="flightEditor" path="departureTimeLocal" labelKey="departureTime" placeholder="HH:mm" />
    <div class="ten wide field">
        <label><fmt:message key="departureAirport" /></label>
        <input name="departureAirportName" id="departureAirportName" tabIndex="-1" readonly="readonly" placeholder="<fmt:message key="airportNameWillBeComputedAutomatically" />" value="<c:out value="${flightEditor.departureAirportName}" />" />
        <script type="text/javascript">
            $("#departureAirportCode").change(function() {
                var airportCode = $(this).val().toUpperCase();
                $(this).val(airportCode);
                $("#departureAirportName").val(null);
                $.ajax({
                    url: "<c:url value="/airport/" />" + airportCode,
                }).done(function(data) {
                    $("#departureAirportName").val(data.name);
                }).fail(function() {
                    $("#departureAirportName").val("<fmt:message key="airportNameCannotBeComputedAutomatically" />");
                });
            });
        </script>
    </div>
</div>
<div class="fields">
    <ft:inputfield cssClass="two wide field" bean="flightEditor" path="arrivalAirportCode" labelKey="arrivalAirportCode" />
    <ft:inputfield cssClass="two wide field" bean="flightEditor" path="arrivalDateLocal" labelKey="arrivalDate" placeholder="yyyy-MM-dd" />
    <ft:inputfield cssClass="two wide field" bean="flightEditor" path="arrivalTimeLocal" labelKey="arrivalTime" placeholder="HH:mm" />
    <div class="ten wide field">
        <label><fmt:message key="arrivalAirport" /></label>
        <input name="arrivalAirportName" id="arrivalAirportName" tabIndex="-1" readonly="readonly" placeholder="<fmt:message key="airportNameWillBeComputedAutomatically" />" value="<c:out value="${flightEditor.arrivalAirportName}" />" />
        <script type="text/javascript">
            $("#arrivalAirportCode").change(function() {
                var airportCode = $(this).val().toUpperCase();
                $(this).val(airportCode);
                $("#arrivalAirportName").val(null);
                $.ajax({
                    url: "<c:url value="/airport/" />" + airportCode,
                }).done(function(data) {
                    $("#arrivalAirportName").val(data.name);
                }).fail(function() {
                    $("#arrivalAirportName").val("<fmt:message key="airportNameCannotBeComputedAutomatically" />");
                });
            });
        </script>
    </div>
</div>
<div class="fields">

    <div class="two wide field">
        <label><fmt:message key="distance" /></label>
        <div class="ui right labeled input">
            <spring:input path="flightDistance" placeholder="..." />
            <div class="ui basic label">
                <fmt:message key="km" />
            </div>
        </div>
    </div>
    <script type="text/javascript">

        function recomputeDistance() {

            $("#flightDistance").val("");

            var departureAirportCode = $("#departureAirportCode").val();
            var arrivalAirportCode = $("#arrivalAirportCode").val();

            if (departureAirportCode.length > 0 && arrivalAirportCode.length > 0) {
                $.ajax({
                    url: "<c:url value="/flights/computedistance" />",
                    data: {
                        departureAirportCode: departureAirportCode,
                        arrivalAirportCode: arrivalAirportCode
                    }
                }).done(function(data) {
                    if (data != null && data > 0) {
                        $("#flightDistance").val(data);
                    }
                });
            }

        }

        $("#departureAirportCode").change(recomputeDistance);
        $("#arrivalAirportCode").change(recomputeDistance);

    </script>

    <div class="two wide field">
        <label><fmt:message key="duration" /></label>
        <div class="ui right labeled input">
            <spring:input path="flightDuration" placeholder="..." />
            <div class="ui basic label">
                <fmt:message key="hoursUnitSign" />
            </div>
        </div>
    </div>
    <script type="text/javascript">

        function recomputeDuration() {

            $("#flightDuration").val("");

            var departureAirportCode = $("#departureAirportCode").val();
            var departureDate = $("#departureDateLocal").val();
            var departureTime = $("#departureTimeLocal").val();
            var arrivalAirportCode = $("#arrivalAirportCode").val();
            var arrivalDate = $("#arrivalDateLocal").val();
            var arrivalTime = $("#arrivalTimeLocal").val();
            if (departureAirportCode.length > 0 && departureDate.length > 0 && departureTime.length > 0 && arrivalAirportCode.length > 0 && arrivalDate.length > 0 && arrivalTime.length > 0) {
                $.ajax({
                    url: "<c:url value="/flights/computeduration" />",
                    data: {
                        departureAirportCode: departureAirportCode,
                        departureDate: departureDate,
                        departureTime: departureTime,
                        arrivalAirportCode: arrivalAirportCode,
                        arrivalDate: arrivalDate,
                        arrivalTime: arrivalTime,
                    }
                }).done(function(data) {
                    if (data != null && data.length > 0) {
                        $("#flightDuration").val(data);
                    }
                });
            }

        }

        $("#arrivalAirportCode").change(recomputeDuration);
        $("#arrivalDateLocal").change(recomputeDuration);
        $("#arrivalTimeLocal").change(recomputeDuration);
        $("#departureAirportCode").change(recomputeDuration);
        $("#departureDateLocal").change(recomputeDuration);
        $("#departureTimeLocal").change(recomputeDuration);

    </script>

</div>

<h3 class="ui dividing header"><fmt:message key="otherData" /></h3>
<div class="fields">

    <ft:inputfield cssClass="two wide field" bean="flightEditor" path="airlineCode" labelKey="airlineCode" />
    <ft:inputfield cssClass="two wide field" bean="flightEditor" path="flightNumber" labelKey="flightNumber" />
    <ft:inputfield cssClass="three wide field" bean="flightEditor" path="airlineName" labelKey="airlineName" />
    <ft:inputfield cssClass="three wide field" bean="flightEditor" path="aircraftType" labelKey="aircraftType" />
    <ft:inputfield cssClass="three wide field" bean="flightEditor" path="aircraftRegistration" labelKey="aircraftRegistration" />
    <ft:inputfield cssClass="three wide field" bean="flightEditor" path="aircraftName" labelKey="aircraftName" />

    <script type="text/javascript">
        $("#airlineCode").change(function() {
            var airlineCode = $(this).val().toUpperCase();
            $(this).val(airlineCode);
            $("#airlineName").val(null);
            $.ajax({
                url: "<c:url value="/airline/" />" + airlineCode,
            }).done(function(data) {
                $("#airlineName").val(data.name);
            });
        });
    </script>

</div>

<div class="fields">
    <ft:inputfield cssClass="two wide field" bean="flightEditor" path="seatNumber" labelKey="seat" />
    <ft:select cssClass="two wide field" bean="flightEditor" path="seatType" labelKey="seatType">
        <option></option>
        <c:forEach items="${flightEditorHelper.seatTypeValues}" var="seatTypeValue">
            <option ${flightEditor.seatType eq seatTypeValue ? 'selected=\"selected\"' : ''} value="${seatTypeValue}"><fmt:message key="seatType.${seatTypeValue}" /></option>
        </c:forEach>
    </ft:select>
    <ft:select cssClass="two wide field" bean="flightEditor" path="cabinClass" labelKey="cabinClass">
        <option></option>
        <c:forEach items="${flightEditorHelper.cabinClassValues}" var="cabinClassValue">
            <option ${flightEditor.cabinClass eq cabinClassValue ? 'selected=\"selected\"' : ''} value="${cabinClassValue}"><fmt:message key="cabinClass.${cabinClassValue}" /></option>
        </c:forEach>
    </ft:select>
    <ft:select cssClass="two wide field" bean="flightEditor" path="flightReason" labelKey="flightReason">
        <option></option>
        <c:forEach items="${flightEditorHelper.flightReasonValues}" var="flightReasonValue">
            <option ${flightEditor.flightReason eq flightReasonValue ? 'selected=\"selected\"' : ''} value="${flightReasonValue}"><fmt:message key="flightReason.${flightReasonValue}" /></option>
        </c:forEach>
    </ft:select>
    <ft:inputfield cssClass="eight wide field" bean="flightEditor" path="comment" labelKey="comment" />
</div>