package com.laptrinhweb.zerostarcafe.domain.user.dao;

import com.laptrinhweb.zerostarcafe.domain.user.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides database access methods for the {@link User} entity mapped to
 * the {@code users} table. This interface defines operations for creating,
 * retrieving, checking existence, listing, and deleting user records.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *     <li>Save new or updated user information</li>
 *     <li>Find users by ID or username</li>
 *     <li>Retrieve all users</li>
 *     <li>Check if a username or email already exists</li>
 *     <li>Delete users by ID</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * UserDAO dao = new UserDAOImpl(connection);
 *
 * if (!dao.existsByEmail("john@example.com")) {
 *     dao.save(new User("john", "john@example.com"));
 * }
 *
 * Optional<User> user = dao.findByUsername("john");
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 16/12/2025
 * @since 1.0.0
 */
public interface UserDAO {

    /**
     * Inserts or updates a {@link User} record.
     *
     * @param user the {@link User} entity to persist
     * @return the saved {@link User} with generated ID if new
     * @throws SQLException if a database access error occurs
     */
    User save(User user) throws SQLException;

    /**
     * Finds a user by ID.
     *
     * @param id the user's unique identifier
     * @return an {@link Optional} containing the user if found
     * @throws SQLException if a database access error occurs
     */
    Optional<User> findById(long id) throws SQLException;

    /**
     * Finds a user by their unique username.
     *
     * @param username the username to search
     * @return an {@link Optional} containing the user if found
     * @throws SQLException if a database access error occurs
     */
    Optional<User> findByUsername(String username) throws SQLException;

    /**
     * Finds a user by their unique email.
     *
     * @param email the email to search
     * @return an {@link Optional} containing the user if found
     * @throws SQLException if a database access error occurs
     */
    Optional<User> findByEmail(String email) throws SQLException;

    /**
     * Retrieves all users ordered by creation date (descending).
     *
     * @return list of {@link User} objects
     * @throws SQLException if a database access error occurs
     */
    List<User> findAll() throws SQLException;

    /**
     * Checks if a given username already exists in the database.
     *
     * @param username the username to check
     * @return {@code true} if found, otherwise {@code false}
     * @throws SQLException if a database access error occurs
     */
    boolean existsByUsername(String username) throws SQLException;

    /**
     * Checks if a given email address already exists in the database.
     *
     * @param email the email address to check
     * @return {@code true} if found, otherwise {@code false}
     * @throws SQLException if a database access error occurs
     */
    boolean existsByEmail(String email) throws SQLException;

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the user ID
     * @return {@code true} if a record was deleted, otherwise {@code false}
     * @throws SQLException if a database access error occurs
     */
    boolean deleteById(long id) throws SQLException;
}