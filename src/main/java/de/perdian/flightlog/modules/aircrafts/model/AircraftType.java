package de.perdian.flightlog.modules.aircrafts.model;

public class AircraftType {

    static final long serialVersionUID = 1L;

    private String iataCode = null;
    private String icaoCode = null;
    private String title = null;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[iataCode=").append(this.getIataCode());
        result.append(",icaoCode=").append(this.getIcaoCode());
        result.append(",title=").append(this.getTitle());
        return result.append("]").toString();
    }

    public String getIataCode() {
        return this.iataCode;
    }
    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getIcaoCode() {
        return this.icaoCode;
    }
    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

}
