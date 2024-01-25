function registerAirportCodeChangeListener(airportCodeField, airportNameField, countryCodeIcon) {
  airportCodeField.on("change", () => onAirportCodeChange(airportCodeField, airportNameField, countryCodeIcon));
}

async function onAirportCodeChange(airportCodeField, airportNameField, countryCodeIcon) {
  if (airportCodeField.val() == null || airportCodeField.val().length < 1) {
    airportCodeField.parent(".field").removeClass("error");
    airportNameField.val("");
    countryCodeIcon.attr("class", "");
  } else if (airportCodeField.val().length != 3) {
    airportCodeField.parent(".field").addClass("error");
    airportNameField.val("Invalid airport code");
    countryCodeIcon.attr("class", "");
  } else {
    airportNameField.val("Checking airport name...");
    countryCodeIcon.attr("class", "");
    try {
      let airportResponse = await fetch($("#flightlogRootUrl").val() + "airport/" + airportCodeField.val());
      if (airportResponse.status != 200) {
        airportCodeField.parent(".field").addClass("error");
        airportNameField.val("Airport unknown");
      } else {
        let airportBody = await airportResponse.json();
        airportNameField.val(airportBody.name);
        countryCodeIcon.addClass("flag").addClass(airportBody.countryCode.toLowerCase());
        airportCodeField.parent(".field").removeClass("error");
      }
    } catch (e) {
      airportCodeField.parent(".field").addClass("error");
      airportNameField.val("Invalid airport code");
    }
  }
}

function registerAirlineCodeChangeListener(airlineCodeField, airlineNameField) {
  airlineCodeField.on("change", () => handleAirlineCodeChange(airlineCodeField, airlineNameField));
}

async function handleAirlineCodeChange(airlineCodeField, airlineNameField) {
  if (airlineCodeField.val() == null || airlineCodeField.val().length < 1) {
    airlineNameField.val("");
  } else {
    try {
      let airlineResponse = await fetch($("#flightlogRootUrl").val() + "airline/" + airlineCodeField.val());
      if (airlineResponse.status = 200) {
        let airlineBody = await airlineResponse.json();
        airlineNameField.val(airlineBody.name);
      }
    } catch (e) {
      console.warn("Cannot load airline for code: " + airlineCodeField.val(), e);
      airlineNameField.val("");
    }
  }
}

function registerRouteChangeListener(departureAirportCodeField, departureDateLocalField, departureTimeLocalField, arrivalAirportCodeField, arrivalDateLocalField, arrivalTimeLocalField, flightDurationField, flightDistanceField) {
  for (let field of [departureAirportCodeField, departureDateLocalField, departureTimeLocalField, arrivalAirportCodeField, arrivalDateLocalField, arrivalTimeLocalField]) {
    field.on("change", () => handleRouteChange(departureAirportCodeField, departureDateLocalField, departureTimeLocalField, arrivalAirportCodeField, arrivalDateLocalField, arrivalTimeLocalField, flightDurationField, flightDistanceField));
  }
}

async function handleRouteChange(departureAirportCodeField, departureDateLocalField, departureTimeLocalField, arrivalAirportCodeField, arrivalDateLocalField, arrivalTimeLocalField, flightDurationField, flightDistanceField) {
  let departureValid = departureAirportCodeField.val() != null && departureAirportCodeField.val().length >= 3;
  let arrivalValid = arrivalAirportCodeField.val() != null && arrivalAirportCodeField.val().length >= 3;
  if (departureValid && arrivalValid) {
    try {

      let routeRequestFormData = new FormData();
      routeRequestFormData.append("departureAirportCode", departureAirportCodeField.val());
      routeRequestFormData.append("departureDateLocal", departureDateLocalField.val());
      routeRequestFormData.append("departureTimeLocal", departureTimeLocalField.val());
      routeRequestFormData.append("arrivalAirportCode", arrivalAirportCodeField.val());
      routeRequestFormData.append("arrivalDateLocal", arrivalDateLocalField.val());
      routeRequestFormData.append("arrivalTimeLocal", arrivalTimeLocalField.val());

      let routeResponse = await fetch($("#flightlogRootUrl").val() + "flights/route", {
        method: "POST",
        body: routeRequestFormData
      });
      let routeBody = await routeResponse.json();
      if (routeBody.durationString != null) {
        flightDurationField.val(routeBody.durationString);
      }
      if (routeBody.distance != null) {
        flightDistanceField.val(routeBody.distance);
      }

      console.info(routeBody);
    } catch (e) {
      console.warn("Cannot compute route (" + departureAirportCodeField.val() + " to " + arrivalAirportCodeField.val() + ")", e);
    }
  }
}
