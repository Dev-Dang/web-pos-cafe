window.__APP_MODE__ = "development";

// Enable Unpoly logging
if (typeof up !== 'undefined') {
    up.log.enable();
}

import {initUnpoly} from "./modules/unpoly.js";
import {getFlashMessages} from "./modules/utils.js";

// Development logging for CSRF token
if (window.__APP_MODE__ === 'development') {
    up.on('up:request:load', function (event) {
        if (event.request.method !== 'GET') {
            const hasToken = event.request.headers['X-CSRF-Token'] ? '✓' : '✗';
            console.log(`[CSRF] ${event.request.method} ${event.request.url} - Token: ${hasToken}`);
        }
    });
}

// ==== Global Unpoly workflow ====
initUnpoly();

// ==== Flash from SSR (initial page load) ====
getFlashMessages();

