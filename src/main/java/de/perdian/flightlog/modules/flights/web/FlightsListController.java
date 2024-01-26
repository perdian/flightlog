package de.perdian.flightlog.modules.flights.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/flights")
public class FlightsListController {

    @RequestMapping(path = "/list")
    String doList() {
        return "flights/list";
    }

}
