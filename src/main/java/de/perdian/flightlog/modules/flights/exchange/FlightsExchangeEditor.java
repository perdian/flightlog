package de.perdian.flightlog.modules.flights.exchange;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FlightsExchangeEditor {

    private MultipartFile file = null;
    private FlightsExchangeFormat fileFormat = null;
    private List<FlightsExchangeEditorItem> items = null;

    public MultipartFile getFile() {
        return this.file;
    }
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public FlightsExchangeFormat getFileFormat() {
        return this.fileFormat;
    }
    public void setFileFormat(FlightsExchangeFormat fileFormat) {
        this.fileFormat = fileFormat;
    }

    public List<FlightsExchangeEditorItem> getItems() {
        return this.items;
    }
    public void setItems(List<FlightsExchangeEditorItem> items) {
        this.items = items;
    }

}
