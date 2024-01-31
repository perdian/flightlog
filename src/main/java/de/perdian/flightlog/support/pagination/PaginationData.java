package de.perdian.flightlog.support.pagination;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaginationData implements Serializable {

    static final long serialVersionUID = 1L;

    private int pageNumber = 0;
    private int totalPages = 0;
    private long totalElements = 0;

    public PaginationData() {
    }

    public PaginationData(Page<?> page) {
        this.setPageNumber(page.getPageable().getPageNumber());
        this.setTotalPages(page.getTotalPages());
        this.setTotalElements(page.getTotalElements());
    }

    @Override
    public String toString() {
        return "[" + this.getPageNumber() + "/" + this.getTotalPages() + "]";
    }

    public List<PaginationDataLink> toLinks(int windowSize) {
        List<PaginationDataLink> links = new ArrayList<>();
        int leftMinimum = windowSize;
        int rightMinimum = this.getTotalPages() - windowSize;
        boolean outOfWindow = false;
        for (int pageIndex=0; pageIndex < this.getTotalPages(); pageIndex++) {
            if (pageIndex < leftMinimum || (pageIndex >= rightMinimum)) {
                outOfWindow = false;
                links.add(PaginationDataLink.forPage(pageIndex));
            } else {
                boolean outOfWindowLeft = pageIndex < (this.getPageNumber() - windowSize);
                boolean outOfWindowRight = pageIndex > (this.getPageNumber() + windowSize);
                if (outOfWindowLeft || outOfWindowRight) {
                    if (!outOfWindow) {
                        outOfWindow = true;
                        links.add(PaginationDataLink.forSpacer());
                    }
                } else {
                    outOfWindow = false;
                    links.add(PaginationDataLink.forPage(pageIndex));
                }
            }
        }
        return links;
    }

    public static class PaginationDataLink {

        private boolean spacer = false;
        private Integer pageNumber = null;

        private PaginationDataLink() {
        }

        private static PaginationDataLink forPage(int pageNumber) {
            PaginationDataLink link = new PaginationDataLink();
            link.setPageNumber(pageNumber);
            return link;
        }

        private static PaginationDataLink forSpacer() {
            PaginationDataLink link = new PaginationDataLink();
            link.setSpacer(true);
            return link;
        }

        public boolean isSpacer() {
            return this.spacer;
        }
        private void setSpacer(boolean spacer) {
            this.spacer = spacer;
        }

        public Integer getPageNumber() {
            return this.pageNumber;
        }
        private void setPageNumber(Integer pageNumber) {
            this.pageNumber = pageNumber;
        }

    }

    public int getPageNumber() {
        return this.pageNumber;
    }
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        return this.totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return this.totalElements;
    }
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

}
