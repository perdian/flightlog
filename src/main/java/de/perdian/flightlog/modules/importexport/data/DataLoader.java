package de.perdian.flightlog.modules.importexport.data;

import java.util.List;

public interface DataLoader<T> {

    List<DataItem> loadDataItems(T source) throws Exception;

}
