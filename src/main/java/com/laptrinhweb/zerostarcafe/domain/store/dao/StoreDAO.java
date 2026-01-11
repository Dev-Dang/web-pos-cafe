package com.laptrinhweb.zerostarcafe.domain.store.dao;

import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides database access operations for the {@link Store} entity,
 * which represents a physical cafe location. This DAO exposes
 * JPA-style query method names while using plain JDBC underneath.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *     <li>Find a store by its primary key</li>
 *     <li>Find an active (open) store by storeId</li>
 *     <li>Find all stores by status</li>
 *     <li>Find all stores by status that have valid coordinates</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * StoreDAO storeDAO = new StoreDAOImpl(connection);
 *
 * // Validate a store from QR
 * Optional<Store> storeOpt = storeDAO.findByIdAndStatus(1L, "open");
 *
 * // Load all open stores with coordinates (for nearest-store logic)
 * List<Store> openStores = storeDAO
 *         .findAllByStatusAndLatitudeIsNotNullAndLongitudeIsNotNull("open");
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/12/2025
 * @since 1.0.0
 */
public interface StoreDAO {

    /**
     * Finds a store by its storeId.
     *
     * @param id the store storeId
     * @return an {@link Optional} containing the store if found
     * @throws SQLException if a database access error occurs
     */
    Optional<Store> findById(long id) throws SQLException;

    /**
     * Loads all stores from the database with specified status.
     */
    List<Store> findAllByStatus(StoreStatus status) throws SQLException;
}