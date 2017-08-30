package de.perdian.apps.flighttracker.web.modules.overview;

public class OverviewQueryHelperItem {

    private String title = null;
    private String value = null;

    OverviewQueryHelperItem(String title, String value) {
        this.setTitle(title);
        this.setValue(value);
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }

}
