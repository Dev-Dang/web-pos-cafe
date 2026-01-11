package com.laptrinhweb.zerostarcafe.core.location;

import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility for resolving an approximate location from an IP address
 * using the MaxMind GeoIP2/GeoLite2 City database.
 * </p>
 *
 * <h2>Usage:</h2>
 * <pre>{@code
 * IpLocation loc = GeoIpUtil.lookup("1.2.3.4");
 * if (loc != null) {
 *     System.out.println(loc.city());
 *     System.out.println(loc.latitude() + ", " + loc.longitude());
 * }
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/12/2025
 * @since 1.0.0
 */
public final class GeoIpUtil {

    private static final DatabaseReader DB_READER;

    private static final String DB_RESOURCE_PATH = "/geo/GeoLite2-City.mmdb";
    private static final double EARTH_RADIUS_KM = 6371.0;

    static {
        DatabaseReader reader = null;

        try (InputStream is = GeoIpUtil.class.getResourceAsStream(DB_RESOURCE_PATH)) {
            if (is == null) {
                LoggerUtil.warn(GeoIpUtil.class,
                        "GeoIP database not found on classpath: " + DB_RESOURCE_PATH);
            } else {
                reader = new DatabaseReader.Builder(is).build();
                LoggerUtil.info(GeoIpUtil.class,
                        "GeoIP database loaded from: " + DB_RESOURCE_PATH);
            }
        } catch (IOException e) {
            LoggerUtil.error(GeoIpUtil.class,
                    "Failed to initialize GeoIP DatabaseReader: " + e.getMessage(), e);
        }

        DB_READER = reader;
    }

    private GeoIpUtil() {
    }

    /**
     * Looks up an IP address and returns an approximate {@link Location}.
     *
     * @param ip IPv4/IPv6 address as string
     * @return {@link Location} or {@code null} if lookup fails
     */
    public static Location lookup(String ip) {
        if (DB_READER == null) {
            return null;
        }
        if (ip == null || ip.isBlank()) {
            return null;
        }

        try {
            InetAddress inetAddress = InetAddress.getByName(ip.trim());
            CityResponse response = DB_READER.city(inetAddress);

            Double lat = response.location().latitude();
            Double lon = response.location().longitude();

            return new Location(lat, lon);

        } catch (GeoIp2Exception | IOException e) {
            LoggerUtil.warn(GeoIpUtil.class,
                    "GeoIP lookup failed for ip=" + ip + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Computes the great-circle distance between two points on Earth using the Haversine formula.
     *
     * @return distance in kilometers
     */
    public static double distanceKm(Location loc1, Location loc2) {
        if (loc1 == null || !loc1.isValid() || loc2 == null || !loc2.isValid()) {
            return Double.MAX_VALUE;
        }

        double dLat = Math.toRadians(loc2.latitude() - loc1.latitude());
        double dLon = Math.toRadians(loc2.longitude() - loc1.longitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(loc1.latitude()))
                * Math.cos(Math.toRadians(loc2.latitude()))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}