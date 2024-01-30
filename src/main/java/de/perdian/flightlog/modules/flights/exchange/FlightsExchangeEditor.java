package de.perdian.flightlog.modules.flights.exchange;

import org.springframework.web.multipart.MultipartFile;

public class FlightsExchangeEditor {

    private MultipartFile file = null;
    private FlightsExchangeFormat exchangeFormat = null;
    private FlightsExchangePackage exchangePackage = null;

    public MultipartFile getFile() {
        return this.file;
    }
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public FlightsExchangeFormat getExchangeFormat() {
        return this.exchangeFormat;
    }
    public void setExchangeFormat(FlightsExchangeFormat exchangeFormat) {
        this.exchangeFormat = exchangeFormat;
    }

    public FlightsExchangePackage getExchangePackage() {
        return this.exchangePackage;
    }
    public void setExchangePackage(FlightsExchangePackage exchangePackage) {
        this.exchangePackage = exchangePackage;
    }

}
