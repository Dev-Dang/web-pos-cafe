/**
 * ------------------------------------------------------------
 * Module: Store Detection
 * ------------------------------------------------------------
 * @description
 * Detects user location and redirects to store-detect endpoint
 * when no store cookie is present. Respects a denial cooldown.
 *
 * @example
 * import { initStoreDetection } from './store-detect.js';
 * initStoreDetection();
 *
 * @version 1.0.1
 * @since 1.0.0
 * @lastModified 12/26/2025
 * @module store-detect
 * @author Dang Van Trung
 */
import * as Cookie from './cookie.js';
import {StoreWebConstants} from './web-constants.js';

const GEO_DENIED_KEY = 'zrc_geo_denied_ts';
const DENIED_COOLDOWN_MS = 7 * 24 * 60 * 60 * 1000; // 7 days

/** Check if geolocation was denied within the cooldown window. */
function isGeoDeniedRecently() {
    const value = localStorage.getItem(GEO_DENIED_KEY);
    if (!value) return false;
    return (Date.now() - parseInt(value, 10)) < DENIED_COOLDOWN_MS;
}

/** Redirect to server endpoint with latitude/longitude query params. */
function sendLocationToServer(lat, lon) {
    window.location.href = `${StoreWebConstants.Endpoint.STORE_DETECT}?lat=${lat}&lon=${lon}`;
}

/**
 * Initialize store detection: checks cookies and geolocation, handles
 * successful position and user denials with cooldown persistence.
 */
export function initStoreDetection() {
    if (Cookie.get(StoreWebConstants.Cookie.LAST_STORE_ID))
        return;

    if (!navigator.geolocation || isGeoDeniedRecently())
        return;

    navigator.geolocation.getCurrentPosition(
        (pos) => {
            const {latitude, longitude} = pos.coords;
            sendLocationToServer(latitude, longitude);
        },

        (err) => {
            if (err.code === 1) {
                localStorage.setItem(GEO_DENIED_KEY, Date.now().toString());
            }
        },

        {
            enableHighAccuracy: false,
            timeout: 15000,
            maximumAge: 600000
        }
    );
}
