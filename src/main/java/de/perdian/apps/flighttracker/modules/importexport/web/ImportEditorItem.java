package de.perdian.apps.flighttracker.modules.importexport.web;

import java.io.Serializable;

import de.perdian.apps.flighttracker.modules.importexport.data.DataItem;

public class ImportEditorItem implements Serializable {

    static final long serialVersionUID = 1L;

    private boolean active = true;
    private DataItem flight = null;

    public ImportEditorItem() {
        this(new DataItem());
    }

    public ImportEditorItem(DataItem flightBean) {
        this.setFlight(flightBean);
    }

    public boolean isActive() {
        return this.active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public DataItem getFlight() {
        return this.flight;
    }
    public void setFlight(DataItem flight) {
        this.flight = flight;
    }

}
