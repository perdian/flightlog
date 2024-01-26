package de.perdian.flightlog.modules.flights.web;

import de.perdian.flightlog.modules.authentication.UserHolder;
import de.perdian.flightlog.modules.flights.service.FlightLookupService;
import de.perdian.flightlog.modules.flights.service.FlightUpdateService;
import de.perdian.flightlog.modules.flights.service.model.Flight;
import de.perdian.flightlog.modules.flights.service.model.FlightLookupRequest;
import de.perdian.flightlog.modules.flights.web.editor.FlightEditor;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
class FlightsEditController {

    private static final Logger log = LoggerFactory.getLogger(FlightsEditController.class);

    private FlightLookupService flightLookupService = null;
    private FlightUpdateService flightUpdateService = null;
    private UserHolder userHolder = null;

    @RequestMapping(path = "/add")
    String doAdd(
        @ModelAttribute(name = "flightLookupRequest") FlightLookupRequest flightLookupRequest,
        @ModelAttribute(name = "flightEditor") FlightEditor flightEditor,
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
        @ModelAttribute(name = "flightEditor") @Valid FlightEditor flightEditor, BindingResult flightEditorBindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (flightEditorBindingResult.hasErrors()) {
            return this.doAdd(flightLookupRequest, flightEditor, false, model);
        } else {

            Flight newFlight = new Flight();
            flightEditor.copyValuesInto(newFlight);
            log.debug("Adding new flight: {}", newFlight);
            Flight storedFlight = this.getFlightUpdateService().saveFlight(newFlight, this.getUserHolder().getCurrentUser());

            redirectAttributes.addFlashAttribute("flightAdded", "true");
            redirectAttributes.addFlashAttribute("flightEntityId", storedFlight.getEntityId());

            return "redirect:/flights/edit/" + storedFlight.getEntityId();

        }
    }

    FlightLookupService getFlightLookupService() {
        return this.flightLookupService;
    }
    @Autowired
    void setFlightLookupService(FlightLookupService flightLookupService) {
        this.flightLookupService = flightLookupService;
    }

    FlightUpdateService getFlightUpdateService() {
        return this.flightUpdateService;
    }
    @Autowired
    void setFlightUpdateService(FlightUpdateService flightUpdateService) {
        this.flightUpdateService = flightUpdateService;
    }

    UserHolder getUserHolder() {
        return this.userHolder;
    }
    @Autowired
    void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

}
