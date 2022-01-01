package de.perdian.flightlog.modules.importexport.data;

import java.util.List;

public interface DataWriter<T> {

    T writeDataItems(List<DataItem> dataItems) throws Exception;

}
