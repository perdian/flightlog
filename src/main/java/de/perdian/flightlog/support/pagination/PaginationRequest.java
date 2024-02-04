package de.perdian.flightlog.support.pagination;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class PaginationRequest implements Serializable {

    static final long serialVersionUID = 1L;

    private int pageNumber = 0;
    private int pageSize = 100;

    public PaginationRequest(int pageNumber, int pageSize) {
        this.setPageNumber(pageNumber);
        this.setPageSize(pageSize);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[pageNumber=").append(this.getPageNumber());
        result.append(",pageSize=").append(this.getPageSize());
        return result.append("]").toString();
    }

    public <T> PaginatedList<T> slice(List<T> inputList) {
        int inputStart = this.getPageNumber() * this.getPageSize();
        int inputEnd = Math.min(inputStart + this.getPageSize(), inputList.size());
        List<T> slicedList = inputStart >= inputList.size() ? Collections.emptyList() : inputList.subList(inputStart, inputEnd);
        return new PaginatedList<>(slicedList, this.toPaginationData(inputList.size()));
    }

    public PaginationData toPaginationData(long totalSize) {
        PaginationData paginationData = new PaginationData();
        paginationData.setPageNumber(this.getPageNumber());
        paginationData.setTotalElements(totalSize);
        paginationData.setTotalPages((int)Math.ceil((double)totalSize / (double)this.getPageSize()));
        return paginationData;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }
    private void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return this.pageSize;
    }
    private void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
