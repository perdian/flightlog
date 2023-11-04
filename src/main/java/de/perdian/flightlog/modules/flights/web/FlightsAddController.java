package de.perdian.flightlog.modules.flights.web;

import de.perdian.flightlog.modules.flights.persistence.FlightEntity;
import de.perdian.flightlog.modules.flights.service.FlightLookupService;
import de.perdian.flightlog.modules.flights.service.model.FlightLookup;
import de.perdian.flightlog.modules.flights.service.model.FlightLookupRequest;
import de.perdian.flightlog.modules.flights.web.editor.FlightEditor;
import de.perdian.flightlog.support.web.MessageSeverity;
import de.perdian.flightlog.support.web.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/flights")
class FlightsAddController {

    private FlightLookupService flightLookupService = null;

    @RequestMapping("/add")
    String doAdd(@ModelAttribute("flightLookupRequest") FlightLookupRequest flightLookupRequest, @ModelAttribute("flightEditor") FlightEditor flightData) {
        return "flights/add";
    }

    @RequestMapping("/add/lookup")
    String doAddLookup(@ModelAttribute("flightLookupRequest") FlightLookupRequest flightLookupRequest, @ModelAttribute("flightEditor") FlightEditor flightEditor, Model model) {
        if (flightLookupRequest.isPopulated()) {
            List<FlightLookup> flightLookupResults = this.getFlightLookupService().lookupFlights(flightLookupRequest);
            model.addAttribute("flightLookupResults", flightLookupResults);
            if (flightLookupResults != null && flightLookupResults.size() == 1) {
                flightLookupResults.get(0).writeInto(flightEditor);
            }
        }
        return this.doAdd(flightLookupRequest, flightEditor);
    }

    @RequestMapping("/add/submit")
    String doAddSubmit(@ModelAttribute("flightLookupRequest") FlightLookupRequest flightLookupRequest, @ModelAttribute("flightEditor") FlightEditor flightEditor, BindingResult flightDataBindingResult, RedirectAttributes redirectAttributes, @ModelAttribute Messages messages) {
        if (flightDataBindingResult.hasErrors()) {
            messages.addMessage(MessageSeverity.ERROR, "Flight creation error", "Cannot create flight");
            return this.doAdd(flightLookupRequest, flightEditor);
        } else {

            FlightEntity flightEntity = new FlightEntity();

            redirectAttributes.addFlashAttribute("flightAdded", "true");
            redirectAttributes.addFlashAttribute("flightEntityId", flightEntity.getId());

            return "redirect:/flights/edit/" + flightEntity.getId();

        }
    }

    FlightLookupService getFlightLookupService() {
        return this.flightLookupService;
    }
    @Autowired
    void setFlightLookupService(FlightLookupService flightLookupService) {
        this.flightLookupService = flightLookupService;
    }

}
