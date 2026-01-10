package com.laptrinhweb.zerostarcafe.core.mail;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletContext;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Simple SMTP mail sender configured via web.xml context params.
 */
public final class SmtpMailService {

    private static final String CTX_KEY = "smtpMailService";

    private final Session session;
    private final String fromEmail;
    private final String fromName;

    private SmtpMailService(Session session, String fromEmail, String fromName) {
        this.session = session;
        this.fromEmail = fromEmail;
        this.fromName = fromName;
    }

    public static SmtpMailService from(ServletContext ctx) {
        Object cached = ctx.getAttribute(CTX_KEY);
        if (cached instanceof SmtpMailService service) {
            return service;
        }

        synchronized (SmtpMailService.class) {
            cached = ctx.getAttribute(CTX_KEY);
            if (cached instanceof SmtpMailService service) {
                return service;
            }

            String host = requiredParam(ctx, "SMTP_HOST");
            String port = requiredParam(ctx, "SMTP_PORT");
            String username = requiredParam(ctx, "SMTP_USERNAME");
            String password = requiredParam(ctx, "SMTP_PASSWORD");
            String fromEmail = requiredParam(ctx, "SMTP_FROM_EMAIL");
            String fromName = ctx.getInitParameter("SMTP_FROM_NAME");

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            SmtpMailService service = new SmtpMailService(session, fromEmail, fromName);
            ctx.setAttribute(CTX_KEY, service);
            return service;
        }
    }

    public void sendHtml(String toEmail, String subject, String html) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(subject, StandardCharsets.UTF_8.name());

        msg.setFrom(buildFromAddress());

        msg.setContent(html, "text/html; charset=UTF-8");
        Transport.send(msg);
    }

    private static String requiredParam(ServletContext ctx, String name) {
        String value = ctx.getInitParameter(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required context-param: " + name);
        }
        return value.trim();
    }

    private InternetAddress buildFromAddress() throws MessagingException {
        if (fromName == null || fromName.isBlank()) {
            return new InternetAddress(fromEmail);
        }
        try {
            return new InternetAddress(fromEmail, fromName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Invalid sender name encoding.", e);
        }
    }
}
