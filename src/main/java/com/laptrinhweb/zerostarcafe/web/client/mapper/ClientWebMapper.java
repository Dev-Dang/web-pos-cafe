package com.laptrinhweb.zerostarcafe.web.client.mapper;

import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <h2>Description:</h2>
 * <p>
 * Maps HttpServletRequest into a StoreContext object.
 * It extracts storeId and tableId from the request.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 13/12/2025
 * @since 1.0.0
 */
public final class ClientWebMapper {

    /**
     * Creates a StoreContext from request parameters.
     *
     * @param req the HTTP request
     * @return a StoreContext, or null if storeId is missing or invalid
     */
    public static StoreContext toStoreContext(HttpServletRequest req) {

        String rawStoreId = req.getParameter(StoreConstants.Param.STORE_ID);
        String rawTableId = req.getParameter(StoreConstants.Param.TABLE_ID);

        Long storeId = parseLong(rawStoreId);
        Long tableId = parseLong(rawTableId);

        if (storeId == null)
            return null;

        return new StoreContext(storeId, tableId);
    }

    private static Long parseLong(String rawId) {
        if (rawId == null || rawId.isBlank())
            return null;

        try {
            long id = Long.parseLong(rawId.trim());
            return id > 0 ? id : null;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

}
