package de.perdian.flightlog.modules.flights.exchange;

import org.springframework.web.multipart.MultipartFile;

public class FlightsExchangeEditor {

    private MultipartFile file = null;
    private FlightsExchangePackage exchangePackage = null;

    public MultipartFile getFile() {
        return this.file;
    }
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public FlightsExchangePackage getExchangePackage() {
        return this.exchangePackage;
    }
    public void setExchangePackage(FlightsExchangePackage exchangePackage) {
        this.exchangePackage = exchangePackage;
    }

}
