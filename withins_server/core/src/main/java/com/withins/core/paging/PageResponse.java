package com.withins.core.paging;


public record PageResponse(long totalElements, int totalPages, int pageNumber) {

    public static PageResponse of(long totalElements, int totalPages, int pageNumber) {
        return new PageResponse(totalElements, totalPages, pageNumber);
    }

}
