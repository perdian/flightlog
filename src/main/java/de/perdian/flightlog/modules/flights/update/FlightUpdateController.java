package de.perdian.flightlog.modules.flights.update;

import de.perdian.flightlog.modules.authentication.UserHolder;
import de.perdian.flightlog.modules.flights.lookup.FlightLookupRequest;
import de.perdian.flightlog.modules.flights.lookup.FlightLookupService;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.service.FlightQuery;
import de.perdian.flightlog.modules.flights.shared.service.FlightQueryService;
import de.perdian.flightlog.support.pagination.PaginatedList;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(path = "/flights")
class FlightUpdateController {

    private static final Logger log = LoggerFactory.getLogger(FlightUpdateController.class);

    private FlightQueryService flightQueryService = null;
    private FlightUpdateService flightUpdateService = null;
    private FlightLookupService flightLookupService = null;
    private UserHolder userHolder = null;

    @GetMapping(path = "/add")
    String doAddGet(
        @ModelAttribute(name = "flightLookupRequest") FlightLookupRequest flightLookupRequest,
        @ModelAttribute(name = "flightUpdateEditor") FlightUpdateEditor flightUpdateEditor,
        @RequestParam(name = "showLookupForm", required = false) Boolean showLookupForm,
        Model model
    ) {
        if (showLookupForm != null) {
            model.addAttribute("showLookupForm", showLookupForm);
        }
        return "flights/add";
    }

    @RequestMapping(path = "/add")
    String doAddPost(
        @ModelAttribute(name = "flightLookupRequest") FlightLookupRequest flightLookupRequest,
        @ModelAttribute(name = "flightUpdateEditor") @Valid FlightUpdateEditor flightUpdateEditor, BindingResult flightUpdateEditorBindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (flightUpdateEditorBindingResult.hasErrors()) {
            return this.doAddGet(flightLookupRequest, flightUpdateEditor, false, model);
        } else {

            Flight newFlight = new Flight();
            flightUpdateEditor.copyValuesInto(newFlight);
            log.debug("Adding new flight: {}", newFlight);
            Flight storedFlight = this.getFlightUpdateService().saveFlight(newFlight, this.getUserHolder().getCurrentUser());

            redirectAttributes.addFlashAttribute("flightAdded", "true");
            redirectAttributes.addFlashAttribute("flightEntityId", storedFlight.getEntityId());

            return "redirect:/flights/edit/" + storedFlight.getEntityId();

        }
    }

    @RequestMapping(path = "/add/lookup")
    String doAddLookup(
        @ModelAttribute(name = "flightLookupRequest") FlightLookupRequest flightLookupRequest,
        @ModelAttribute(name = "flightUpdateEditor") FlightUpdateEditor flightUpdateEditor,
        Model model
    ) {
        if (flightLookupRequest.isPopulated()) {
            List<Flight> flightLookupResults = this.getFlightLookupService().lookupFlights(flightLookupRequest);
            model.addAttribute("flightLookupResults", flightLookupResults);
            if (flightLookupResults != null && flightLookupResults.size() == 1) {
                flightUpdateEditor.applyValuesFrom(flightLookupResults.get(0));
            } else if (flightLookupResults != null && flightLookupResults.size() > 1) {
                model.addAttribute("showLookupResults", true);
            }
            model.addAttribute("showLookupForm", false);
        }
        return this.doAddGet(flightLookupRequest, flightUpdateEditor, null, model);
    }

    @GetMapping(path = "/edit/{flightEntityId}")
    String doEditGet(
        @ModelAttribute(name = "flightUpdateEditor") FlightUpdateEditor flightUpdateEditor,
        @PathVariable(name = "flightEntityId") UUID flightEntityId,
        Model model
    ) {
        FlightQuery flightQuery = new FlightQuery(this.getUserHolder().getCurrentUser());
        flightQuery.setRestrictEntityIdentifiers(Collections.singleton(flightEntityId));
        PaginatedList<Flight> flightList = this.getFlightQueryService().loadFlights(flightQuery, null);
        Flight flight = flightList.getItem(0).orElse(null);
        if (flight == null) {
            return "/flights/not-found";
        } else {
            flightUpdateEditor.applyValuesFrom(flight);
            return "/flights/edit";
        }
    }

    @PostMapping(path = "/edit/{flightEntityId}")
    String doEditPost(
        @ModelAttribute(name = "flightUpdateEditor") @Valid FlightUpdateEditor flightUpdateEditor, BindingResult flightUpdateEditorBindingResult,
        @PathVariable(name = "flightEntityId") UUID flightEntityId,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        FlightQuery flightQuery = new FlightQuery(this.getUserHolder().getCurrentUser());
        flightQuery.setRestrictEntityIdentifiers(Collections.singleton(flightEntityId));
        PaginatedList<Flight> flightList = this.getFlightQueryService().loadFlights(flightQuery, null);
        Flight flight = flightList.getItem(0).orElse(null);
        if (flight == null) {
            return "/flights/not-found";
        } else {
            if (flightUpdateEditorBindingResult.hasErrors()) {
                return "/flights/edit";
            } else {
                Flight updatedFlight = this.getFlightUpdateService().saveFlight(flight, this.getUserHolder().getCurrentUser());
                flightUpdateEditor.applyValuesFrom(updatedFlight);
                redirectAttributes.addFlashAttribute("flightUpdated", "true");
                return "redirect:/flights/edit/" + updatedFlight.getEntityId();
            }
        }
    }

    FlightQueryService getFlightQueryService() {
        return this.flightQueryService;
    }
    @Autowired
    void setFlightQueryService(FlightQueryService flightQueryService) {
        this.flightQueryService = flightQueryService;
    }

    FlightUpdateService getFlightUpdateService() {
        return this.flightUpdateService;
    }
    @Autowired
    void setFlightUpdateService(FlightUpdateService flightUpdateService) {
        this.flightUpdateService = flightUpdateService;
    }

    FlightLookupService getFlightLookupService() {
        return this.flightLookupService;
    }
    @Autowired
    void setFlightLookupService(FlightLookupService flightLookupService) {
        this.flightLookupService = flightLookupService;
    }

    UserHolder getUserHolder() {
        return this.userHolder;
    }
    @Autowired
    void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

}
