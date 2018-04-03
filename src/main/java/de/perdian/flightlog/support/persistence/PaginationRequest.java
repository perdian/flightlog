package de.perdian.flightlog.support.persistence;

import java.io.Serializable;

public class PaginationRequest implements Serializable {

    static final long serialVersionUID = 1L;

    private int offset = 0;
    private int pageSize = 100;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[offset=").append(this.getOffset());
        result.append(",pageSize=").append(this.getPageSize());
        return result.append("]").toString();
    }

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
