package de.perdian.flightlog.modules.importexport.services;

import java.util.List;

import de.perdian.flightlog.modules.importexport.data.DataItem;
import de.perdian.flightlog.modules.users.persistence.UserEntity;

public interface ImportExportService {

    void importDataItems(List<DataItem> dataItems, UserEntity user);
    List<DataItem> exportDataItems(UserEntity user);

}
