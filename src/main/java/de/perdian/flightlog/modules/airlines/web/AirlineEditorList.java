package de.perdian.flightlog.modules.airlines.web;

import java.io.Serializable;
import java.util.List;

public class AirlineEditorList implements Serializable {

    static final long serialVersionUID = 1L;

    private List<AirlineEditor> items = null;
    private AirlineEditor newItem = null;

    public List<AirlineEditor> getItems() {
        return this.items;
    }
    public void setItems(List<AirlineEditor> items) {
        this.items = items;
    }

    public AirlineEditor getNewItem() {
        return this.newItem;
    }
    public void setNewItem(AirlineEditor newItem) {
        this.newItem = newItem;
    }

}
