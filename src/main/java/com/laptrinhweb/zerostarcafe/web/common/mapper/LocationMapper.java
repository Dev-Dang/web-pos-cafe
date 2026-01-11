package com.laptrinhweb.zerostarcafe.web.common.mapper;

import com.laptrinhweb.zerostarcafe.core.location.Location;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <h2>Description:</h2>
 * <p>
 * Map the HttpServletRequest into a Location object.
 * It extracts the latitude and longitude from the request.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 13/12/2025
 * @since 1.0.0
 */
public final class LocationMapper {

    private LocationMapper() {
    }

    /**
     * Creates a Location from request latitude and longitude parameters.
     *
     * @param req the HTTP request
     * @return a valid Location, or null if inputs are missing or invalid
     */
    public static Location from(HttpServletRequest req) {
        String latParam = req.getParameter(StoreConstants.Param.LATITUDE);
        String lonParam = req.getParameter(StoreConstants.Param.LONGITUDE);

        Double lat = parseDouble(latParam);
        Double lon = parseDouble(lonParam);

        if (lat == null || lon == null) {
            return null;
        }

        Location loc = new Location(lat, lon);
        return loc.isValid() ? loc : null;
    }

    private static Double parseDouble(String value) {
        if (value == null || value.isBlank())
            return null;

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
