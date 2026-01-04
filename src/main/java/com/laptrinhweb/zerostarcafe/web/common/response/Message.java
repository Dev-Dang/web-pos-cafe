package com.laptrinhweb.zerostarcafe.web.common.response;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h2>Description:</h2>
 * <p>
 * Message factory - creates message instances.
 * Uses factory methods to enforce immutability.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 03/01/2026
 * @since 1.0.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class Message {

    public enum Type {success, error, warn, info, normal}

    private final Type type;
    private final String msgKey;

    public static void success(HttpServletRequest req, String msgKey) {
        RespContext.from(req).addMsg(new Message(Type.success, msgKey));
    }

    public static void error(HttpServletRequest req, String msgKey) {
        RespContext.from(req).addMsg(new Message(Type.error, msgKey));
    }

    public static void warn(HttpServletRequest req, String msgKey) {
        RespContext.from(req).addMsg(new Message(Type.warn, msgKey));
    }

    public static void info(HttpServletRequest req, String msgKey) {
        RespContext.from(req).addMsg(new Message(Type.info, msgKey));
    }

    public static void normal(HttpServletRequest req, String msgKey) {
        RespContext.from(req).addMsg(new Message(Type.normal, msgKey));
    }
}