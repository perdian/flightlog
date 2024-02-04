package de.perdian.flightlog.support.pagination;

import org.hamcrest.MatcherAssert;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PaginationRequestTest {

    @Test
    public void sliceWithStartPageZero() {
        List<String> inputList = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I");
        PaginatedList<String> paginatedList = new PaginationRequest(0, 2).slice(inputList);
        MatcherAssert.assertThat(paginatedList.getItems(), IsCollectionWithSize.hasSize(2));
        MatcherAssert.assertThat(paginatedList.getItems(), IsIterableContainingInOrder.contains("A", "B"));
        MatcherAssert.assertThat(paginatedList.getPagination().getPageNumber(), IsEqual.equalTo(0));
        MatcherAssert.assertThat(paginatedList.getPagination().getTotalElements(), IsEqual.equalTo(9L));
        MatcherAssert.assertThat(paginatedList.getPagination().getTotalPages(), IsEqual.equalTo(5));
    }

    @Test
    public void sliceWithStartPageOne() {
        List<String> inputList = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I");
        PaginatedList<String> paginatedList = new PaginationRequest(1, 2).slice(inputList);
        MatcherAssert.assertThat(paginatedList.getItems(), IsCollectionWithSize.hasSize(2));
        MatcherAssert.assertThat(paginatedList.getItems(), IsIterableContainingInOrder.contains("C", "D"));
        MatcherAssert.assertThat(paginatedList.getPagination().getPageNumber(), IsEqual.equalTo(1));
        MatcherAssert.assertThat(paginatedList.getPagination().getTotalElements(), IsEqual.equalTo(9L));
        MatcherAssert.assertThat(paginatedList.getPagination().getTotalPages(), IsEqual.equalTo(5));
    }

    @Test
    public void sliceWithEverythingInSlice() {
        List<String> inputList = List.of("A", "B", "C");
        PaginatedList<String> paginatedList = new PaginationRequest(0, 100).slice(inputList);
        MatcherAssert.assertThat(paginatedList.getItems(), IsCollectionWithSize.hasSize(3));
        MatcherAssert.assertThat(paginatedList.getItems(), IsIterableContainingInOrder.contains("A", "B", "C"));
        MatcherAssert.assertThat(paginatedList.getPagination().getPageNumber(), IsEqual.equalTo(0));
        MatcherAssert.assertThat(paginatedList.getPagination().getTotalElements(), IsEqual.equalTo(3L));
        MatcherAssert.assertThat(paginatedList.getPagination().getTotalPages(), IsEqual.equalTo(1));
    }

    @Test
    public void sliceWithEverythingOutOfSlice() {
        List<String> inputList = List.of("A", "B", "C");
        PaginatedList<String> paginatedList = new PaginationRequest(1, 100).slice(inputList);
        MatcherAssert.assertThat(paginatedList.getItems(), IsEmptyCollection.empty());
        MatcherAssert.assertThat(paginatedList.getPagination().getPageNumber(), IsEqual.equalTo(1));
        MatcherAssert.assertThat(paginatedList.getPagination().getTotalElements(), IsEqual.equalTo(3L));
        MatcherAssert.assertThat(paginatedList.getPagination().getTotalPages(), IsEqual.equalTo(1));
    }

}
