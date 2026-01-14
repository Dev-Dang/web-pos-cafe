package com.laptrinhweb.zerostarcafe.web.client.mapper;

import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>Description:</h2>
 * <p>
 * Maps HttpServletRequest into a StoreContext object using RequestUtils.
 * It extracts storeId and tableId from the request parameters.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 05/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StoreCtxMapper {

    /**
     * Creates a StoreContext from request parameters using RequestUtils.
     *
     * @param req the HTTP request
     * @return a StoreContext, or null if storeId is missing or invalid
     */
    public static StoreContext toStoreContext(HttpServletRequest req) {
        Long storeId = RequestUtils.getLongParam(req, StoreConstants.Param.STORE_ID);
        Long tableId = RequestUtils.getLongParam(req, StoreConstants.Param.TABLE_ID);

        if (storeId == null || storeId <= 0) {
            return null;
        }

        // Ensure positive tableId or null
        if (tableId != null && tableId <= 0) {
            tableId = null;
        }

        return new StoreContext(storeId, tableId);
    }
}
