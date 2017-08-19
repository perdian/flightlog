package de.perdian.apps.flighttracker.business.modules.importexport;

import java.util.List;

import de.perdian.apps.flighttracker.business.modules.importexport.data.DataItem;

public interface ImportExportService {

    void importDataItems(List<DataItem> dataItems);
    List<DataItem> exportDataItems();

}
