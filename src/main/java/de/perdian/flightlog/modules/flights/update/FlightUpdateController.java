package de.perdian.flightlog.modules.flights.update;

import de.perdian.flightlog.modules.authentication.service.userdetails.FlightlogUserDetailsHolder;
import de.perdian.flightlog.modules.flights.lookup.FlightLookupRequest;
import de.perdian.flightlog.modules.flights.lookup.FlightLookupService;
import de.perdian.flightlog.modules.flights.shared.model.Flight;
import de.perdian.flightlog.modules.flights.shared.service.FlightQuery;
import de.perdian.flightlog.modules.flights.shared.service.FlightQueryService;
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
    private FlightlogUserDetailsHolder flightlogUserDetailsHolder = null;

    @GetMapping(path = "/add")
    String doAddGet(
        @ModelAttribute FlightLookupRequest flightLookupRequest,
        @ModelAttribute FlightUpdateEditor flightUpdateEditor,
        @RequestParam(required = false) Boolean showLookupForm,
        Model model
    ) {
        if (showLookupForm != null) {
            model.addAttribute("showLookupForm", showLookupForm);
        }
        return "flights/add";
    }

    @RequestMapping(path = "/add")
    String doAddPost(
        @ModelAttribute FlightLookupRequest flightLookupRequest,
        @ModelAttribute @Valid FlightUpdateEditor flightUpdateEditor, BindingResult flightUpdateEditorBindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (flightUpdateEditorBindingResult.hasErrors()) {
            return this.doAddGet(flightLookupRequest, flightUpdateEditor, false, model);
        } else {

            Flight newFlight = new Flight();
            flightUpdateEditor.copyValuesInto(newFlight);
            log.debug("Adding new flight: {}", newFlight);
            Flight storedFlight = this.getFlightUpdateService().saveFlight(newFlight, this.getFlightlogUserDetailsHolder().getCurrentUserDetails());

            redirectAttributes.addFlashAttribute("flightAdded", "true");
            redirectAttributes.addFlashAttribute("flightEntityId", storedFlight.getEntityId());

            return "redirect:/flights/edit/" + storedFlight.getEntityId();

        }
    }

    @RequestMapping(path = "/add/lookup")
    String doAddLookup(
        @ModelAttribute FlightLookupRequest flightLookupRequest,
        @ModelAttribute FlightUpdateEditor flightUpdateEditor,
        Model model
    ) {
        if (flightLookupRequest.isPopulated()) {
            List<Flight> flightLookupResults = this.getFlightLookupService().lookupFlights(flightLookupRequest);
            model.addAttribute("flightLookupResults", flightLookupResults);
            if (flightLookupResults != null && flightLookupResults.size() == 1) {
                flightUpdateEditor.applyValuesFrom(flightLookupResults.getFirst());
            } else if (flightLookupResults != null && flightLookupResults.size() > 1) {
                model.addAttribute("showLookupResults", true);
            }
            model.addAttribute("showLookupForm", false);
        }
        return this.doAddGet(flightLookupRequest, flightUpdateEditor, null, model);
    }

    @GetMapping(path = "/edit/{flightEntityId}")
    String doEditGet(
        @ModelAttribute FlightUpdateEditor flightUpdateEditor,
        @PathVariable UUID flightEntityId,
        Model model
    ) {
        FlightQuery flightQuery = new FlightQuery().withUserDetails(this.getFlightlogUserDetailsHolder().getCurrentUserDetails());
        flightQuery.setRestrictEntityIdentifiers(Collections.singleton(flightEntityId));
        List<Flight> flightList = this.getFlightQueryService().loadFlights(flightQuery);
        Flight flight = flightList == null || flightList.isEmpty() ? null : flightList.getFirst();
        if (flight == null) {
            return "flights/not-found";
        } else {
            flightUpdateEditor.applyValuesFrom(flight);
            return "flights/edit";
        }
    }

    @PostMapping(path = "/edit/{flightEntityId}")
    String doEditPost(
        @ModelAttribute @Valid FlightUpdateEditor flightUpdateEditor, BindingResult flightUpdateEditorBindingResult,
        @PathVariable UUID flightEntityId,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        FlightQuery flightQuery = new FlightQuery().withUserDetails(this.getFlightlogUserDetailsHolder().getCurrentUserDetails());
        flightQuery.setRestrictEntityIdentifiers(Collections.singleton(flightEntityId));
        List<Flight> flightList = this.getFlightQueryService().loadFlights(flightQuery);
        Flight flight = flightList == null || flightList.isEmpty() ? null : flightList.getFirst();
        if (flight == null) {
            return "flights/not-found";
        } else {
            if (flightUpdateEditorBindingResult.hasErrors()) {
                return "flights/edit";
            } else {

                flightUpdateEditor.copyValuesInto(flight);

                Flight updatedFlight = this.getFlightUpdateService().saveFlight(flight, this.getFlightlogUserDetailsHolder().getCurrentUserDetails());
                flightUpdateEditor.applyValuesFrom(updatedFlight);

                redirectAttributes.addFlashAttribute("flightUpdated", "true");
                return "redirect:/flights/edit/" + updatedFlight.getEntityId();

            }
        }
    }

    @GetMapping(path = "/delete/{flightEntityId}")
    String doDeleteGet(@PathVariable UUID flightEntityId, Model model) {
        FlightQuery flightQuery = new FlightQuery().withUserDetails(this.getFlightlogUserDetailsHolder().getCurrentUserDetails());
        flightQuery.setRestrictEntityIdentifiers(Collections.singleton(flightEntityId));
        List<Flight> flightList = this.getFlightQueryService().loadFlights(flightQuery);
        Flight flight = flightList == null || flightList.isEmpty() ? null : flightList.getFirst();
        if (flight == null) {
            return "flights/not-found";
        } else {
            model.addAttribute("flight", flight);
            return "flights/delete";
        }
    }

    @PostMapping(path = "/delete/{flightEntityId}")
    String doDeletePost(@PathVariable UUID flightEntityId, Model model, RedirectAttributes redirectAttributes) {
        FlightQuery flightQuery = new FlightQuery().withUserDetails(this.getFlightlogUserDetailsHolder().getCurrentUserDetails());
        flightQuery.setRestrictEntityIdentifiers(Collections.singleton(flightEntityId));
        List<Flight> flightList = this.getFlightQueryService().loadFlights(flightQuery);
        Flight flight = flightList == null || flightList.isEmpty() ? null : flightList.getFirst();
        if (flight == null) {
            return "flights/not-found";
        } else {
            this.getFlightUpdateService().deleteFlight(flight, this.getFlightlogUserDetailsHolder().getCurrentUserDetails());
            redirectAttributes.addFlashAttribute("flight", flight);
            return "redirect:/flights/delete/success";
        }
    }

    @GetMapping(path = "/delete/success")
    String doDeleteSuccess() {
        return "flights/delete/success";
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

    FlightlogUserDetailsHolder getFlightlogUserDetailsHolder() {
        return this.flightlogUserDetailsHolder;
    }
    @Autowired
    void setFlightlogUserDetailsHolder(FlightlogUserDetailsHolder flightlogUserDetailsHolder) {
        this.flightlogUserDetailsHolder = flightlogUserDetailsHolder;
    }

}
