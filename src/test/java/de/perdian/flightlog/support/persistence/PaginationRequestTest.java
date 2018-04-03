package de.perdian.flightlog.support.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.flightlog.support.persistence.PaginationRequest;

public class PaginationRequestTest {

    @Test
    public void setPageSize() {
        PaginationRequest paginationRequest = new PaginationRequest();
        paginationRequest.setPageSize(50);
        Assertions.assertEquals(50, paginationRequest.getPageSize());
    }

    @Test
    public void setPageSizeOverflow() {
        PaginationRequest paginationRequest = new PaginationRequest();
        paginationRequest.setPageSize(500);
        Assertions.assertEquals(100, paginationRequest.getPageSize());
    }

    @Test
    public void setPageSizeNegative() {
        PaginationRequest paginationRequest = new PaginationRequest();
        paginationRequest.setPageSize(-25);
        Assertions.assertEquals(25, paginationRequest.getPageSize());
    }

}
