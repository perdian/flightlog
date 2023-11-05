package de.perdian.flightlog.modules.flights.web;

import de.perdian.flightlog.modules.flights.persistence.FlightEntity;
import de.perdian.flightlog.modules.flights.service.FlightLookupService;
import de.perdian.flightlog.modules.flights.service.model.Flight;
import de.perdian.flightlog.modules.flights.service.model.FlightLookupRequest;
import de.perdian.flightlog.modules.flights.web.editor.FlightEditor;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = "/flights")
class FlightsAddController {

    private FlightLookupService flightLookupService = null;

    @RequestMapping(path = "/add")
    String doAdd(
        @ModelAttribute(name = "flightLookupRequest") FlightLookupRequest flightLookupRequest,
        @ModelAttribute(name = "flightEditor") FlightEditor flightData,
        @RequestParam(name = "showLookupForm", required = false) Boolean showLookupForm,
        Model model
    ) {
        if (showLookupForm != null) {
            model.addAttribute("showLookupForm", showLookupForm);
        }
        return "flights/add";
    }

    @RequestMapping(path = "/add/lookup")
    String doAddLookup(
        @ModelAttribute(name = "flightLookupRequest") FlightLookupRequest flightLookupRequest,
        @ModelAttribute(name = "flightEditor") FlightEditor flightEditor,
        Model model
    ) {
        if (flightLookupRequest.isPopulated()) {
            List<Flight> flightLookupResults = this.getFlightLookupService().lookupFlights(flightLookupRequest);
            model.addAttribute("flightLookupResults", flightLookupResults);
            if (flightLookupResults != null && flightLookupResults.size() == 1) {
                flightEditor.applyValuesFrom(flightLookupResults.get(0));
            } else if (flightLookupResults != null && flightLookupResults.size() > 1) {
                model.addAttribute("showLookupResults", true);
            }
            model.addAttribute("showLookupForm", false);
        }
        return this.doAdd(flightLookupRequest, flightEditor, null, model);
    }

    @RequestMapping(path = "/add/submit")
    String doAddSubmit(
        @ModelAttribute(name = "flightLookupRequest") FlightLookupRequest flightLookupRequest,
        @ModelAttribute(name = "flightEditor") @Valid FlightEditor flightEditor, BindingResult flightDataBindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (flightDataBindingResult.hasErrors()) {
            return this.doAdd(flightLookupRequest, flightEditor, false, model);
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
