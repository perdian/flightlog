package de.perdian.flightlog.modules.importexport.web;

import java.io.Serializable;
import java.util.List;

public class ImportEditor implements Serializable {

    static final long serialVersionUID = 1L;

    private List<ImportEditorItem> items = null;

    public List<ImportEditorItem> getItems() {
        return this.items;
    }
    public void setItems(List<ImportEditorItem> items) {
        this.items = items;
    }
}
