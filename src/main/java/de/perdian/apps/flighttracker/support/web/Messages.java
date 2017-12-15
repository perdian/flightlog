package de.perdian.apps.flighttracker.support.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Messages implements Serializable {

    static final long serialVersionUID = 1L;

    private Map<MessageSeverity, List<Message>> messagesBySeverity = new HashMap<>();

    public void addMessage(MessageSeverity messageSeverity, String title, String text) {
        Message message = new Message();
        message.setTitle(title);
        message.setText(text);
        this.addMessage(messageSeverity, message);
    }

    public void addMessage(MessageSeverity messageSeverity, Message message) {
        if (messageSeverity == null) {
            throw new IllegalArgumentException("Parameter 'messageSeverity' must not be null!");
        } else {
            this.getMessagesBySeverity().compute(messageSeverity, (k, v) -> v == null ? new ArrayList<>() : v).add(message);
        }
    }

    public Map<MessageSeverity, List<Message>> getMessagesBySeverity() {
        return this.messagesBySeverity;
    }
    public void setMessagesBySeverity(Map<MessageSeverity, List<Message>> messagesBySeverity) {
        this.messagesBySeverity = messagesBySeverity;
    }

}
