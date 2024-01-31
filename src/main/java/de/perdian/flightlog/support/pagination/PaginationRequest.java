package de.perdian.flightlog.support.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

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

    public Pageable toPageable(Sort sort) {
        return PageRequest.of(this.getPageNumber(), this.getPageSize(), sort);
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
        this.pageSize = Math.min(100, Math.abs(pageSize));
    }

}
