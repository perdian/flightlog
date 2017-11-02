package de.perdian.apps.flighttracker.support.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class MessagesAdvice {

    @ModelAttribute
    public Messages messages() {
        return new Messages();
    }

}
