package de.perdian.flightlog.support.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PaginationDataTest {

    @Test
    public void constructor() {
        PaginationData paginationData = new PaginationData(1, 2);
        Assertions.assertEquals(1, paginationData.getPage());
        Assertions.assertEquals(2, paginationData.getTotalPages());
    }

}
