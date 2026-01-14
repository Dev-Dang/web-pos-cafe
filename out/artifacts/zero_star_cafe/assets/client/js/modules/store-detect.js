import * as Cookie from './cookie.js';
import {StoreWebConstants} from './web-constants.js';

const GEO_DENIED_KEY = 'zrc_geo_denied_ts';
const DENIED_COOLDOWN_MS = 7 * 24 * 60 * 60 * 1000; // 7 days

function isGeoDeniedRecently() {
    const value = localStorage.getItem(GEO_DENIED_KEY);
    if (!value) return false;
    return (Date.now() - parseInt(value, 10)) < DENIED_COOLDOWN_MS;
}

function sendLocationToServer(lat, lon) {
    window.location.href = `${StoreWebConstants.Endpoint.STORE_DETECT}?lat=${lat}&lon=${lon}`;
}

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
            console.warn('Geolocation failed:', err.message);
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