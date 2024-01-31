package de.perdian.flightlog.support.pagination;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PaginationDataTest {

    @Test
    public void testToLinks() {
        PaginationData paginationData = new PaginationData();
        paginationData.setPageNumber(10);
        paginationData.setTotalPages(20);
        List<PaginationData.PaginationDataLink> paginationDataLinks = paginationData.toLinks(2);
        MatcherAssert.assertThat(paginationDataLinks.get(0).getPageNumber(), IsEqual.equalTo(0));
        MatcherAssert.assertThat(paginationDataLinks.get(1).getPageNumber(), IsEqual.equalTo(1));
        MatcherAssert.assertThat(paginationDataLinks.get(2).isSpacer(), IsEqual.equalTo(true));
        MatcherAssert.assertThat(paginationDataLinks.get(3).getPageNumber(), IsEqual.equalTo(8));
        MatcherAssert.assertThat(paginationDataLinks.get(4).getPageNumber(), IsEqual.equalTo(9));
        MatcherAssert.assertThat(paginationDataLinks.get(5).getPageNumber(), IsEqual.equalTo(10));
        MatcherAssert.assertThat(paginationDataLinks.get(6).getPageNumber(), IsEqual.equalTo(11));
        MatcherAssert.assertThat(paginationDataLinks.get(7).getPageNumber(), IsEqual.equalTo(12));
        MatcherAssert.assertThat(paginationDataLinks.get(8).isSpacer(), IsEqual.equalTo(true));
        MatcherAssert.assertThat(paginationDataLinks.get(9).getPageNumber(), IsEqual.equalTo(18));
        MatcherAssert.assertThat(paginationDataLinks.get(10).getPageNumber(), IsEqual.equalTo(19));
    }

    @Test
    public void testToLinksAllInWindow() {
        PaginationData paginationData = new PaginationData();
        paginationData.setPageNumber(4);
        paginationData.setTotalPages(5);
        List<PaginationData.PaginationDataLink> paginationDataLinks = paginationData.toLinks(3);
        MatcherAssert.assertThat(paginationDataLinks.get(0).getPageNumber(), IsEqual.equalTo(0));
        MatcherAssert.assertThat(paginationDataLinks.get(1).getPageNumber(), IsEqual.equalTo(1));
        MatcherAssert.assertThat(paginationDataLinks.get(2).getPageNumber(), IsEqual.equalTo(2));
        MatcherAssert.assertThat(paginationDataLinks.get(3).getPageNumber(), IsEqual.equalTo(3));
        MatcherAssert.assertThat(paginationDataLinks.get(4).getPageNumber(), IsEqual.equalTo(4));
    }

}
