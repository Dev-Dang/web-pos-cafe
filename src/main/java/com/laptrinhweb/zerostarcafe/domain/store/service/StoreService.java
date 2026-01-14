package com.laptrinhweb.zerostarcafe.domain.store.service;

import com.laptrinhweb.zerostarcafe.core.context.LocaleContext;
import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.location.GeoIpUtil;
import com.laptrinhweb.zerostarcafe.core.location.Location;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.store.dao.StoreDAO;
import com.laptrinhweb.zerostarcafe.domain.store.dao.StoreDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Service layer for store-related business logic. Handles store retrieval,
 * location resolution, and GeoIP-based store detection.
 * Language is automatically determined from {@link LocaleContext}.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * StoreService storeService = new StoreService();
 *
 * // Get all active stores (locale from LocaleContext)
 * List<Store> stores = storeService.getAllActiveStores();
 *
 * // Get specific store (locale from LocaleContext)
 * Store store = storeService.getActiveStoreById(1L);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.2.0
 * @lastModified 05/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class StoreService {

    private static final StoreService INSTANCE = new StoreService();
    private final StoreDAO storeDAO = new StoreDAOImpl();

    public static StoreService getInstance() {
        return INSTANCE;
    }

    /**
     * Retrieves all active (open) stores with localized content.
     *
     * @return list of active stores with localized name and address
     */
    public List<Store> getAllActiveStores() {
        try {
            return storeDAO.findAllByStatus(StoreStatus.OPEN);
        } catch (SQLException e) {
            throw new AppException("Fail to get all active stores", e);
        }
    }

    /**
     * Retrieves an active store by ID with localized content.
     *
     * @param storeId the store ID
     * @return the store if found and active, null otherwise
     */
    public Store getActiveStoreById(@NonNull Long storeId) {
        try {
            Optional<Store> storeOpt = storeDAO.findById(storeId);
            if (storeOpt.isEmpty())
                return null;

            Store store = storeOpt.get();
            if (store.getStatus() == StoreStatus.OPEN)
                return store;

            return null;
        } catch (SQLException e) {
            throw new AppException("Fail to get active store by storeId=" + storeId, e);
        }
    }

    /**
     * Resolves store based on client's IP address using GeoIP lookup.
     * Falls back to default store if GeoIP fails.
     *
     * @param reqIp client's IP address
     * @return the nearest store or default store
     */
    public Store resolveStoreByReqIp(@NonNull String reqIp) {
        Location loc = GeoIpUtil.lookup(reqIp);
        if (loc == null || !loc.isValid()) {
            LoggerUtil.warn(StoreService.class,
                    "GeoIP failed for IP=" + reqIp + ", using default.");
            return resolveDefaultStore();
        }

        try {
            Store nearest = findNearestStore(loc);
            return nearest != null ? nearest : resolveDefaultStore();
        } catch (AppException e) {
            LoggerUtil.error(StoreService.class,
                    "Find store by request ip failed -> fallback default", e);
            return resolveDefaultStore();
        }
    }

    /**
     * Returns the default store with localized content.
     *
     * @return the default store
     */
    private Store resolveDefaultStore() {
        return getActiveStoreById(StoreConstants.DEFAULT_STORE_ID);
    }

    /**
     * Finds the nearest open store to the given client location.
     *
     * @param clientLoc client's geographic location
     * @return the nearest store, or null if none found
     * @throws AppException if a data access error occurs
     */
    private Store findNearestStore(@NonNull Location clientLoc) {
        if (!clientLoc.isValid()) {
            return null;
        }

        try {
            List<Store> stores = storeDAO.findAllByStatus(StoreStatus.OPEN);
            if (stores.isEmpty())
                return null;

            Store nearest = null;
            double minDistance = Double.MAX_VALUE;

            for (Store s : stores) {
                if (s.getStatus() != StoreStatus.OPEN)
                    continue;

                Location storeLoc = new Location(
                        s.getLatitude(),
                        s.getLongitude()
                );

                if (!storeLoc.isValid())
                    continue;

                double distance = GeoIpUtil.distanceKm(clientLoc, storeLoc);

                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = s;
                }
            }
            return nearest;

        } catch (SQLException e) {
            throw new AppException("Fail to get nearest store for loc=" + clientLoc, e);
        }
    }

    /**
     * Finds the nearest open store to the given client location.
     *
     * @param clientLoc client's geographic location
     * @return the nearest store or default store if error occurs
     */
    public Store resolveStoreByLocation(@NonNull Location clientLoc) {
        try {
            Store nearest = findNearestStore(clientLoc);
            return nearest != null ? nearest : resolveDefaultStore();
        } catch (AppException e) {
            LoggerUtil.error(StoreService.class,
                    "Find store by location failed -> fallback default", e);
            return resolveDefaultStore();
        }
    }
}