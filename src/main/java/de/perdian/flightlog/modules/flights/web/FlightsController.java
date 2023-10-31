package de.perdian.flightlog.modules.flights.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/flights")
class FlightsController {

    @GetMapping("/add")
    String doAddGet() {
        return "flights/add";
    }

    @PostMapping("/add/wizard")
    String doAddWizardPost() {
        return "flights/add";
    }

}
