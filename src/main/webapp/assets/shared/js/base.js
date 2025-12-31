// ==== Initialize APP_MODE before anything else ====
window.__APP_MODE__ = "${initParam.APP_MODE}";

console.log('Base.js started loading...');
console.log('APP_MODE:', window.__APP_MODE__);

// Import the original modal.js for modal management (Bootstrap modals)
import { Modal } from "./modules/modal.js";
import { getFlashMessages, getReopenModal, initResponseHandler, refillFormData, setupLogging } from "./modules/utils.js";

console.log('All imports loaded successfully');

// ==== TEMPORARILY SKIP setupLogging to see all debug logs ====
// setupLogging();
console.log('Skipping setupLogging for debugging');

// ==== Make getFlashMessages available globally for Unpoly ====
window.getFlashMessages = getFlashMessages;

// ==== Initialize response handler for modal reopening ====
initResponseHandler();
console.log('Response handler initialized');

// ==== Flash from SSR (initial page load) ====
getFlashMessages();
console.log('Flash messages processed');

// ==== Modal System (using original Bootstrap-based modal.js) ====
console.log('About to call Modal.init...');
try {
    await Modal.init({
        CONTAINER_SELECTOR: "#modal-container",
        TRIGGER_SELECTOR: ".btn-open-modal",
        preloadList: [], // Skip preloading entirely for now
    });
    console.log('Modal system initialized');
} catch (e) {
    console.error('Modal.init failed:', e);
}

// ==== Handle Unpoly form submissions inside modals ====
console.log('Setting up Unpoly handlers, up exists:', typeof up !== 'undefined');
if (typeof up !== 'undefined') {
    // Compiler for flash-data - runs whenever #flash-data is inserted/updated
    up.compiler('#flash-data', function(element) {
        console.log('Flash data element compiled by Unpoly');
        getFlashMessages();
    });
    
    up.on('up:fragment:loaded', function(event) {
        console.log('Unpoly fragment loaded');
        
        // Check for redirect header from server (successful login)
        const redirectUrl = event.response.header('X-Up-Location');
        if (redirectUrl) {
            console.log('Login success, closing modal and navigating to:', redirectUrl);
            
            // Close the Bootstrap modal properly (including backdrop)
            const openModal = document.querySelector('.modal.show');
            if (openModal) {
                const bsModal = bootstrap.Modal.getInstance(openModal);
                if (bsModal) {
                    // Listen for modal fully hidden before navigating
                    openModal.addEventListener('hidden.bs.modal', function() {
                        // Remove any leftover backdrop
                        document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
                        document.body.classList.remove('modal-open');
                        document.body.style.removeProperty('padding-right');
                        document.body.style.removeProperty('overflow');
                        
                        // Process any flash messages first
                        getFlashMessages();
                        
                        // Navigate to the redirect URL
                        window.location.href = redirectUrl;
                    }, { once: true });
                    
                    bsModal.hide();
                    return;
                }
            }
            
            // Fallback if no modal found
            getFlashMessages();
            window.location.href = redirectUrl;
            return;
        }
        
        // Process flash messages from response - check if fragment exists first
        if (event.fragment) {
            const flashData = event.fragment.querySelector ? 
                              event.fragment.querySelector('#flash-data') : null;
            if (flashData || event.fragment.id === 'flash-data') {
                console.log('Processing flash messages from response');
                getFlashMessages();
            }
        }
    });
    console.log('Unpoly handlers registered');
} else {
    console.warn('Unpoly not found!');
}

// ==== Reopen modal if required (from cache or server response) ====
const reopen = getReopenModal();
console.log('Reopen modal:', reopen);
if (reopen) {
    Modal.open(reopen);
    // Refill form data AFTER modal is opened (when DOM elements exist)
    refillFormData();
}

console.log('Base.js initialization complete');