package de.perdian.apps.flighttracker.modules.airlines.web;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.perdian.apps.flighttracker.modules.airlines.model.AirlineBean;
import de.perdian.apps.flighttracker.modules.airlines.services.AirlinesService;
import de.perdian.apps.flighttracker.modules.security.web.FlighttrackerUser;

@Controller
public class AirlineEditController {

    private AirlinesService airlinesService = null;

    @RequestMapping(value = "/airlines/list", method = RequestMethod.GET)
    public String doListGet(@AuthenticationPrincipal FlighttrackerUser user, Model model) {
        AirlineEditorList airlineEditorList = new AirlineEditorList();
        airlineEditorList.setAirlines(this.getAirlinesService().loadUserSpecificAirlines(user == null ? null : user.getUserEntity()).stream().map(this::createAirlineEditor).collect(Collectors.toList()));
        model.addAttribute("airlines", airlineEditorList);
        return "/airlines/list";
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

}
