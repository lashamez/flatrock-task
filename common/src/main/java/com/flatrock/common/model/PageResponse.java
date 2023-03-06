package com.flatrock.common.model;

import java.util.List;

public class PageResponse<T> {
    private final List<T> data;
    private final long totalElements;
    private final int totalPages;

    public PageResponse(List<T> content, long totalElements, int totalPages) {
        this.data = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<T> getData() {
        return data;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }
}




