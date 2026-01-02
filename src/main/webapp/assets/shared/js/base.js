window.__APP_MODE__ = "development";

// Enable Unpoly logging
if (typeof up !== 'undefined') {
    up.log.enable();
}

import {initUnpoly} from "./modules/unpoly.js";
import {getFlashMessages, refillFormData} from "./modules/utils.js";

// ==== Make getFlashMessages available globally for Unpoly ====
window.getFlashMessages = getFlashMessages;

// ==== Global Unpoly workflow ====
initUnpoly();

// ==== Flash from SSR (initial page load) ====
getFlashMessages();

// ==== Handle Unpoly form submissions inside modals ====
if (typeof up === 'undefined') {
    console.error("Unpoly not found!");
}

// ==== Open embedded partial as modal if present ====
// Detects when page loads with an embedded partial (for deeplink restoration)
up.compiler('body[data-has-embedded-partial]', function(element) {
    const embeddedContainer = document.getElementById('embedded-partial');
    if (!embeddedContainer) return;
    
    const partialPath = embeddedContainer.dataset.partialPath;
    console.log('[Base] Found embedded partial:', partialPath);
    
    // Find the actual partial content (element with up-main="modal")
    const partialElement = embeddedContainer.querySelector('[up-main~=modal]');
    if (!partialElement) {
        console.error('[Base] Embedded partial missing [up-main~=modal]');
        return;
    }
    
    // Extract the partial HTML
    const partialHTML = partialElement.outerHTML;
    
    // Get the base URL (home page) - this is the actual page URL we're on
    // When modal closes, we want to restore to this URL
    const baseUrl = window.location.pathname.replace(/\/auth\/.*$/, '/home');
    
    // First, update browser history to the base page (without triggering navigation)
    // This ensures when modal closes, we're at the correct URL
    window.history.replaceState({}, '', baseUrl);
    
    // Open modal with the pre-rendered content
    setTimeout(() => {
        up.layer.open({
            content: partialHTML,
            mode: 'modal',
            history: partialPath, // Modal URL shows while open
            baseLayer: 'root',
            onOpened: () => {
                console.log('[Base] Embedded partial opened as modal');
                // Refill form data if needed
                setTimeout(() => refillFormData(), 100);
            },
            onDismissed: () => {
                // When modal is dismissed, ensure we're at home URL
                console.log('[Base] Modal dismissed, URL should be:', baseUrl);
            }
        }).catch(err => {
            console.error('[Base] Failed to open embedded partial:', err);
        });
    }, 50);
    
    // Remove the embedded partial container after extracting
    embeddedContainer.remove();
});

