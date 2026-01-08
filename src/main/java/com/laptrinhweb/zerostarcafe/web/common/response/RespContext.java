package com.laptrinhweb.zerostarcafe.web.common.response;

import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import com.laptrinhweb.zerostarcafe.web.common.utils.PaginationContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Generic response context holder for all response metadata.
 * Stores any key-value pairs needed in the response.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 03/01/2026
 * @since 1.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RespContext {

    private final HttpServletRequest req;

    @Getter
    private final Map<String, Object> data = new HashMap<>();

    /**
     * Get or create RespContext from request.
     */
    public static RespContext from(HttpServletRequest req) {
        RespContext ctx = (RespContext) req.getAttribute(
                WebConstants.Attribute.RESPONSE_CONTEXT);

        if (ctx == null) {
            ctx = new RespContext(req);
            req.setAttribute(WebConstants.Attribute.RESPONSE_CONTEXT, ctx);
        }
        return ctx;
    }

    /**
     * Set data with key-value pair and sync to request attributes.
     */
    public RespContext setData(String key, Object value) {
        data.put(key, value);
        syncDataToRequest();
        return this;
    }

    /**
     * Set pagination data from PaginationContext and sync to request attributes.
     */
    public RespContext setPaginationData(PaginationContext context) {
        data.put(WebConstants.Attribute.SEARCH_QUERY, context.getQuery());
        data.put(WebConstants.Attribute.HAS_MORE, context.isHasMore());
        data.put(WebConstants.Attribute.SEARCH_RESULTS_COUNT, context.getResultsCount());
        data.put(WebConstants.Attribute.PAGINATION_URL, context.getPaginationUrl());
        syncDataToRequest();
        return this;
    }

    /**
     * Add a message to the response context.
     */
    @SuppressWarnings("unchecked")
    public RespContext addMsg(Message msg) {
        // Get message list (if not exists, create a new one)
        List<Message> msgs = (List<Message>) data.computeIfAbsent(
                WebConstants.Attribute.MESSAGES,
                k -> new ArrayList<>()
        );
        msgs.add(msg);

        // Add back to data and sync to request
        data.put(WebConstants.Attribute.MESSAGES, msgs);
        syncDataToRequest();
        return this;
    }

    /**
     * Sync all data to request attributes for consistency.
     */
    private void syncDataToRequest() {
        data.forEach(req::setAttribute);
    }

    /**
     * Get a value.
     */
    public Object get(String key) {
        return data.get(key);
    }

    /**
     * Check if has any data.
     */
    public boolean hasData() {
        return !data.isEmpty();
    }
}
