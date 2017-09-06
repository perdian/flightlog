package de.perdian.apps.flighttracker.web.modules.flights;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.perdian.apps.flighttracker.business.modules.flights.FlightsUpdateService;
import de.perdian.apps.flighttracker.business.modules.flights.model.FlightBean;
import de.perdian.apps.flighttracker.web.security.FlighttrackerUser;
import de.perdian.apps.flighttracker.web.support.messages.MessageSeverity;
import de.perdian.apps.flighttracker.web.support.messages.Messages;

@Controller
public class FlightsAddController {

    private MessageSource messageSource = null;
    private FlightsUpdateService flightsUpdateService = null;
    private FlightsWizardService flightsWizardService = null;

    @ModelAttribute
    public FlightEditor flightEditor() {
        return new FlightEditor();
    }

    @RequestMapping(value = "/flights/add", method = RequestMethod.GET)
    public String doAddGet() {
        return "/flights/add";
    }

    @RequestMapping(value = "/flights/add/wizard", method = RequestMethod.POST)
    public String doAddGetWizard(@ModelAttribute("flightEditor") FlightEditor flightEditor, FlightsWizardData wizardData) {
        this.getFlightsWizardService().enhanceFlightEditor(flightEditor, wizardData);
        return this.doAddGet();
    }

    @RequestMapping(value = "/flights/add", method = RequestMethod.POST)
    public String doAddPost(@AuthenticationPrincipal FlighttrackerUser user, @Valid @ModelAttribute("flightEditor") FlightEditor flightEditor, BindingResult bindingResult, @ModelAttribute Messages messages, Locale locale) {
        if (!bindingResult.hasErrors()) {

            FlightBean flightBean = new FlightBean();
            flightBean.setUser(user == null ? null : user.getUserEntitiy());
            flightEditor.copyValuesInto(flightBean);
            FlightBean insertedFlightBean = this.getFlightsUpdateService().saveFlight(flightBean);

            return "redirect:/flights/edit/" + insertedFlightBean.getEntityId() + "?updated=true";

        } else {
            messages.addMessage(MessageSeverity.ERROR, this.getMessageSource().getMessage("updateError", null, locale), this.getMessageSource().getMessage("cannotAddFlightDueToBindingErrors", null, locale));
            return this.doAddGet();
        }
    }

    MessageSource getMessageSource() {
        return this.messageSource;
    }
    @Autowired
    void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    FlightsUpdateService getFlightsUpdateService() {
        return this.flightsUpdateService;
    }
    @Autowired
    void setFlightsUpdateService(FlightsUpdateService flightsUpdateService) {
        this.flightsUpdateService = flightsUpdateService;
    }

    FlightsWizardService getFlightsWizardService() {
        return this.flightsWizardService;
    }
    @Autowired
    void setFlightsWizardService(FlightsWizardService flightsWizardService) {
        this.flightsWizardService = flightsWizardService;
    }

}
