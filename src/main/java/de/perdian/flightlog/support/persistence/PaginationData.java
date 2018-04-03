package de.perdian.flightlog.support.persistence;

import java.io.Serializable;

public class PaginationData implements Serializable {

    static final long serialVersionUID = 1L;

    private int page = 0;
    private int totalPages = 0;

    public PaginationData(int page, int totalPages) {
        this.setPage(page);
        this.setTotalPages(totalPages);
    }

    @Override
    public String toString() {
        return "[" + this.getPage() + "/" + this.getTotalPages() + "]";
    }

    public int getPage() {
        return this.page;
    }
    private void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return this.totalPages;
    }
    private void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}
