package de.perdian.apps.flighttracker.modules.importexport.services;

import java.util.List;

import de.perdian.apps.flighttracker.modules.importexport.data.DataItem;
import de.perdian.apps.flighttracker.modules.users.persistence.UserEntity;

public interface ImportExportService {

    void importDataItems(List<DataItem> dataItems, UserEntity user);
    List<DataItem> exportDataItems(UserEntity user);

}
