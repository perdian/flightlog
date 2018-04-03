package de.perdian.flightlog.support.web;

import java.io.Serializable;

public class Message implements Serializable {

    static final long serialVersionUID = 1L;

    private String title = null;
    private String text = null;

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }

}
