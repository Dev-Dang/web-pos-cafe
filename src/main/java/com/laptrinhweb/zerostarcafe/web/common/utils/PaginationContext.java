package com.laptrinhweb.zerostarcafe.web.common.utils;

import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import com.laptrinhweb.zerostarcafe.web.common.routing.RouteMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Context information for pagination requests.
 * Helps determine the type of pagination (search vs category) and build appropriate URLs.
 */
@RequiredArgsConstructor
@Getter
public class PaginationContext {
    private final Type type;
    private final String query;
    private final int currentPage;
    private final boolean hasMore;
    private final int resultsCount;

    private enum Type {
        SEARCH, CATEGORY
    }

    /**
     * Create search pagination context.
     *
     * @param query the search query
     * @return pagination context for search
     */
    public static PaginationContext search(String query, int currentPage, boolean hasMore, int resultsCount) {
        return new PaginationContext(Type.SEARCH, query, currentPage, hasMore, resultsCount);
    }

    /**
     * Create category pagination context.
     *
     * @param slug the category slug
     * @return pagination context for category
     */
    public static PaginationContext category(String slug, int currentPage, boolean hasMore, int resultsCount) {
        return new PaginationContext(Type.CATEGORY, slug, currentPage, hasMore, resultsCount);
    }

    /**
     * Build the pagination URL based on the context type.
     *
     * @return the pagination URL
     */
    public String getPaginationUrl() {
        int nextPage = hasMore ? currentPage + 1 : currentPage;
        return switch (type) {
            case SEARCH -> RouteMap.SEARCH +
                    "?" + WebConstants.Param.SEARCH_KEYWORD + "=" + query +
                    "&" + WebConstants.Param.PAGE + "=" + nextPage;
            case CATEGORY -> RouteMap.CATEGORY_PRODUCTS +
                    "/" + query +
                    "?" + WebConstants.Param.PAGE + "=" + nextPage;
        };
    }
}