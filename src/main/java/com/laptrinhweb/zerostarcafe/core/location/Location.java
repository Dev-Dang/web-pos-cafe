package com.laptrinhweb.zerostarcafe.core.location;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents an approximate geographical location
 * using latitude and longitude.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/12/2025
 * @since 1.0.0
 */
public record Location(
        Double latitude,
        Double longitude
) {

    public boolean isValid() {
        return latitude != null && longitude != null
                && latitude != 0 && longitude != 0;
    }

}
