package com.laptrinhweb.zerostarcafe.domain.store.dao;

import com.laptrinhweb.zerostarcafe.core.context.LocaleContext;
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
 * // Get store with current locale (from LocaleContext)
 * Optional<Store> storeOpt = storeDAO.findById(1L);
 *
 * // Load all open stores with current locale
 * List<Store> openStores = storeDAO.findAllByStatus(StoreStatus.OPEN);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.1.0
 * @lastModified 05/01/2026
 * @since 1.0.0
 */
public interface StoreDAO {

    /**
     * Finds a store by its storeId with localized content based on current locale.
     *
     * @param id the store storeId
     * @return an {@link Optional} containing the store if found
     * @throws SQLException if a database access error occurs
     */
    Optional<Store> findById(long id) throws SQLException;

    /**
     * Loads all stores from the database with specified status and localized content.
     * Language is determined from {@link LocaleContext}.
     *
     * @param status the store status to filter by
     * @return list of stores with localized name and address
     * @throws SQLException if a database access error occurs
     */
    List<Store> findAllByStatus(StoreStatus status) throws SQLException;
}