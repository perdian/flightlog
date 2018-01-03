package de.perdian.apps.flighttracker.modules.airlines.web;

import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.airlines.services.AirlinesService;
import de.perdian.apps.flighttracker.modules.security.web.FlighttrackerUser;
import de.perdian.apps.flighttracker.support.web.MessageSeverity;
import de.perdian.apps.flighttracker.support.web.Messages;

@Controller
public class AirlineEditController {

    private static final Logger log = LoggerFactory.getLogger(AirlineEditController.class);
    private AirlinesService airlinesService = null;
    private MessageSource messageSource = null;

    @RequestMapping(value = "/airlines/list", method = RequestMethod.GET)
    public String doListGet(@AuthenticationPrincipal FlighttrackerUser user, Model model) {
        AirlineEditorList airlineEditorList = new AirlineEditorList();
        airlineEditorList.setItems(this.getAirlinesService().loadUserSpecificAirlines(user == null ? null : user.getUserEntity()).stream().map(this::createAirlineEditor).collect(Collectors.toList()));
        model.addAttribute("airlines", airlineEditorList);
        return "/airlines/list";
    }

    @RequestMapping(value = "/airlines/list", method = RequestMethod.POST)
    public String doListPost(@AuthenticationPrincipal FlighttrackerUser user, AirlineEditorList editorList, @ModelAttribute Messages messages, Locale locale, Model model) {
        try {
            if (editorList.getItems() != null) {
                for (AirlineEditor editor : editorList.getItems()) {
                    if (editor.isDelete()) {
                        this.getAirlinesService().deleteUserSpecificAirline(user == null ? null : user.getUserEntity(), editor.getAirlineBean());
                    } else {
                        this.getAirlinesService().updateUserSpecificAirline(user == null ? null : user.getUserEntity(), editor.getAirlineBean());
                    }
                }
            }
            if (editorList.getNewItem() != null && !StringUtils.isEmpty(editorList.getNewItem().getAirlineBean().getCode())) {
                this.getAirlinesService().updateUserSpecificAirline(user == null ? null : user.getUserEntity(), editorList.getNewItem().getAirlineBean());
            }
            messages.addMessage(MessageSeverity.INFO, this.getMessageSource().getMessage("updateSuccessful", null, locale), null);
            return this.doListGet(user, model);
        } catch (Exception e) {
            log.warn("Cannot update airline", e);
            messages.addMessage(MessageSeverity.ERROR, this.getMessageSource().getMessage("updateError", null, locale), ExceptionUtils.getMessage(e));
            model.addAttribute("airlines", editorList);
            return "/airlines/list";
        }
    }

    private AirlineEditor createAirlineEditor(AirlineBean airlineBan) {
        AirlineEditor editor = new AirlineEditor();
        editor.setAirlineBean(airlineBan);
        return editor;
    }

    AirlinesService getAirlinesService() {
        return this.airlinesService;
    }
    @Autowired
    void setAirlinesService(AirlinesService airlinesService) {
        this.airlinesService = airlinesService;
    }

    MessageSource getMessageSource() {
        return this.messageSource;
    }
    @Autowired
    void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

}
