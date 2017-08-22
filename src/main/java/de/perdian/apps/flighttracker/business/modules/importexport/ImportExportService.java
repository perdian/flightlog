package de.perdian.apps.flighttracker.business.modules.importexport;

import java.util.List;

import de.perdian.apps.flighttracker.business.modules.importexport.data.DataItem;
import de.perdian.apps.flighttracker.persistence.entities.UserEntity;

public interface ImportExportService {

    void importDataItems(List<DataItem> dataItems, UserEntity user);
    List<DataItem> exportDataItems(UserEntity user);

}
