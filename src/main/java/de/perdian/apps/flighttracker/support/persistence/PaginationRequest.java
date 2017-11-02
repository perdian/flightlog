package de.perdian.apps.flighttracker.support.persistence;

import java.io.Serializable;

public class PaginationRequest implements Serializable {

    static final long serialVersionUID = 1L;

    private int offset = 0;
    private int pageSize = 100;

    public int getOffset() {
        return this.offset;
    }
    public void setOffset(int offset) {
        this.offset = Math.max(0, offset);
    }

    public int getPageSize() {
        return this.pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = Math.min(100, Math.abs(pageSize));
    }

}
