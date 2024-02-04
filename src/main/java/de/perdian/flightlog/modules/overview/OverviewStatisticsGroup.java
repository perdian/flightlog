package de.perdian.flightlog.modules.overview;

import java.util.List;

public class OverviewStatisticsGroup {

    private OverviewString title = null;
    private List<OverviewStatisticsItem> items = null;

    public OverviewString getTitle() {
        return this.title;
    }
    void setTitle(OverviewString title) {
        this.title = title;
    }

    public List<OverviewStatisticsItem> getItems() {
        return this.items;
    }
    void setItems(List<OverviewStatisticsItem> items) {
        this.items = items;
    }

}
