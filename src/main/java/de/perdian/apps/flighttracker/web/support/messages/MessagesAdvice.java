package de.perdian.apps.flighttracker.web.support.messages;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class MessagesAdvice {

    @ModelAttribute
    public Messages messages() {
        return new Messages();
    }

}
