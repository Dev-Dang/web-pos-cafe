package com.laptrinhweb.zerostarcafe.domain.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class LogDAO {

    public static void log(Long userId, String action, String targetType, Long targetId, String description) {
        new Thread(() -> {
            String sql = "INSERT INTO activity_logs (user_id, action, target_type, target_id, description) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = AdminDAO.connection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                if (userId != null) ps.setLong(1, userId);
                else ps.setNull(1, java.sql.Types.BIGINT);
                ps.setString(2, action);
                ps.setString(3, targetType);
                if (targetId != null) ps.setLong(4, targetId);
                else ps.setNull(4, java.sql.Types.BIGINT);
                ps.setString(5, description);

                ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}