package de.perdian.flightlog.support.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.perdian.flightlog.support.persistence.PaginationData;

public class PaginationDataTest {

    @Test
    public void constructor() {
        PaginationData paginationData = new PaginationData(1, 2);
        Assertions.assertEquals(1, paginationData.getPage());
        Assertions.assertEquals(2, paginationData.getTotalPages());
    }

}
