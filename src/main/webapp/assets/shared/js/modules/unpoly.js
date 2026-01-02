import {Toast} from "./toast.js";
import {getFlashMessages} from "./utils.js";

export function initUnpoly() {
    if (typeof up === 'undefined') return;

    // Intercept requests to check responses for X-Up-Location header
    up.on('up:request:loaded', function (event) {
        const response = event.response;
        const upLocation = response?.xhr?.getResponseHeader('X-Up-Location');
        
        // Handle 200 OK with X-Up-Location (login success case)
        if (response.status === 200 && upLocation) {
            console.info('[Unpoly] Redirecting to:', upLocation);
            
            // Close all layers/modals
            if (up.layer) {
                up.layer.dismissOverlays(':all', { animation: false });
            }
            
            // Show success toast message
            Toast.success('Đăng nhập thành công!');
            
            // Navigate to the target page
            setTimeout(() => {
                window.location.href = upLocation;
            }, 300);
        }
    });
    
    // Handle [up-flashes] elements when they are inserted or updated
    up.compiler('[up-flashes]', function(element) {
        console.info('[Unpoly] Processing [up-flashes] element');
        getFlashMessages(element);
    });
    
    // Also handle flash messages after fragment updates
    up.on('up:fragment:inserted', function(event) {
        const fragment = event.fragment;
        if (!fragment) return;
        
        if (fragment.matches && (fragment.matches('[up-flashes]') || fragment.querySelector('[up-flashes]'))) {
            getFlashMessages(fragment);
        }
    });
}
