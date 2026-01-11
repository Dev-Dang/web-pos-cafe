package com.laptrinhweb.zerostarcafe.domain.user_role;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides database access operations for the {@link UserStoreRole} entity,
 * which represents the role assigned to a user within a specific store.
 * This DAO supports creating, updating, retrieving, and deleting
 * user–store role mappings in the {@code user_store_roles} table.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *     <li>Create or update a user-to-store role record</li>
 *     <li>Retrieve all store-role assignments for a user</li>
 *     <li>Find a specific user–store role mapping</li>
 *     <li>Remove a user's role assignment for a store</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * UserStoreRoleDAO dao = new UserStoreRoleDAOImpl(connection);
 *
 * // Assign role to user at a store
 * dao.save(new UserStoreRole(id, storeId, "staff"));
 *
 * // Load all roles for user
 * List<UserStoreRole> roles = dao.findByUserId(id);
 *
 * // Delete a specific assignment
 * dao.delete(id, storeId);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 11/11/2025
 * @since 1.0.0
 */
public interface UserStoreRoleDAO {

    /**
     * Inserts or updates a user-store-role record.
     * <p>
     * If a record with the same {@code (user_id, store_id)} already exists,
     * it updates the {@code role_code}.
     * </p>
     *
     * @param usr the {@link UserStoreRole} entity to insert or update
     * @return {@code true} if operation succeeded
     * @throws SQLException if a database access error occurs
     */
    boolean save(UserStoreRole usr) throws SQLException;

    /**
     * Finds all store-role assignments belonging to a specific user.
     *
     * @param userId the user's ID
     * @return a list of {@link UserStoreRole} objects
     * @throws SQLException if a database access error occurs
     */
    List<UserStoreRole> findByUserId(long userId) throws SQLException;

    /**
     * /**
     * Finds a specific user-store-role record by user ID and store ID.
     *
     * @param userId  the user's ID
     * @param storeId the store's ID
     * @return an {@link Optional} containing the record if found
     * @throws SQLException if a database access error occurs
     */
    Optional<UserStoreRole> findByUserAndStore(long userId, long storeId) throws SQLException;

    /**
     * Deletes a specific user-store-role record.
     *
     * @param userId  the user's ID
     * @param storeId the store's ID
     * @return {@code true} if deletion succeeded
     * @throws SQLException if a database access error occurs
     */
    boolean delete(long userId, long storeId) throws SQLException;

}
