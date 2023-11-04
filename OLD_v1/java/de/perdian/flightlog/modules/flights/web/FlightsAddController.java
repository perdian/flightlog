package de.perdian.flightlog.modules.flights.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.perdian.flightlog.modules.authentication.FlightlogUser;
import de.perdian.flightlog.modules.flights.editor.FlightBean;
import de.perdian.flightlog.modules.flights.services.FlightsUpdateService;
import de.perdian.flightlog.modules.wizard.services.WizardData;
import de.perdian.flightlog.modules.wizard.services.WizardDataService;
import de.perdian.flightlog.support.FlightlogHelper;
import de.perdian.flightlog.support.web.MessageSeverity;
import de.perdian.flightlog.support.web.Messages;

@Controller
public class FlightsAddController {

    private MessageSource messageSource = null;
    private FlightsUpdateService flightsUpdateService = null;
    private FlightsWizardService flightsWizardService = null;
    private WizardDataService wizardDataService = null;

    @ModelAttribute
    public FlightEditor flightEditor() {
        return new FlightEditor();
    }

    @RequestMapping(value = "/flights/add", method = RequestMethod.GET)
    public String doAddGet() {
        return "/flights/add";
    }

    @RequestMapping(value = "/flights/add/wizard", method = RequestMethod.GET)
    public String doAddWizardGet() {
        return this.doAddGet();
    }

    @RequestMapping(value = "/flights/add/wizard", method = RequestMethod.POST)
    public String doAddWizardPost(@ModelAttribute("flightEditor") FlightEditor flightEditor, FlightWizardEditor flightWizardEditor, Model model) {
        LocalDate wizardDepartureDate = FlightlogHelper.parseLocalDate(flightWizardEditor.getWizDepartureDateLocal());
        String wizardAirlineCode = Optional.ofNullable(flightWizardEditor.getWizAirlineCode()).map(String::toUpperCase).orElse(null);
        List<WizardData> wizardDataList = StringUtils.isBlank(wizardAirlineCode) || StringUtils.isBlank(flightWizardEditor.getWizFlightNumber()) ? null : this.getWizardDataService().createData(wizardAirlineCode, flightWizardEditor.getWizFlightNumber(), wizardDepartureDate, flightWizardEditor.getWizDepartureAirportCode());
        if (wizardDataList != null && wizardDataList.size() == 1) {
            this.getFlightsWizardService().enhanceFlightEditor(flightEditor, flightWizardEditor, wizardDataList.get(0));
        } else if (wizardDataList != null && !wizardDataList.isEmpty()) {
            model.addAttribute("wizardFlightEditors", wizardDataList.stream()
                .map(wizardData -> this.getFlightsWizardService().enhanceFlightEditor(new FlightEditor(), flightWizardEditor, wizardData))
                .collect(Collectors.toList())
            );
        } else {
            flightEditor.setAirlineCode(flightWizardEditor.getWizAirlineCode());
            flightEditor.setDepartureAirportCode(flightWizardEditor.getWizDepartureAirportCode());
            flightEditor.setDepartureDateLocal(flightWizardEditor.getWizDepartureDateLocal());
            flightEditor.setFlightNumber(flightWizardEditor.getWizFlightNumber());
        }
        return this.doAddWizardGet();
    }

    @RequestMapping(value = "/flights/add", method = RequestMethod.POST)
    public String doAddPost(FlightlogUser user, @Valid @ModelAttribute("flightEditor") FlightEditor flightEditor, BindingResult bindingResult, @ModelAttribute Messages messages, Locale locale) {
        if (!bindingResult.hasErrors()) {

            FlightBean flightBean = new FlightBean();
            flightBean.setUser(user == null ? null : user.getUserEntity());
            flightEditor.copyValuesInto(flightBean);
            FlightBean insertedFlightBean = this.getFlightsUpdateService().saveFlight(flightBean, user == null ? null : user.getUserEntity());

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

    WizardDataService getWizardDataService() {
        return this.wizardDataService;
    }
    @Autowired
    void setWizardDataService(WizardDataService wizardDataService) {
        this.wizardDataService = wizardDataService;
    }

}
