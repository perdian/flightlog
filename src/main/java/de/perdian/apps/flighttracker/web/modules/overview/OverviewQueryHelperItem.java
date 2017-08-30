package de.perdian.apps.flighttracker.web.modules.overview;

public class OverviewQueryHelperItem implements Comparable<OverviewQueryHelperItem> {

    private String title = null;
    private String value = null;

    OverviewQueryHelperItem(String title, String value) {
        this.setTitle(title);
        this.setValue(value);
    }

    @Override
    public int compareTo(OverviewQueryHelperItem other) {
        return this.getTitle().compareToIgnoreCase(other.getTitle());
    }

    @Override
    public int hashCode() {
        return this.getTitle().hashCode();
    }

    @Override
    public boolean equals(Object that) {
        return (that instanceof OverviewQueryHelperItem) && this.getTitle().equals(((OverviewQueryHelperItem)that).getTitle());
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
