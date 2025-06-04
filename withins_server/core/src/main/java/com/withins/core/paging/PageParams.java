package com.withins.core.paging;

public record PageParams(int page, int size) {
    public PageParams() {
        this(0, 10);
    }
}
