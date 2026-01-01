import {getFlashMessages} from "./utils.js";
import {Toast} from "./toast.js";

// Track processed responses to avoid double toasts
const processedResponses = new Set();

export function initUnpoly() {
    if (typeof up === 'undefined') return;

    console.log('[Unpoly] Initializing Unpoly handlers...');

    // Intercept requests to check responses for X-Up-Location header
    up.on('up:request:loaded', function (event) {
        const response = event.response;
        const upLocation = response?.xhr?.getResponseHeader('X-Up-Location');
        
        console.log('[Unpoly] Request loaded:', response.status, 'X-Up-Location:', upLocation);
        
        // Handle 200 OK with X-Up-Location (login success case)
        if (response.status === 200 && upLocation) {
            console.log('[Unpoly] Handling redirect to:', upLocation);
            
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
    
    // Extract flash messages from error responses (422, 500, etc)
    up.on('up:fragment:loaded', function (event) {
        const response = event.response;
        
        console.log('[Unpoly] Fragment loaded from response:', response.status);
        
        // Only process error responses
        if (response.status < 400) {
            return;
        }
        
        // Create unique key for this response
        const responseKey = `${response.method}_${response.url}_${response.loadedAt?.getTime() || Date.now()}`;
        
        // Skip if already processed
        if (processedResponses.has(responseKey)) {
            console.log('[Unpoly] Response already processed:', responseKey);
            return;
        }
        
        processedResponses.add(responseKey);
        
        // Clean up old entries (keep only last 10)
        if (processedResponses.size > 10) {
            const firstKey = processedResponses.values().next().value;
            processedResponses.delete(firstKey);
        }
        
        console.log('[Unpoly] Processing error response:', response.status);
        
        try {
            // Parse the full HTML response
            const parser = new DOMParser();
            const doc = parser.parseFromString(response.text, 'text/html');
            const flashDataInResponse = doc.querySelector('#flash-data');
            
            if (flashDataInResponse) {
                console.log('[Unpoly] Found flash-data in response HTML');
                console.log('[Unpoly] Flash-data content:', flashDataInResponse.innerHTML);
                
                // Extract and show messages immediately
                getFlashMessages(flashDataInResponse);
            } else {
                console.log('[Unpoly] No flash-data found in response');
            }
        } catch (e) {
            console.error('[Unpoly] Error parsing response:', e);
        }
    });
}
