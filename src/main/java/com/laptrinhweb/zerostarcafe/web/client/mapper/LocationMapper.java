package com.laptrinhweb.zerostarcafe.web.client.mapper;

import com.laptrinhweb.zerostarcafe.core.location.Location;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>Description:</h2>
 * <p>
 * Map the HttpServletRequest into a Location object using RequestUtils.
 * It extracts the latitude and longitude from the request.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 05/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LocationMapper {

    /**
     * Creates a Location from request latitude and longitude parameters using RequestUtils.
     *
     * @param req the HTTP request
     * @return a valid Location, or null if inputs are missing or invalid
     */
    public static Location from(HttpServletRequest req) {
        Double lat = RequestUtils.getDoubleParam(req, StoreConstants.Param.LATITUDE);
        Double lon = RequestUtils.getDoubleParam(req, StoreConstants.Param.LONGITUDE);

        if (lat == null || lon == null) {
            return null;
        }

        Location loc = new Location(lat, lon);
        return loc.isValid() ? loc : null;
    }
}
