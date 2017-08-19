package de.perdian.apps.flighttracker.business.modules.importexport.data;

import java.util.List;

public interface DataWriter<T> {

    T writeDataItems(List<DataItem> dataItems) throws Exception;

}
