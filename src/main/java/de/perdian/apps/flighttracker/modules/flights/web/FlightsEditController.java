package de.perdian.apps.flighttracker.modules.flights.web;

import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.perdian.apps.flighttracker.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.modules.flights.services.FlightsQuery;
import de.perdian.apps.flighttracker.modules.flights.services.FlightsQueryService;
import de.perdian.apps.flighttracker.modules.flights.services.FlightsUpdateService;
import de.perdian.apps.flighttracker.modules.security.web.FlighttrackerUser;
import de.perdian.apps.flighttracker.support.persistence.PaginatedList;
import de.perdian.apps.flighttracker.support.web.MessageSeverity;
import de.perdian.apps.flighttracker.support.web.Messages;

@Controller
public class FlightsEditController {

    private MessageSource messageSource = null;
    private FlightsQueryService flightsQueryService = null;
    private FlightsUpdateService flightsUpdateService = null;

    @RequestMapping(value = "/flights/edit/{id}", method = RequestMethod.GET)
    public String doEditGet(@AuthenticationPrincipal FlighttrackerUser user, @PathVariable("id") UUID id, @RequestParam(name = "updated", required = false) Boolean updated, @ModelAttribute Messages messages, Locale locale, Model model) {

        FlightsQuery flightsQuery = new FlightsQuery();
        flightsQuery.setRestrictUser(user == null ? null : user.getUserEntity());
        flightsQuery.setRestrictIdentifiers(Arrays.asList(id));
        PaginatedList<FlightBean> flights = this.getFlightsQueryService().loadFlights(flightsQuery);
        FlightBean flight = flights.getItem(0).orElse(null);
        if (flight == null) {
            throw new FlightNotFoundException();
        } else {
            if (Boolean.TRUE.equals(updated)) {
                messages.addMessage(MessageSeverity.INFO, this.getMessageSource().getMessage("updateSuccessful", null, locale), null);
            }
            model.addAttribute("flightEditor", new FlightEditor(flight));
            return "/flights/edit";
        }
    }

    @RequestMapping(value = "/flights/edit/{id}", method = RequestMethod.POST)
    public String doEditPost(@AuthenticationPrincipal FlighttrackerUser user, @PathVariable("id") UUID id, @Valid @ModelAttribute("flightEditor") FlightEditor flightEditor, BindingResult bindingResult, @ModelAttribute Messages messages, Locale locale, Model model) {
        if (!bindingResult.hasErrors()) {

            FlightsQuery targetFlightsQuery = new FlightsQuery();
            targetFlightsQuery.setRestrictUser(user == null ? null : user.getUserEntity());
            targetFlightsQuery.setRestrictIdentifiers(Arrays.asList(id));
            PaginatedList<FlightBean> flights = this.getFlightsQueryService().loadFlights(targetFlightsQuery);
            FlightBean flight = flights.getItem(0).orElse(null);
            if (flight == null) {
                throw new FlightNotFoundException();
            } else {

                flightEditor.copyValuesInto(flight);
                this.getFlightsUpdateService().saveFlight(flight, user == null ? null : user.getUserEntity());

                return "redirect:/flights/edit/" + flight.getEntityId() + "?updated=true";

            }
        } else {
            messages.addMessage(MessageSeverity.ERROR, this.getMessageSource().getMessage("updateError", null, locale), this.getMessageSource().getMessage("cannotAddFlightDueToBindingErrors", null, locale));
            return this.doEditGet(user, id, null, messages, locale, model);
        }
    }

    @RequestMapping(value = "/flights/delete/{id}", method = RequestMethod.GET)
    public String doDeleteGet(@AuthenticationPrincipal FlighttrackerUser user, @PathVariable("id") UUID id, Model model) {

        FlightsQuery flightsQuery = new FlightsQuery();
        flightsQuery.setRestrictUser(user == null ? null : user.getUserEntity());
        flightsQuery.setRestrictIdentifiers(Arrays.asList(id));
        FlightBean flight = this.getFlightsQueryService().loadFlights(flightsQuery).getItem(0).orElse(null);
        if (flight == null) {
            throw new FlightNotFoundException();
        } else {
            model.addAttribute("flight", flight);
            return "/flights/delete";
        }
    }

    @RequestMapping(value = "/flights/delete/{id}", method = RequestMethod.POST)
    public String doDeletePost(@AuthenticationPrincipal FlighttrackerUser user, @PathVariable("id") UUID id, @ModelAttribute Messages messages, Locale locale, Model model) {
        FlightsQuery flightsQuery = new FlightsQuery();
        flightsQuery.setRestrictUser(user == null ? null : user.getUserEntity());
        flightsQuery.setRestrictIdentifiers(Arrays.asList(id));
        FlightBean flight = this.getFlightsQueryService().loadFlights(flightsQuery).getItem(0).orElse(null);
        if (flight == null) {
            throw new FlightNotFoundException();
        } else {
            this.getFlightsUpdateService().deleteFlight(flight);
            return "redirect:/flights/list?updated=true";
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    static class FlightNotFoundException extends RuntimeException {

        static final long serialVersionUID = 1L;

    }

    MessageSource getMessageSource() {
        return this.messageSource;
    }
    @Autowired
    void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    FlightsQueryService getFlightsQueryService() {
        return this.flightsQueryService;
    }
    @Autowired
    void setFlightsQueryService(FlightsQueryService flightsQueryService) {
        this.flightsQueryService = flightsQueryService;
    }

    FlightsUpdateService getFlightsUpdateService() {
        return this.flightsUpdateService;
    }
    @Autowired
    void setFlightsUpdateService(FlightsUpdateService flightsUpdateService) {
        this.flightsUpdateService = flightsUpdateService;
    }

}
