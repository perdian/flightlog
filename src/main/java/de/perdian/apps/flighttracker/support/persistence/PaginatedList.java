package de.perdian.apps.flighttracker.support.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class PaginatedList<T> implements Serializable {

    static final long serialVersionUID = 1L;

    private List<T> items = null;
    private PaginationData pagination = null;

    public Optional<T> getItem(int index) {
        return this.getItems().size() > index ? Optional.of(this.getItems().get(index)) : Optional.empty();
    }

    public List<T> getItems() {
        return this.items;
    }
    public void setItems(List<T> items) {
        this.items = items;
    }

    public PaginationData getPagination() {
        return this.pagination;
    }
    public void setPagination(PaginationData pagination) {
        this.pagination = pagination;
    }

}
