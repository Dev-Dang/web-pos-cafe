package com.laptrinhweb.zerostarcafe.web.common.response;

import com.laptrinhweb.zerostarcafe.web.common.utils.WebConstants;
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
                WebConstants.Request.RESPONSE_CONTEXT);

        if (ctx == null) {
            ctx = new RespContext(req);
            req.setAttribute(WebConstants.Request.RESPONSE_CONTEXT, ctx);
        }
        return ctx;
    }

    /**
     * Add a message to the response context.
     */
    @SuppressWarnings("unchecked")
    public RespContext addMsg(Message msg) {
        // Get message list (if not exists, create a new one)
        List<Message> msgs = (List<Message>) data.computeIfAbsent(
                WebConstants.Request.MESSAGES,
                k -> new ArrayList<>()
        );
        msgs.add(msg);

        // Return updated context
        req.setAttribute(WebConstants.Request.MESSAGES, msgs);
        return this;
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
