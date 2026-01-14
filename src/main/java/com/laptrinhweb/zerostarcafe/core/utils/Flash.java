package com.laptrinhweb.zerostarcafe.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides "flash scope" utility for passing short-lived data
 * (e.g., messages, form errors) between requests. Data is temporarily
 * stored in the HTTP session and automatically cleared on the next request.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * Flash.from(request)
 *      .success("message.register_success")
 *      .formResponse(formData, formErrors)
 *      .send();
 * resp.sendRedirect(req.getHeader("Referer"));
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/11/2025
 * @since 1.0.0
 */
public final class Flash {
    public static final String FLASH_KEY = "flash";
    private final HttpServletRequest request;
    private final Map<String, Object> flashBag = new LinkedHashMap<>();

    // ==== Constructors ====
    public Flash(HttpServletRequest request) {
        this.request = request;
    }

    // ==== Message Types ====
    public enum MsgType {success, error, warn, info, normal}

    public record Message(MsgType type, String msgKey) {
    }

    // ==== Private Helpers ====
    @SuppressWarnings("unchecked")
    private Flash addMessage(Message msg) {
        List<Message> messages = (List<Message>) flashBag.get("messages");
        if (messages == null) {
            messages = new ArrayList<>();
            flashBag.put("messages", messages);
        }
        messages.add(msg);
        return this;
    }

    // ==== Public API ====

    public Flash success(String msgKey) {
        return addMessage(new Message(MsgType.success, msgKey));
    }

    public Flash info(String msgKey) {
        return addMessage(new Message(MsgType.info, msgKey));
    }

    public Flash warn(String msgKey) {
        return addMessage(new Message(MsgType.warn, msgKey));
    }

    public Flash error(String msgKey) {
        return addMessage(new Message(MsgType.error, msgKey));
    }

    /**
     * Stores form-related data and validation errors for the next view rendering.
     *
     * @param formData   key-value pairs to refill form fields
     * @param formErrors key-message pairs describing validation errors
     * @return this Flash instance
     */
    public Flash formResponse(Map<String, String> formData, Map<String, String> formErrors) {
        if (formData != null && !formData.isEmpty())
            flashBag.put("formData", new LinkedHashMap<>(formData));
        if (formErrors != null && !formErrors.isEmpty())
            flashBag.put("formErrors", new LinkedHashMap<>(formErrors));
        return this;
    }

    /**
     * Sets a custom key-value pair into the flash bag.
     *
     * @param key   unique key name
     * @param value arbitrary value to store
     * @return this Flash instance
     */
    public Flash set(String key, Object value) {
        flashBag.put(key, value);
        return this;
    }

    /**
     * Commits all flash data into the HTTP session for one-time retrieval.
     * This method merges new and existing flash messages safely
     * and clears the in-memory bag afterward.
     */
    public void send() {
        HttpSession session = request.getSession();
        synchronized (session) {
            Map<String, Object> sessionBag = getOrCreateSessionBag(session);
            mergeMessages(sessionBag, flashBag);
            mergeOtherKeys(sessionBag, flashBag);
            session.setAttribute(FLASH_KEY, sessionBag);
        }
        flashBag.clear();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getOrCreateSessionBag(HttpSession session) {
        Object existingBag = session.getAttribute(FLASH_KEY);
        if (existingBag instanceof Map<?, ?> map)
            return new LinkedHashMap<>((Map<String, Object>) map);
        return new LinkedHashMap<>();
    }

    @SuppressWarnings("unchecked")
    private static void mergeMessages(Map<String, Object> sessionBag, Map<String, Object> flashBag) {
        List<Message> incoming = (List<Message>) flashBag.get("messages");
        if (incoming == null || incoming.isEmpty()) return;

        // Get existing messages and merge
        Object existingMsg = sessionBag.get("messages");
        List<Message> merged;
        if (existingMsg instanceof List<?> exist) {
            merged = new ArrayList<>(exist.size() + incoming.size());
            for (Object o : exist) merged.add((Message) o);
            merged.addAll(incoming);
        } else {
            merged = new ArrayList<>(incoming);
        }

        // Limit to 5 messages
        if (merged.size() > 5)
            merged = merged.subList(merged.size() - 5, merged.size());

        sessionBag.put("messages", merged);
        flashBag.remove("messages");
    }

    private static void mergeOtherKeys(Map<String, Object> sessionBag, Map<String, Object> flashBag) {
        for (Map.Entry<String, Object> entry : flashBag.entrySet()) {
            if ("messages".equals(entry.getKey())) continue;
            sessionBag.put(entry.getKey(), entry.getValue());
        }
    }
}
