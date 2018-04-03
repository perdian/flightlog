package de.perdian.flightlog.support.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.flightlog.support.web.Message;
import de.perdian.flightlog.support.web.MessageSeverity;
import de.perdian.flightlog.support.web.Messages;

public class MessagesTest {

    @Test
    public void addMessage() {

        Message message1a = new Message();
        Message message1b = new Message();
        Message message2 = new Message();

        Messages messages = new Messages();
        messages.addMessage(MessageSeverity.ERROR, message1a);
        messages.addMessage(MessageSeverity.ERROR, message1b);
        messages.addMessage(MessageSeverity.INFO, message2);

        Assertions.assertEquals(2, messages.getMessagesBySeverity().size());
        Assertions.assertEquals(2, messages.getMessagesBySeverity().get(MessageSeverity.ERROR).size());
        Assertions.assertEquals(message1a, messages.getMessagesBySeverity().get(MessageSeverity.ERROR).get(0));
        Assertions.assertEquals(message1b, messages.getMessagesBySeverity().get(MessageSeverity.ERROR).get(1));
        Assertions.assertEquals(1, messages.getMessagesBySeverity().get(MessageSeverity.INFO).size());
        Assertions.assertEquals(message2, messages.getMessagesBySeverity().get(MessageSeverity.INFO).get(0));

    }

    @Test
    public void addMessageByParameters() {

        Messages messages = new Messages();
        messages.addMessage(MessageSeverity.ERROR, "aTitle", "aText");

        Assertions.assertEquals(1, messages.getMessagesBySeverity().size());
        Assertions.assertEquals(1, messages.getMessagesBySeverity().get(MessageSeverity.ERROR).size());
        Assertions.assertEquals("aTitle", messages.getMessagesBySeverity().get(MessageSeverity.ERROR).get(0).getTitle());
        Assertions.assertEquals("aText", messages.getMessagesBySeverity().get(MessageSeverity.ERROR).get(0).getText());

    }

    @Test
    public void addMessageWithSeverityNull() {
        Messages messages = new Messages();
        Assertions.assertThrows(IllegalArgumentException.class, () -> messages.addMessage(null, "aTitle", "aText"));
    }

}
