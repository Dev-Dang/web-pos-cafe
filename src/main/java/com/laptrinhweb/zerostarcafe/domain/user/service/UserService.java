package com.laptrinhweb.zerostarcafe.domain.user.service;

import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.domain.user.dao.UserDAO;
import com.laptrinhweb.zerostarcafe.domain.user.dao.UserDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.user.model.User;
import com.laptrinhweb.zerostarcafe.domain.user.model.UserStatus;
import com.laptrinhweb.zerostarcafe.domain.user_role.UserStoreRole;
import com.laptrinhweb.zerostarcafe.domain.user_role.UserStoreRoleDAO;
import com.laptrinhweb.zerostarcafe.domain.user_role.UserStoreRoleDAOImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides user-related operations.
 * It supports saving users, checking duplicates,
 * and loading user roles.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * UserService service = new UserService(conn);
 * User user = service.getActiveBy("username");
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 29/11/2025
 * @since 1.0.0
 */
public class UserService {

    private final UserDAO userDAO;
    private final UserStoreRoleDAO roleDAO;

    public UserService(Connection conn) {
        this.userDAO = new UserDAOImpl(conn);
        this.roleDAO = new UserStoreRoleDAOImpl(conn);
    }

    /**
     * Saves a user to the database.
     *
     * @param user the user to save
     * @return the saved user with ID
     * @throws AppException if a SQL error occurs
     */
    public User save(User user) {
        try {
            return userDAO.save(user);
        } catch (SQLException e) {
            throw new AppException("Fail to insert user=" + user.getUsername(), e);
        }
    }

    /**
     * Checks if a user with the given email already exists.
     *
     * @param email the email to check
     * @return true if a user with this email exists
     * @throws AppException if a SQL error occurs
     */
    public boolean existsByEmail(String email) {
        try {
            return userDAO.existsByEmail(email);
        } catch (SQLException e) {
            throw new AppException("Fail to check user exists by email=" + email, e);
        }
    }

    /**
     * Checks if a user with the given username already exists.
     *
     * @param username the username to check
     * @return true if a user with this username exists
     * @throws AppException if a SQL error occurs
     */
    public boolean existsByUsername(String username) {
        try {
            return userDAO.existsByUsername(username);
        } catch (SQLException e) {
            throw new AppException("Fail to check user exists by username=" + username, e);
        }
    }

    /**
     * Loads an active user by username.
     * Only users with ACTIVE status are returned.
     *
     * @param username the username to search
     * @return the active user, or null if not found or not active
     * @throws AppException if a SQL error occurs
     */
    public User getActiveByUsername(String username) {
        try {
            Optional<User> userOpt = userDAO.findByUsername(username);
            if (userOpt.isEmpty())
                return null;

            User user = userOpt.get();

            if (user.getStatus() != UserStatus.ACTIVE)
                return null;

            return user;
        } catch (SQLException e) {
            throw new AppException("Fail to get active user by username=" + username, e);
        }
    }

    /**
     * Loads an active user by ID.
     * Only users with ACTIVE status are returned.
     *
     * @param id the user ID
     * @return the active user, or null if not found or not active
     * @throws AppException if a SQL error occurs
     */
    public User getActiveById(Long id) {
        try {
            Optional<User> userOpt = userDAO.findById(id);
            if (userOpt.isEmpty())
                return null;

            User user = userOpt.get();

            if (user.getStatus() != UserStatus.ACTIVE)
                return null;

            return user;
        } catch (SQLException e) {
            throw new AppException("Fail to get active user with id=" + id, e);
        }
    }

    /**
     * Loads all roles of a user.
     * Only ACTIVE users can have roles returned.
     *
     * @param user the user to load roles for
     * @return a list of roles, or an empty list if user is null or not active
     * @throws AppException if a SQL error occurs
     */
    public List<UserStoreRole> getRolesOf(User user) {
        if (user == null || user.getStatus() != UserStatus.ACTIVE) {
            return List.of();
        }

        try {
            return roleDAO.findByUserId(user.getId());
        } catch (SQLException e) {
            throw new AppException("Fail to get list roles of user=" + user.getUsername(), e);
        }
    }
}
