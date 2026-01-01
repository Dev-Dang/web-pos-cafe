package com.laptrinhweb.zerostarcafe.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple message system for Unpoly.
 * Creates flash messages that are rendered directly to the request
 * for Unpoly to swap into the DOM.
 *
 * For Unpoly: Messages are set in request scope and rendered in _flash-data.jsp
 * No session storage needed - Unpoly handles everything in one response cycle.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 01/01/2026
 * @since 1.0.0
 */
public class Message {

    public enum Type {success, error, warn, info, normal}

    public record Data(Type type, String msgKey) {}

    /**
     * Adds a single message to the request
     * @param req HttpServletRequest
     * @param type Message type
     * @param msgKey Translation key for the message
     */
    public static void add(HttpServletRequest req, Type type, String msgKey) {
        List<Data> messages = getOrCreateList(req);
        messages.add(new Data(type, msgKey));
    }

    /**
     * Adds a success message
     */
    public static void success(HttpServletRequest req, String msgKey) {
        add(req, Type.success, msgKey);
    }

    /**
     * Adds an error message
     */
    public static void error(HttpServletRequest req, String msgKey) {
        add(req, Type.error, msgKey);
    }

    /**
     * Adds an info message
     */
    public static void info(HttpServletRequest req, String msgKey) {
        add(req, Type.info, msgKey);
    }

    /**
     * Adds a warning message
     */
    public static void warn(HttpServletRequest req, String msgKey) {
        add(req, Type.warn, msgKey);
    }

    /**
     * Gets the messages list, creating it if needed
     */
    @SuppressWarnings("unchecked")
    private static List<Data> getOrCreateList(HttpServletRequest req) {
        List<Data> messages = (List<Data>) req.getAttribute("messages");
        if (messages == null) {
            messages = new ArrayList<>();
            req.setAttribute("messages", messages);
        }
        return messages;
    }
}
