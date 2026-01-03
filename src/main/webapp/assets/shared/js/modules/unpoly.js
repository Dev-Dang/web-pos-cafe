import {getFlashMessages} from "./utils.js";

export function initUnpoly() {
    if (typeof up === 'undefined') return;

    // Intercept requests to check responses for X-Up-Location header
    up.on('up:request:loaded', function (event) {
        const response = event.response;
        const upLocation = response?.xhr?.getResponseHeader('X-Up-Location');

        // Handle 302 redirects with X-Up-Location header
        if (response.status === 303 && upLocation) {
            // Close all layers/modals
            if (up.layer) {
                up.layer.dismissOverlays(':all', {animation: false});
            }

            // Navigate to the target page
            setTimeout(() => {
                window.location.href = upLocation;
            }, 300);
        }
    });

    // Handle [up-flashes] elements when they are inserted or updated
    up.compiler('[up-flashes]', function (element) {
        getFlashMessages(element);
    });

    // Adding csrf token to all requests
    up.on('up:request:load', function (event) {
        const token = document.querySelector('meta[name="csrf-token"]')?.content;
        if (token && event.request.method !== 'GET') {
            event.request.headers['X-CSRF-Token'] = token;
        }
    });
}
