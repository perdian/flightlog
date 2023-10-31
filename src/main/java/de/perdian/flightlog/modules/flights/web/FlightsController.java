package de.perdian.flightlog.modules.flights.web;

import de.perdian.flightlog.modules.flights.model.FlightData;
import de.perdian.flightlog.modules.flights.model.WizardData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/flights")
class FlightsController {

    @GetMapping("/add")
    String doAddGet(@ModelAttribute WizardData wizardData, @ModelAttribute FlightData flightData) {
        return "flights/add";
    }

    @PostMapping("/add/wizard")
    String doAddWizardPost(@ModelAttribute WizardData wizardData, @ModelAttribute FlightData flightData) {
        return "flights/add";
    }

}
