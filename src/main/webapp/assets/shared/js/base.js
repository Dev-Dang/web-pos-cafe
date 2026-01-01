window.__APP_MODE__ = "development";

// Enable Unpoly logging
if (typeof up !== 'undefined') {
    up.log.enable();
}

// Import the original modal.js for modal management (Bootstrap modals)
import {Modal} from "./modules/modal.js";
import {initUnpoly} from "./modules/unpoly.js";
import {getFlashMessages, getReopenModal, initResponseHandler, refillFormData} from "./modules/utils.js";

// ==== Make getFlashMessages available globally for Unpoly ====
window.getFlashMessages = getFlashMessages;

// ==== Initialize response handler for modal reopening ====
initResponseHandler();

// ==== Global Unpoly workflow ====
initUnpoly();

// ==== Flash from SSR (initial page load) - ONLY for non-Unpoly responses ====
// For Unpoly responses, flash messages are handled by Unpoly event handlers
if (!window.location.href.includes('unpoly')) {
    getFlashMessages();
}

// ==== Modal System (using original Bootstrap-based modal.js) ====
try {
    await Modal.init({
        CONTAINER_SELECTOR: "#modal-container",
        TRIGGER_SELECTOR: ".btn-open-modal",
        preloadList: [], // Skip preloading entirely for now
    });
} catch (e) {
    console.error("Modal.init failed:", e);
}

// ==== Handle Unpoly form submissions inside modals ====
if (typeof up === 'undefined') {
    console.error("Unpoly not found!");
}

// ==== Reopen modal if required (from cache or server response) ====
const reopen = getReopenModal();
if (reopen) {
    Modal.open(reopen);
    // Refill form data AFTER modal is opened (when DOM elements exist)
    refillFormData();
}
