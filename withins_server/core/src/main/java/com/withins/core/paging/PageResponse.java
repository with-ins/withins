package com.withins.core.paging;

import java.util.List;

public record PageResponse<T>(int totalPages, long totalElements, int number, int size, List<T> content) {
}
