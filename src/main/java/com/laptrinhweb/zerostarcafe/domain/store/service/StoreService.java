package com.laptrinhweb.zerostarcafe.domain.store.service;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.location.GeoIpUtil;
import com.laptrinhweb.zerostarcafe.core.location.Location;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.store.dao.StoreDAO;
import com.laptrinhweb.zerostarcafe.domain.store.dao.StoreDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreStatus;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 *
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ... code here
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/12/2025
 * @since 1.0.0
 */
public final class StoreService {

    public List<Store> getAllActiveStores() {
        try (Connection conn = DBConnection.getConnection()) {
            StoreDAO storeDAO = new StoreDAOImpl(conn);
            return storeDAO.findAllByStatus(StoreStatus.OPEN);
        } catch (AppException | SQLException e) {
            LoggerUtil.error(StoreService.class, "Fail to get all active stores", e);
            return List.of();
        }
    }

    public Store getActiveStoreById(@NonNull Long storeId) {
        try (Connection conn = DBConnection.getConnection()) {
            StoreDAO storeDAO = new StoreDAOImpl(conn);

            Optional<Store> storeOpt = storeDAO.findById(storeId);
            if (storeOpt.isEmpty())
                return null;

            Store store = storeOpt.get();
            if (store.getStatus() == StoreStatus.OPEN)
                return store;

            return null;
        } catch (AppException | SQLException e) {
            LoggerUtil.error(StoreService.class,
                    "Fail to get active store by storeId=" + storeId, e);
            return null;
        }
    }

    public Store resolveStoreByReqIp(String reqIp) {
        Location loc = GeoIpUtil.lookup(reqIp);
        if (loc == null || !loc.isValid()) {
            LoggerUtil.warn(StoreService.class,
                    "GeoIP failed for IP=" + reqIp + ", using default.");
            return resolveDefaultStore();
        }

        Store nearest = findNearestStore(loc);
        if (nearest != null) {
            LoggerUtil.info(StoreService.class,
                    "Resolved by IP=" + reqIp + " -> Store=" + nearest.getId());
            return nearest;
        }

        return resolveDefaultStore();
    }

    private Store resolveDefaultStore() {
        return getActiveStoreById(StoreConstants.DEFAULT_STORE_ID);
    }

    public Store findNearestStore(Location clientLoc) {
        if (clientLoc == null || !clientLoc.isValid()) {
            return null;
        }

        try (Connection conn = DBConnection.getConnection()) {
            StoreDAO storeDAO = new StoreDAOImpl(conn);

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
            LoggerUtil.error(StoreService.class,
                    "Fail to find nearest store for loc=" + clientLoc, e);
            return null;
        }
    }
}