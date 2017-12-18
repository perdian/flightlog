package de.perdian.apps.flighttracker.support.persistence;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PaginatedListTest {

    @Test
    public void constructor() {

        List<Object> objectList = Arrays.asList("foo");
        PaginationData paginationData = new PaginationData(0, 1);

        PaginatedList<Object> paginatedList = new PaginatedList<>(objectList, paginationData);
        Assertions.assertEquals(objectList, paginatedList.getItems());
        Assertions.assertEquals(paginationData, paginatedList.getPagination());

    }

    @Test
    public void getItemFound() {

        PaginatedList<Object> paginatedList = new PaginatedList<>(Arrays.asList("foo"), null);
        Assertions.assertTrue(paginatedList.getItem(0).isPresent());
        Assertions.assertEquals("foo", paginatedList.getItem(0).get());

    }

    @Test
    public void getItemNotFound() {

        PaginatedList<Object> paginatedList = new PaginatedList<>(Arrays.asList("foo"), null);
        Assertions.assertFalse(paginatedList.getItem(1).isPresent());

    }

}
